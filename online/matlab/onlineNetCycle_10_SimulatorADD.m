% --------------------------------------------------------------------------- %
% libpomdp
% ========
% File: onlineNetCycle_9_SimulatorADD.m
% Description: 
% Copyright (c) 2009, 2010 Diego Maniloff
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
javaaddpath '../../offline/java'
javaaddpath '../../online/java'

% add to the matlab path
% addpath     '../../external/symPerseusMatlab' -end
% addpath     '../../offline/matlab' -end

%% load problem parameters - factored representation
factoredProb  = pomdpAdd  ('../../problems/network/cycle10.SPUDD');

%% compute offline lower and upper bounds
% blindCalc = blindAdd;
% lBound    = blindCalc.getBlindAdd(factoredProb);

% qmdpCalc  = qmdpAdd;
% uBound    = qmdpCalc.getqmdpAdd(factoredProb);

%% load them in case we have them saved
load 'saved-data/network/blindAdd_NetCycle_10.mat';
load 'saved-data/network/qmdpAdd_NetCycle_10.mat';

%% create heuristic search AND-OR tree
% instantiate an aems2 heuristic object
aems2h  = aems2(factoredProb);

%% there is only one possible initial belief state
states = [];
states(end+1) = factoredProb.getnrSta - 1;

%% play the pomdp - set main parameter first
NUM_MACHINES      = 10;

logFilename = sprintf('simulation-logs/network/marginals/online-LOG-NetCycle-%d-%s-ADD.log',...
                      NUM_MACHINES, date);
diary(logFilename);

% parameters
EPSILON_ACT_TH    = 1e-3;
MAXPLANNINGTIME   = 1.0;
EPISODECOUNT      = 500;
MAXEPISODELENGTH  = 60;
TOTALRUNS         = size(states,2);

% stats
cumR              = [];
all.avcumrews     = [];
all.avTs          = [];
all.avreusedTs    = [];
all.avexps        = [];
all.avfoundeopt   = [];


for run = 1:TOTALRUNS
    
    fprintf(1, '///////////////////////////// RUN %d of %d \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n',...
        run, TOTALRUNS);
    
    % stats
    all.stats{run}.cumrews      = [];
    all.stats{run}.foundeopt    = [];
    all.stats{run}.meanT        = [];
    all.stats{run}.meanreusedT  = [];
    all.stats{run}.meanexps     = [];
    
    % start this run
    for ep = 1:EPISODECOUNT

        fprintf(1, '********************** EPISODE %d of %d *********************\n', ep, EPISODECOUNT);
        
        % re - initialize tree at starting belief
        aoTree = [];
        aoTree = AndOrTree(factoredProb, aems2h, lBound, uBound);
        aoTree.init(factoredProb.getInit());
        rootNode = aoTree.getRoot();

        % starting state for this set of EPISODECOUNT episodes
        factoredS = [factoredProb.staIds' ; ...
            factoredProb.sdecode(states(run), factoredProb.getnrStaV, factoredProb.staArity)'];
        
        % stats
        cumR = 0;
        fndO = 0;
        all.stats{run}.ep{ep}.R        = [];
        all.stats{run}.ep{ep}.exps     = [];
        all.stats{run}.ep{ep}.T        = [];
        all.stats{run}.ep{ep}.reusedT  = [];
        
        for iter = 1:MAXEPISODELENGTH
            
            fprintf(1, '******************** INSTANCE %d ********************\n', iter);
            tc = cell(factoredProb.printS(factoredS));
            fprintf(1, 'Current world state is:         %s\n', tc{1});
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
            
            % start stopwatch
            tic
            while toc < MAXPLANNINGTIME
                % expand best node
                aoTree.expand(rootNode.bStar);
                % update its ancestors
                aoTree.updateAncestors(rootNode.bStar);
                % expand counter
                expC = expC + 1;
                % check whether we found an e-optimal action
                if (aoTree.currentBestActionIsOptimal(EPSILON_ACT_TH))
                    toc
                    fprintf(1, 'Found e-optimal action, aborting expansions\n');
                    fndO = fndO + 1;
                    break;
                end
            end
            
            % obtain the best action for the root
            % remember that a's and o's in matlab should start from 1
            a = aoTree.currentBestAction();
            a = a + 1;

            % execute it and receive new o
            restrictedT = OP.restrictN(factoredProb.T(a), factoredS);
            factoredS1  = OP.sampleMultinomial(restrictedT, factoredProb.staIdsPr);
            restrictedO = OP.restrictN(factoredProb.O(a), [factoredS, factoredS1]);
            factoredO   = OP.sampleMultinomial(restrictedO, factoredProb.obsIdsPr);


            % save stats
            all.stats{run}.ep{ep}.R(end+1)     = OP.eval(factoredProb.R(a), factoredS);
            all.stats{run}.ep{ep}.T(end+1)     = rootNode.subTreeSize;
            cumR = cumR + ...
                factoredProb.getGamma^(iter - 1) * all.stats{run}.ep{ep}.R(end);
            all.stats{run}.ep{ep}.exps(end+1)  = expC;

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

        all.stats{run}.cumrews     (end+1) = cumR;
        all.stats{run}.foundeopt   (end+1) = fndO;
        all.stats{run}.meanT       (end+1) = mean(all.stats{run}.ep{ep}.T);
        all.stats{run}.meanreusedT (end+1) = mean(all.stats{run}.ep{ep}.reusedT);
        all.stats{run}.meanexps    (end+1) = mean(all.stats{run}.ep{ep}.exps);
        %pause
        
    end % episodes loop
    
    % average cum reward of this run of 20 episodes
    all.avcumrews  (end+1) = mean(all.stats{run}.cumrews);
    all.avfoundeopt(end+1) = mean(all.stats{run}.foundeopt);
    all.avTs       (end+1) = mean(all.stats{run}.meanT);
    all.avreusedTs (end+1) = mean(all.stats{run}.meanreusedT);
    all.avexps     (end+1) = mean(all.stats{run}.meanexps);
    
end % runs loop

% save statistics before quitting
statsFilename = ...
    sprintf('simulation-logs/network/marginals/online-ALLSTATS-NetCycle-%d-%s-ADD.mat',...
            NUM_MACHINES, date);
save(statsFilename, 'all');
