% --------------------------------------------------------------------------- %
% libpomdp
% ========
% File: 
% Description: 
% Copyright (c) 2009, 2010 Diego Maniloff
% W3: http://www.cs.uic.edu/~dmanilof
% --------------------------------------------------------------------------- %

%% preparation
% clear
clear all
clear java
clear java

% add dynamic classpath
javaaddpath '../../../../external/jmatharray.jar'
javaaddpath '../../../../external/symPerseusJava.jar'
javaaddpath '../../../../dist/libpomdp.jar'

% java imports
import symPerseusJava.*;
import libpomdp.general.java.*;
import libpomdp.online.java.*;
import libpomdp.offline.java.*;
import libpomdp.hybrid.java.*;
import libpomdp.problems.catchproblem.java.*;

%% load problem 
factoredProb  = pomdpAdd  ('../../problems/catchproblem/catch_tag_hgrid_taggingAction_10_7.SPUDD');

%% load pre-computed offline bounds
load '../../problems/catchproblem/catch_tag_hgrid_taggingAction_10_7_blind_ADD.mat';
load '../../problems/catchproblem/catch_tag_hgrid_taggingAction_10_7_qmdp_ADD.mat';

%% create heuristic search AND-OR tree
% instantiate an aems2 heuristic object
aems2h  = aems2(factoredProb);

%% figure out all possible initial states of the pomdp
states  = [];
for r=1:factoredProb.getnrSta
    factoredS = [factoredProb.getstaIds()'; ...
        Common.sdecode(r-1, factoredProb.getnrStaV, factoredProb.staArity)'];
    if (OP.eval(factoredProb.getInit.bAdd , factoredS) > 0)
        states(end+1) = r - 1;
    end
end

% of all 2352 possible states, remove every other one to reduce the runs to 1176
states(2:2:end) = [];

%% play the pomdp
diary(['simulation-logs/catchproblem/catch_tag_hgrid_taggingAction_10_7-online-run-AEMS2-',date,'.log']);

% catch parameters for the grapher
drawer            = CatchGraph(10, 7, CatchTagHGrid(10,7));
COLLOCATED_REWARD = 10.0;

% other parameters
EPSILON_ACT_TH       = 1e-3;
EPISODECOUNT         = 5;
MAXPLANNINGTIME      = 1.0;
MAXEPISODELENGTH     = 100;
TOTALRUNS            = size(states,2);
USE_FACTORED_BELIEFS = 1;

% stats
cumR              = [];
all.avcumrews     = [];
all.avTs          = [];
all.avreusedTs    = [];
all.avexps        = [];
all.avfoundeopt   = [];

% print general config problem parameters
fprintf(1, '+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n');
fprintf(1, 'libpomdp log - config parameters\n');
fprintf(1, '--------------------------------\n');
fprintf(1, 'TOTALRUNS            = %d\n', TOTALRUNS);
fprintf(1, 'EPISODECOUNT         = %d\n', EPISODECOUNT);
fprintf(1, 'MAXEPISODELENGTH     = %d\n', MAXEPISODELENGTH);
fprintf(1, 'MAXPLANNINGTIME      = %d\n', MAXPLANNINGTIME);
fprintf(1, 'EPSILON_ACT_TH       = %d\n', EPSILON_ACT_TH);
fprintf(1, 'USE_FACTORED_BELIEFS = %d\n', USE_FACTORED_BELIEFS);
fprintf(1, '+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n');
  

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
          b_init(1) = factoredProb.getInit().bAdd;
          b_init    = BelStateFactoredADD( ...
              OP.marginals(b_init,factoredProb.getstaIds(),factoredProb.getstaIdsPr()), ...
              factoredProb.getstaIds());
        else
          b_init    = factoredProb.getInit();
        end
        
        % re - initialize tree at starting belief
        aoTree = [];
        aoTree = AndOrTree(factoredProb, aems2h, lBound, uBound);
        aoTree.init(b_init);
        rootNode = aoTree.getRoot();

        % starting state for this set of EPISODECOUNT episodes
        factoredS = [factoredProb.getstaIds()' ; ...
            Common.sdecode(states(run), factoredProb.getnrStaV, factoredProb.staArity)'];
        
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
            drawer.drawState(factoredS);
            if strcmp(rootNode.belief.getClass.toString,...
                      'class libpomdp.general.java.BelStateFactoredADD')
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
                if (aoTree.actionIsEpsOptimal(aoTree.currentBestAction(), EPSILON_ACT_TH))
                    toc
                    % fprintf(1, 'Found e-optimal action, aborting expansions\n');
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
            all.stats{run}.ep{ep}.T(end+1)     = rootNode.subTreeSize;
            cumR = cumR + ...
                factoredProb.getGamma^(iter - 1) * all.stats{run}.ep{ep}.R(end);
            all.stats{run}.ep{ep}.exps(end+1)  = expC;

            % output some stats
            fprintf(1, 'Expansion finished, # expands:  %d\n'  , expC);
            % this will count an extra |A||O| nodes given the expansion of the root
            fprintf(1, '|T|:                            %d\n'  , rootNode.subTreeSize);
            tc = cell(factoredProb.getactStr(a-1));
            fprintf(1, 'Outputting action:              %s\n'  , tc{1});
            tc = cell(factoredProb.printO(factoredO));
            fprintf(1, 'Perceived observation:          %s\n'  , tc{1});
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
            o = Common.sencode(factoredO(2,:), ...
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
save (['simulation-logs/catchproblem/catch_tag_hgrid_taggingAction_10_7-ALLSTATS-online-run-AEMS2-',date,'.mat'], 'all');

% onlineTagHgridSimulatorADD