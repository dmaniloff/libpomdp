% --------------------------------------------------------------------------- %
% libpomdp
% ========
% File: .m
% Description: m script to instantiate aoTree and heuristic objects to combine
%              them with different lower bounds -
%              uses part of Spaan's and Poupart's packages - see README
%              references [1,5]
% Copyright (c) 2009, 2010 Diego Maniloff
% W3: http://www.cs.uic.edu/~dmanilof
% --------------------------------------------------------------------------- %

%% preparation
clear all
clear java
clear java

% add dynamic classpath
javaaddpath '../../../../../external/jmatharray.jar'
javaaddpath '../../../../../external/antlr-3.2.jar'
javaaddpath '../../../../../external/mtj-0.9.12.jar'
javaaddpath '../../../../../external/symPerseusJava.jar'
javaaddpath '../../../../../dist/libpomdp.jar'

% java imports
import symPerseusJava.*;
import libpomdp.common.java.*;
import libpomdp.common.java.standard.*;
import libpomdp.online.java.*;
import libpomdp.offline.java.*;

%% load problem
standardProb = PomdpStandard  ('../../../problems/homecare/homecare.pomdp');

%% load pre-computed offline bounds
load '../../../problems/rocksample/7-8/RockSample_7_8_blind_ADD.mat';
load '../../../problems/rocksample/7-8/RockSample_7_8_qmdp_ADD.mat';

%% create heuristic search AND-OR tree
% instantiate an aems2 heuristic object
aems2h  = AEMS2(factoredProb);

%% play the pomdp
logFilename = sprintf('simulation-logs/rocksample/RS78-online-AEMS2-STD-%s.log', datestr(now, 'yyyy-mmm-dd-HHMMSS'));
diary(logFilename);

% rocksample parameters for the grapher
GRID_SIZE         = 7;
ROCK_POSITIONS    = [2 0; 0 1; 3 1; 6 3; 2 4; 3 4; 5 5; 1 6];
SARTING_POS       = [0 3];
drawer            = RocksampleGraph;
NUM_ROCKS         = size(ROCK_POSITIONS,1);

% parameters
EPSILON_ACT_TH       = 1e-3;
EPISODECOUNT         = 10;
MAXPLANNINGTIME      = 1.0;
MAXEPISODELENGTH     = 100;
TOTALRUNS            = 2^NUM_ROCKS;
USE_FACTORED_BELIEFS = 1;

% stats
cumR              = [];
all.avcumrews     = [];
all.avTs          = [];
all.avreusedTs    = [];
all.avexps        = [];
all.avfoundeopt   = [];

% print general config problem parameters
fprintf(1, '+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++');
fprintf(1, 'libpomdp log - config parameters');
fprintf(1, '--------------------------------');
fprintf(1, 'TOTALRUNS            = %d\n', TOTALRUNS);
fprintf(1, 'EPISODECOUNT         = %d\n', EPISODECOUNT);
fprintf(1, 'MAXEPISODELENGTH     = %d\n', MAXEPISODELENGTH);
fprintf(1, 'MAXPLANNINGTIME      = %d\n', MAXPLANNINGTIME);
fprintf(1, 'EPSILON_ACT_TH       = %d\n', EPSILON_ACT_TH);
fprintf(1, 'USE_FACTORED_BELIEFS = %d\n', USE_FACTORED_BELIEFS);
fprintf(1, '+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++');


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
        
        % are we approximating beliefs with the product of marginals?
        if USE_FACTORED_BELIEFS
          b_init    = javaArray('symPerseusJava.DD', 1);          
          b_init(1) = factoredProb.getInitialBeliefState().bAdd;
          b_init    = BelStateFactoredAdd( ...
              OP.marginals(b_init,factoredProb.getstaIds(),factoredProb.getstaIdsPr()), ...
              factoredProb.getstaIds());
        else
          b_init    = factoredProb.getInit();
        end
        
        % re - initialize tree at starting belief
        aoTree = AndOrTree(factoredProb, aems2h, lBound, uBound);
        aoTree.init(b_init);
        rootNode = aoTree.getRoot();

        % starting state for this set of EPISODECOUNT episodes
        factoredS = [factoredProb.getstaIds()' ; ...
            1 + SARTING_POS, 1 + bitget((run-1), NUM_ROCKS:-1:1)];
        
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
            drawer.drawState(GRID_SIZE, ROCK_POSITIONS,factoredS);
            if strcmp(rootNode.getBeliefState().getClass.toString, ...
                    'class libpomdp.common.java.add.BeliefStateFactoredAdd')
              fprintf(1, 'Current belief agree prob:      %d\n', ...                       
                      OP.evalN(rootNode.getBeliefState().marginals, factoredS));
            else
              fprintf(1, 'Current belief agree prob:      %d\n', ... 
                      OP.eval(rootNode.belief.bAdd, factoredS));
            end            
            fprintf(1, 'Current |T| is:                 %d\n', rootNode.getSubTreeSize());

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
                % check whether we found an e-optimal action - there is another criteria
                % here too!!
                %if (abs(rootNode.u-rootNode.l) < 1e-3)
                if (aoTree.actionIsEpsOptimal(aoTree.currentBestAction(), EPSILON_ACT_TH))
                    % toc
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
            factoredS1  = OP.sampleMultinomial(restrictedT, factoredProb.getstaIdsPr());
            restrictedO = OP.restrictN(factoredProb.O(a), [factoredS, factoredS1]);
            factoredO   = OP.sampleMultinomial(restrictedO, factoredProb.getobsIdsPr());


            % save stats
            all.stats{run}.ep{ep}.R(end+1)     = OP.eval(factoredProb.R(a), factoredS);
            all.stats{run}.ep{ep}.T(end+1)     = rootNode.getSubTreeSize();
            cumR = cumR + ...
                factoredProb.getGamma^(iter - 1) * all.stats{run}.ep{ep}.R(end);
            all.stats{run}.ep{ep}.exps(end+1)  = expC;

            % output some stats
            fprintf(1, 'Expansion finished, # expands:  %d\n'  , expC);
            % this will count an extra |A||O| nodes given the expansion of the root
            fprintf(1, '|T|:                            %d\n'  , rootNode.getSubTreeSize());
            tc = cell(factoredProb.getActionString(a-1));
            fprintf(1, 'Outputting action:              %s\n'  , tc{1});
            tc = cell(factoredProb.printO(factoredO));
            fprintf(1, 'Perceived observation:          %s\n'  , tc{1});
            fprintf(1, 'Received reward:                %.2f\n', all.stats{run}.ep{ep}.R(end));
            fprintf(1, 'Cumulative reward:              %.2f\n', cumR);

            % check whether this episode ended (terminal state)
            if(factoredS1(2,1) == GRID_SIZE+1)
                fprintf(1, '==================== Episode ended at instance %d==================\n', iter);
                drawer.drawState(GRID_SIZE, ROCK_POSITIONS,factoredS1);
                break;
            end

            % transform factoredO into absolute o 
            o = Util.sencode(factoredO(2,:), ...
                factoredProb.getnrObsV(), ...
                factoredProb.getobsArity());
            % compute an exact update of the new belief we will move into...this should not matter for RS!
            % bPrime = factoredProb.factoredtao(rootNode.belief,a-1,o-1);
            % move the tree's root node
            aoTree.moveTree(rootNode.getChild(a-1).getChild(o-1)); 
            % update reference to rootNode
            rootNode = aoTree.getRoot();
            % replace its factored belief by an exact one....this should not matter for RS!
            % rootNode.belief = bPrime;
            
            fprintf(1, 'Tree moved, reused |T|:         %d\n', rootNode.getSubTreeSize());
            all.stats{run}.ep{ep}.reusedT(end+1)  = rootNode.getSubTreeSize();
            
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
    sprintf('simulation-logs/rocksample/RS78-online-ALLSTATS-AEMS2-STD-%s.mat', datestr(now, 'yyyy-mmm-dd-HHMMSS'));
save(statsFilename, 'all');