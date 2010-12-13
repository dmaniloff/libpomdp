function realtime_netcycle_7_HYVI_reuse_ADD(REUSETHRESHOLD, USE_FACTORED_BELIEFS)
% --------------------------------------------------------------------------- %
% libpomdp
% ========
% File: .m
% Description: 
% Copyright (c) 2010, Diego Maniloff
% W3: http://www.cs.uic.edu/~dmanilof
% --------------------------------------------------------------------------- %

% check arguments
if nargin ~= 2 
  error('Error in calling function: wrong number of arguments');
end

%% preparation
% clear
clear java
clear all

% add dynamic classpath
javaaddpath '../../../../external/jmatharray.jar'
javaaddpath '../../../../external/symPerseusJava.jar'
javaaddpath '../../../../dist/libpomdp.jar'
 
% java imports
import symPerseusJava.*;
import libpomdp.common.java.*;
import libpomdp.online.java.*;
import libpomdp.offline.java.*;
import libpomdp.hybrid.java.*;
import libpomdp.problems.rocksample.*;
 
%% load problem parameters - factored representation
factoredProb  = pomdpAdd  ('../../problems/network/cycle7.SPUDD');

%% load pre-computed offline bounds
load '../../problems/netcycle/7/netcycle_7_blind_ADD.mat';
load '../../problems/netcycle/7/netcycle_7_qmdp_ADD.mat';

%% create heuristics
aems2h  = aems2(factoredProb);
dosih   = DOSI(factoredProb);

%% there is only one possible initial belief state
states = [];
states(end+1) = factoredProb.getnrSta - 1;

%% play the pomdp - set main parameters first
NUM_MACHINES      = 7;

logFilename = sprintf('simulation-logs/network/realtime-LOG-NetCycle-%d-%s-HYVI-%.1freuse-ADD.log',...
                      NUM_MACHINES, date, REUSETHRESHOLD);
diary(logFilename);

% parameters
EPSILON_ACT_TH    = 1e-3;
EPISODECOUNT      = 500;
EXPANSIONTIME     = 0.9;
BACKUPTIME        = 0.1;
TOTALPLANNINGTIME = 1.0;
MAXEPISODELENGTH  = 60;
TOTALRUNS         = size(states,2);

% stats
cumR              = [];
all.avcumrews     = [];
all.avTs          = [];
all.avreusedTs    = [];
all.avbackuptimes = [];
all.avexps        = [];
all.avbackups     = [];
all.avfoundeopt   = [];

% print general config problem parameters
fprintf(1, '+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++');
fprintf(1, 'libpomdp log - config parameters');
fprintf(1, '--------------------------------');
fprintf(1, 'TOTALRUNS            = %d\n', TOTALRUNS);
fprintf(1, 'EPISODECOUNT         = %d\n', EPISODECOUNT);
fprintf(1, 'MAXEPISODELENGTH     = %d\n', MAXEPISODELENGTH);
fprintf(1, 'EXPANSIONTIME        = %d\n', EXPANSIONTIME);
fprintf(1, 'BACKUPTIME           = %d\n', BACKUPTIME);
fprintf(1, 'REUSETHRESHOLD       = %d\n', REUSETHRESHOLD);
fprintf(1, 'EPSILON_ACT_TH       = %d\n', EPSILON_ACT_TH);
fprintf(1, 'USE_FACTORED_BELIEFS = %d\n', USE_FACTORED_BELIEFS);
fprintf(1, '+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++');


% main runs loop
for run = 1:TOTALRUNS
    
    fprintf(1, '///////////////////////////// UPDATE RUN %d of %d \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n',...
        run, TOTALRUNS);
    
    % stats
    all.stats{run}.cumrews        = [];
    all.stats{run}.backups        = [];
    all.stats{run}.foundeopt      = [];
    all.stats{run}.meanT          = [];
    all.stats{run}.meanreusedT    = [];
    all.stats{run}.meanbackuptime = [];
    all.stats{run}.meanexps       = [];
    
    
    % start this run
    for ep = 1:EPISODECOUNT

        fprintf(1, '********************** EPISODE %d of %d *********************\n',...
                ep, EPISODECOUNT);
        
        % are we approximating beliefs with the product of marginals?
        if USE_FACTORED_BELIEFS
          b_init    = javaArray('symPerseusJava.DD', 1);          
          b_init(1) = factoredProb.getInit().bAdd;
          b_init    = BelStateFactoredADD( ...
              OP.marginals(b_init,factoredProb.staIds,factoredProb.staIdsPr), ...
              factoredProb.staIds)
        else
          b_init    = factoredProd.getInit();
        end

        % re - initialize tree at starting belief
        aoTree = [];
        aoTree = AndOrTreeUpdateAdd(factoredProb, aems2h, dosih, lBound, uBound);
        aoTree.init(b_init);
        rootNode = aoTree.getRoot();

        % starting state for this set of EPISODECOUNT episodes
        factoredS = [factoredProb.staIds' ; ...
            factoredProb.sdecode(states(run), factoredProb.getnrStaV, factoredProb.staArity)'];
        
        % stats
        cumR = 0;
        bakC = 0;
        fndO = 0;
        all.stats{run}.ep{ep}.R          = [];
        all.stats{run}.ep{ep}.exps       = [];
        all.stats{run}.ep{ep}.T          = [];
        all.stats{run}.ep{ep}.reusedT    = [];
        all.stats{run}.ep{ep}.backuptime = [];
        
        for iter = 1:MAXEPISODELENGTH
            
            fprintf(1, '******************** INSTANCE %d ********************\n', iter);
            tc = cell(factoredProb.printS(factoredS));
            fprintf(1, 'Current world state is:         %s\n', tc{1});
            if rootNode.belief.getClass.toString == ...
                  'class libpomdp.common.java.add.BelStateFactoredAdd'
              fprintf(1, 'Current belief agree prob:      %d\n', ...                       
                      OP.evalN(rootNode.belief.marginals, factoredS));
            else
              fprintf(1, 'Current belief agree prob:      %d\n', ... 
                      OP.eval(rootNode.belief.bAdd, factoredS));
            end
            fprintf(1, 'Current |T| is:                 %d\n', rootNode.subTreeSize);

            % reset expand counter
            expC = 0;
            
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            % start stopwatch for expansions
            tic
            while toc < EXPANSIONTIME
                % expand best node
                aoTree.expand(rootNode.bStar);
                % update its ancestors
                aoTree.updateAncestors(rootNode.bStar);
                % expand counter
                expC = expC + 1;
            end

            
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            % BACKUP decision comes here 
            if(aoTree.expectedReuseRatio() > REUSETHRESHOLD && ...
                    rootNode.children(aoTree.currentBestAction() + 1).bakHeuristicStar > 0)
                % backup at best node according to heuristic
                tic
                aoTree.backupLowerAtNode(...
                    rootNode.children(aoTree.currentBestAction() + 1).bakCandidate);
                all.stats{run}.ep{ep}.backuptime(end+1) = toc;
                bakC = bakC + 1;
            else
            % if not, continue expanding for the remaining 0.1 secs
            %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
                tic
                while toc < BACKUPTIME
                    % expand best node
                    aoTree.expand(rootNode.bStar);
                    % update its ancestors
                    aoTree.updateAncestors(rootNode.bStar);
                    % expand counter
                    expC = expC + 1;
                end
            end
            
            % check whether we found an e-optimal action, either in the
            % EXPANSIONTIME slot or in the TOTALPLANNINGTIME slot
            if (aoTree.currentBestActionIsOptimal(EPSILON_ACT_TH))
                fprintf(1, 'Achieved e-optimal action!\n');
                fndO = fndO + 1;
            end
            
            % obtain the best action for the root
            % remember that a's and o's in matlab should start from 1
            a = aoTree.currentBestAction();
            a = a + 1;
            
            % execute best action and receive new o
            restrictedT = OP.restrictN(factoredProb.T(a), factoredS);
            factoredS1  = OP.sampleMultinomial(restrictedT, factoredProb.staIdsPr);
            restrictedO = OP.restrictN(factoredProb.O(a), [factoredS, factoredS1]);
            factoredO   = OP.sampleMultinomial(restrictedO, factoredProb.obsIdsPr);

            % save stats
            all.stats{run}.ep{ep}.exps(end+1)  = expC;
            all.stats{run}.ep{ep}.R   (end+1)  = OP.eval(factoredProb.R(a), factoredS);
            all.stats{run}.ep{ep}.T   (end+1)  = rootNode.subTreeSize;
            cumR = cumR + ...
                factoredProb.getGamma^(iter - 1) * all.stats{run}.ep{ep}.R(end);
            

            % output some stats
            fprintf(1, 'Expansion finished, # expands:  %d\n', expC);
            % this will count an extra |A||O| nodes given the expansion of the root
            fprintf(1, '|T|:                            %d\n', rootNode.subTreeSize);
            tc = cell(factoredProb.getactStr(a-1));
            fprintf(1, 'Outputting action:              %s\n', tc{1});
            tc = cell(factoredProb.printO(factoredO));
            fprintf(1, 'Perceived observation:          %s\n', tc{1});
            fprintf(1, 'Received reward:                %.2f\n', all.stats{run}.ep{ep}.R(end));
            fprintf(1, 'Cumulative reward:              %.2f\n', cumR);

            % there is no end of the world for this problem
            
            %     pause;

            % move the tree's root node
            o = factoredProb.sencode(factoredO(2,:), ...
                                     factoredProb.getnrObsV(), ...
                                     factoredProb.getobsArity()); 
            aoTree.moveTree(rootNode.children(a).children(o)); 
            % update reference to rootNode
            rootNode = aoTree.getRoot();

            fprintf(1, 'Tree moved, reused |T|:         %d\n', rootNode.subTreeSize);
            all.stats{run}.ep{ep}.reusedT(end+1)  = rootNode.subTreeSize;
            
            % iterate
            factoredS = factoredS1;
            factoredS = Config.primeVars(factoredS, -factoredProb.getnrTotV);

        end % time-steps loop

        all.stats{run}.cumrews       (end+1)   = cumR;
        all.stats{run}.backups       (end+1)   = bakC;
        all.stats{run}.foundeopt     (end+1)   = fndO;
        all.stats{run}.meanT         (end+1)   = mean(all.stats{run}.ep{ep}.T);
        all.stats{run}.meanreusedT   (end+1)   = mean(all.stats{run}.ep{ep}.reusedT);
        all.stats{run}.meanbackuptime(end+1)   = mean(all.stats{run}.ep{ep}.backuptime);
        all.stats{run}.meanexps      (end+1)   = mean(all.stats{run}.ep{ep}.exps);
        %pause
        
    end % episodes loop
    
    % average cum reward of this run of 20 episodes
    all.avcumrews    (end+1)   = mean(all.stats{run}.cumrews);
    all.avbackups    (end+1)   = mean(all.stats{run}.backups);
    all.avfoundeopt  (end+1)   = mean(all.stats{run}.foundeopt);
    all.avTs         (end+1)   = mean(all.stats{run}.meanT);
    all.avreusedTs   (end+1)   = mean(all.stats{run}.meanreusedT);
    all.avbackuptimes(end+1)   = mean(all.stats{run}.meanbackuptime);
    all.avexps       (end+1)   = mean(all.stats{run}.meanexps);
    
end % runs loop

% save statistics before quitting
statsFilename = ...
    sprintf('simulation-logs/network/realtime-ALLSTATS-NetCycle-%d-%s-HYVI-%.1freuse-ADD.mat',...
            NUM_MACHINES, date, REUSETHRESHOLD);
save(statsFilename, 'all');
