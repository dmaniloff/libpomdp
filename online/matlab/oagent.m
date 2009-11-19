% --------------------------------------------------------------------------- %
% libpomdp
% ========
% File: oagent.m
% Description: m script to instantiate aoTree and heuristic objects to combine
%              them with different lower bounds - 
%              uses part of Spaan's and Poupart's packages - see README 
%              references [1,5]
% Copyright (c) 2009, Diego Maniloff
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
% addpath     '../../offline/matlab' -end

%% load problem parameters - factored representation

% factoredProb = pomdpAdd  ('../../general/problems/tiger/tiger.95.SPUDD');
% symDD        = parsePOMDP('../../general/problems/tiger/tiger.95.SPUDD');
% factoredProb = pomdpAdd('../../general/problems/coffee/coffee.90.SPUDD');
% factoredProb = pomdpAdd  ('../../general/problems/rocksample/RockSample_2_1/RockSample_2_1.SPUDD');
factoredProb = pomdpAdd  ('../../general/problems/rocksample/RockSample_7_8/RockSample_7_8.SPUDD');
% symDD        = parsePOMDP('../../general/problems/rocksample/RockSample_7_8/RockSample_7_8.SPUDD');



%% compute offline lower and upper bounds
blindCalc = blindAdd;
lBound    = blindCalc.getBlindAdd(factoredProb);

% qmdpCalc  = qmdpAdd;
% uBound    = qmdpCalc.getqmdpAdd(factoredProb);

% use Poupart's QMDP solver
[Vqmdp qmdpP] = solveQMDP(symDD);
% uBound        = valueFunction(OP.convert2array(Vqmdp, factoredProb.staIds), qmdpP);
uBound        = valueFunctionAdd(Vqmdp, factoredProb.staIds, qmdpP);

%% load them in case we have them saved
load 'saved-data/blindAdd_RockSample_7_8.mat';
% load 'saved-data/qmdpAdd_RockSample_7_8.mat';
load 'saved-data/qmdpSymPerseus_RockSample_7_8.mat';
%% create heuristic search AND-OR tree
% instantiate an aems2 heuristic object
aems2h  = aems2(factoredProb);
% instantiate AndOrTree
aoTree = AndOrTree(factoredProb, aems2h, lBound, uBound);
aoTree.init(factoredProb.getInit());
rootNode = aoTree.getRoot();

%% play the pomdp

MAXPLANNINGTIME   = 2.0;
MAXEPISODELENGTH  = 100;

% stats counters
stats.R           = [];
stats.cumR        = 0;
stats.expands     = [];

% rocksample parameters for the grapher
GRID_SIZE         = 7;
ROCK_POSITIONS    = [2 0; 0 1; 3 1; 6 3; 2 4; 3 4; 5 5; 1 6];
drawer            = rocksampleGraph;

% initial belief and state
factoredS         = OP.sampleMultinomial(factoredProb.getInit.bAdd, factoredProb.staIds);

for iter = 1:MAXEPISODELENGTH
    
    fprintf(1, '******************** INSTANCE %d ********************\n', iter);
    fprintf(1, 'Current world state is:         %s\n', cell(factoredProb.printS(factoredS)){1});
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
        if (abs(rootNode.u-rootNode.l) < 1e-3)
            toc
            fprintf(1, 'Found e-optimal action, aborting expansions\n');
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
    stats.R(end+1) = OP.eval(factoredProb.R(a), factoredS);
    stats.cumR     = stats.cumR + factoredProb.getGamma^(iter - 1) * stats.R(end);
    stats.expands(end+1)  = expC;
    
    % output some stats
    fprintf(1, 'Expansion finished, # expands:  %d\n', expC);
    % this will count an extra |A||O| nodes given the expansion of the root
    fprintf(1, '|T|:                            %d\n', rootNode.subTreeSize);
    fprintf(1, 'Outputting action:              %s\n', cell(factoredProb.getactStr(a-1)){1});
    fprintf(1, 'Perceived observation:          %s\n', cell(factoredProb.printO(factoredO)){1});
    fprintf(1, 'Received reward:                %.2f\n', stats.R(end));
    fprintf(1, 'Cumulative reward:              %.2f\n', stats.cumR);
    
    % check whether this episode ended (terminal state)
    if(factoredS1(2,1)==8)
        fprintf(1, '***************** Episode ended at instance %d\n', iter);
        break;
    end
    
    pause;
    
    % move the tree's root node
    o = prod(double(factoredO(2,:)));
    aoTree.moveTree(rootNode.children(a).children(o)); % check this!
    % update reference to rootNode
    rootNode = aoTree.getRoot();
    
    fprintf(1, 'Tree moved, reused |T|:         %d\n', rootNode.subTreeSize);
    
    % iterate
    %b = b1;
    factoredS = factoredS1;
    factoredS = Config.primeVars(factoredS, -factoredProb.nrTotV);

end

