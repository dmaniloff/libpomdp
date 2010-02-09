% --------------------------------------------------------------------------- %
% libpomdp
% ========
% File: onlineRockSampleSimulatorUpdateADD.m
% Description: online agent simulator with updates to the offline bounds
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
javaaddpath '../../general/problems/rocksample'
javaaddpath '../../offline/java'
javaaddpath '../../online/java'

% add to the matlab path
addpath     '../../external/symPerseusMatlab' -end
% addpath     '../../external/utils' -end
% addpath     '../../offline/matlab' -end

%% load problem parameters - factored representation

% factoredProb = pomdpAdd  ('../../general/problems/tiger/tiger.95.SPUDD');
% symDD        = parsePOMDP('../../general/problems/tiger/tiger.95.SPUDD');
% factoredProb = pomdpAdd('../../general/problems/coffee/coffee.90.SPUDD');
% factoredProb = pomdpAdd  ('../../general/problems/rocksample/RockSample_2_1/RockSample_2_1.SPUDD');
factoredProb = pomdpAdd  ('../../general/problems/rocksample/RockSample_7_8/RockSample_7_8.SPUDD');
% symDD        = parsePOMDP('../../general/problems/rocksample/RockSample_7_8/RockSample_7_8.SPUDD');



%% compute offline lower and upper bounds
% blindCalc = blindAdd;
% lBound    = blindCalc.getBlindAdd(factoredProb);
% 
% % qmdpCalc  = qmdpAdd;
% % uBound    = qmdpCalc.getqmdpAdd(factoredProb);
% 
% % use Poupart's QMDP solver
% [Vqmdp qmdpP] = solveQMDP(symDD);
% % uBound        = valueFunction(OP.convert2array(Vqmdp, factoredProb.staIds), qmdpP);
% uBound        = valueFunctionAdd(Vqmdp, factoredProb.staIds, qmdpP);

%% load them in case we have them saved
load 'saved-data/blindAdd_RockSample_7_8.mat';
% load 'saved-data/qmdpAdd_RockSample_7_8.mat';
load 'saved-data/qmdpSymPerseus_RockSample_7_8.mat';

%% compute exact solution to compare
% % write .aplha for pomdp-solve
% writeTonyAlpha(lBound.getvFlat, lBound.getActions + 1, 'simulation-logs/tiger/blind.alpha');
% % read .alpha form pomdp-solve
% [Vv Va] = readTonyAlpha('simulation-logs/tiger/tiger.95-8024.alpha');
% % plot
% for i=1:size(Vv,1)
%     plot([0 1], Vv(i,:));
%     hold on;
% end
%% create heuristics
aems2h  = aems2(factoredProb);
dosih   = DOSI(factoredProb);

%% play the pomdp
diary(['simulation-logs/rocksample/7-8-online-run-BACKUPS-',date,'-07reuse.log']);

% parameters
EPISODECOUNT      = 20;
MAXPLANNINGTIME   = 1.0;
MAXEPISODELENGTH  = 100;
TOTALRUNS         = 2^8;
REUSETHRESHOLD    = 0.7;

% stats
cumR              = [];
all.avcumrews     = [];
all.avTs          = [];
all.avreusedTs    = [];
all.avplantimes   = [];
all.avexps        = [];
all.avbackups     = [];
all.avfoundeopt   = [];

% rocksample parameters for the grapher
GRID_SIZE         = 7;
ROCK_POSITIONS    = [2 0; 0 1; 3 1; 6 3; 2 4; 3 4; 5 5; 1 6];
drawer            = rocksampleGraph;

for run = 1:TOTALRUNS
    
    fprintf(1, '///////////////////////////// UPDATE RUN %d of %d \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n',...
        run, TOTALRUNS);
    
    % stats
    all.stats{run}.cumrews      = [];
    all.stats{run}.backups      = [];
    all.stats{run}.foundeopt    = [];
    all.stats{run}.meanT        = [];
    all.stats{run}.meanreusedT  = [];
    all.stats{run}.meanplantime = [];
    all.stats{run}.meanexps     = [];
    
    
    % start this run
    for ep = 1:EPISODECOUNT

        fprintf(1, '********************** EPISODE %d of %d *********************\n', ep, EPISODECOUNT);
        
        % re - initialize tree at starting belief
        aoTree = [];
        aoTree = AndOrTreeUpdateAdd(factoredProb, aems2h, dosih, lBound, uBound);
        aoTree.init(factoredProb.getInit());
        rootNode = aoTree.getRoot();

        % starting state for this set of EPISODECOUNT episodes
        factoredS = [factoredProb.staIds' ; 1, 4, 1 + bitget(uint8(run-1), 8:-1:1)];
        
        % stats
        cumR = 0;
        bakC = 0;
        fndO = 0;
        all.stats{run}.ep{ep}.R        = [];
        all.stats{run}.ep{ep}.exps     = [];
        all.stats{run}.ep{ep}.T        = [];
        all.stats{run}.ep{ep}.reusedT  = [];
        all.stats{run}.ep{ep}.plantime = [];
        
        for iter = 1:MAXEPISODELENGTH
            
            fprintf(1, '******************** INSTANCE %d ********************\n', iter);
            tc = cell(factoredProb.printS(factoredS));
            fprintf(1, 'Current world state is:         %s\n', tc{1});
            drawer.drawState(GRID_SIZE, ROCK_POSITIONS,factoredS);
            fprintf(1, 'Current belief agree prob:      %d\n', OP.eval(rootNode.belief.bAdd, factoredS));
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
                % check whether we found an e-optimal action - there is another criteria
                % here too!!
                %if (abs(rootNode.u-rootNode.l) < 1e-3)
                if (currentBestActionIsOptimal(1e-3))
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

            % UPDATE decision comes here %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            if(aoTree.expectedReuseRatio() > REUSETHRESHOLD && ...
                    rootNode.children(a).bakHeuristicStar > 0)
                fprintf(1, 'Going overtime for lower bound improvement\n');
                % bakup at best node - for now, keep the rest of the tree after
                % a move
                aoTree.backupLowerAtNode(rootNode.children(a).bakCandidate);
                bakC = bakC + 1;
            end
            
            % save planning time (not very accurate)
            all.stats{run}.ep{ep}.plantime(end+1) = toc;
            
            
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

            % check whether this episode ended (terminal state)
            if(factoredS1(2,1)==8)
                fprintf(1, '==================== Episode ended at instance %d==================\n', iter);
                drawer.drawState(GRID_SIZE, ROCK_POSITIONS,factoredS1);
                break;
            end

            %     pause;

            % move the tree's root node
            o = prod(double(factoredO(2,:)));
            aoTree.moveTree(rootNode.children(a).children(o)); % check this!
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
        all.stats{run}.backups     (end+1) = bakC;
        all.stats{run}.foundeopt   (end+1) = fndO;
        all.stats{run}.meanT       (end+1) = mean(all.stats{run}.ep{ep}.T);
        all.stats{run}.meanreusedT (end+1) = mean(all.stats{run}.ep{ep}.reusedT);
        all.stats{run}.meanplantime(end+1) = mean(all.stats{run}.ep{ep}.plantime);
        all.stats{run}.meanexps    (end+1) = mean(all.stats{run}.ep{ep}.exps);
        %pause
        
    end % episodes loop
    
    % average cum reward of this run of 20 episodes
    all.avcumrews  (end+1) = mean(all.stats{run}.cumrews);
    all.avbackups  (end+1) = mean(all.stats{run}.backups);
    all.avfoundeopt(end+1) = mean(all.stats{run}.foundeopt);
    all.avTs       (end+1) = mean(all.stats{run}.meanT);
    all.avreusedTs (end+1) = mean(all.stats{run}.meanreusedT);
    all.avplantimes(end+1) = mean(all.stats{run}.meanplantime);
    all.avexps     (end+1) = mean(all.stats{run}.meanexps);
    
end % runs loop

% onlineRockSampleSimulatorUpdateADD