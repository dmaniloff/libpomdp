% --------------------------------------------------------------------------- %
% libpomdp
% ========
% File: realtimeTagSimulator-DOSI-ADD-01.m
% Description: online agent simulator with updates to the offline bounds
% Copyright (c) 2010, Diego Maniloff
% W3: http://www.cs.uic.edu/~dmanilof
% --------------------------------------------------------------------------- %
%% preparation
% clear
clear java
clear all
% add dynamic classpath
javaaddpath '../../external/jmatharray.jar'
javaaddpath '../../external/symPerseusJava'
javaaddpath '../../general/java'
javaaddpath '../../problems/catch'
javaaddpath '../../offline/java'
javaaddpath '../../online/java'

% add to the matlab path
addpath     '../../external/symPerseusMatlab' -end

%% load problem parameters - factored representation
factoredProb  = pomdpAdd  ('../../problems/catch/catch_tag_taggingAction_10_5.SPUDD');
symDD         = parsePOMDP('../../problems/catch/catch_tag_taggingAction_10_5.SPUDD');

%% compute offline lower and upper bounds
blindCalc = blindAdd;
lBound    = blindCalc.getBlindAdd(factoredProb);

qmdpCalc  = qmdpAdd;
uBound    = qmdpCalc.getqmdpAdd(factoredProb);

% use Poupart's QMDP solver
%[Vqmdp qmdpP] = solveQMDP(symDD);
%uBound        = valueFunctionAdd(Vqmdp, factoredProb.staIds, qmdpP - 1);

%% load them in case we have them saved
% load 'saved-data/blindAdd_RockSample_7_8.mat';
% load 'saved-data/qmdpAdd_RockSample_7_8.mat';
% load 'saved-data/qmdpSymPerseus_RockSample_7_8.mat';
% load 'saved-data/catch-tag/qmdp_SymPerseus_catch_tag_taggingaction.mat';
% load 'saved-data/catch-tag/blind_catch_tag_taggingaction.mat';

%% create heuristics
aems2h  = aems2(factoredProb);
dosih   = DOSI(factoredProb);

%% figure out all possible initial states of the pomdp
states  = [];
for r=1:factoredProb.getnrSta
    factoredS = [factoredProb.staIds'; ...
        factoredProb.sdecode(r-1, factoredProb.getnrStaV, factoredProb.staArity)'];
    if (OP.eval(factoredProb.getInit.bAdd , factoredS) > 0)
        states(end+1) = r - 1;
    end
end

%% play the pomdp - set main parameter first
REUSETHRESHOLD    = 0.7;

logFilename = sprintf('simulation-logs/catch-tag/marginals/realtime-LOG-tag-%s-DOSI-%.1freuse-ADD.log',...
                      date, REUSETHRESHOLD);
diary(logFilename);

% catch parameters for the grapher
drawer            = CatchGraph(10, 5, CatchTagGrid(10,5));
COLLOCATED_REWARD = 10.0;

% parameters
EPSILON_ACT_TH    = 1e-3;
EPISODECOUNT      = 5;
EXPANSIONTIME     = 0.9;
BACKUPTIME        = 0.1;
TOTALPLANNINGTIME = 1.0;
MAXEPISODELENGTH  = 100;
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
        
        % re - initialize tree at starting belief
        aoTree = [];
        aoTree = AndOrTreeUpdateAdd(factoredProb, aems2h, dosih, lBound, uBound);
        aoTree.init(factoredProb.getInit());
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
            drawer.drawState(factoredS);
            if rootNode.belief.getClass.toString == 'class BelStateFactoredADD'
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

            % check whether this episode ended (reward for being collocated)
            if(all.stats{run}.ep{ep}.R(end) == COLLOCATED_REWARD)
                fprintf(1, '==================== Episode ended at instance %d==================\n', iter);
                drawer.drawState(factoredS1);
                break;
            end

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
            %b = b1;
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
statsFilename = sprintf('simulation-logs/catch-tag/marginals/realtime-ALLSTATS-tag-%s-DOSI-%.1freuse-ADD.mat',...
                        date, REUSETHRESHOLD);
save(statsFilename, 'all');
