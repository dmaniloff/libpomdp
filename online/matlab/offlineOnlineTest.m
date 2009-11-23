% --------------------------------------------------------------------------- %
% libpomdp
% ========
% File: offlineOnlineTest.m
% Description: generic script to test the improvement of offline bounds
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
aems2h   = aems2(factoredProb);
% instantiate AndOrTree
aoTree   = AndOrTree(factoredProb, aems2h, lBound, uBound);
aoTree.init(factoredProb.getInit());
rootNode = aoTree.getRoot();

%% offline test of online tree search
OFF_TEST_TIME = 1.0;
NUM_EXPANDS   = 100;
NUM_TRIALS    = 50;

expstats      = [];

for j=1:NUM_TRIALS
    expc          = 0;
    tic
    while toc < OFF_TEST_TIME
        % while expc < NUM_EXPANDS
        %     tic
        %         fprintf(1,'before expand: %d\n', rootNode.subTreeSize);
        % expand best node
        aoTree.expand(rootNode.bStar);
        %         fprintf(1,'after expand, before update: %d\n', rootNode.subTreeSize);
        % update its ancestors
        aoTree.updateAncestors(rootNode.bStar);
        %         fprintf(1,'after update: %d\n', rootNode.subTreeSize);
        %     toc
        % expand counter
        expc = expc + 1;
        %     fprintf(1,'upper: %d    lower: %d  \n',rootNode.u,rootNode.l);
    end
    % fprintf(1,'expanded at a rate of %.4f expands / sec\n', expc / OFF_TEST_TIME);
    expc
    expstats(end+1) = expc;
end
mean(expstats)

%% speed of tao
TAO_TEST = 40;
b1 = factoredProb.getInit;
times = [];

for i = 1:TAO_TEST
    tic
    b2 = factoredProb.tao(b1,floor(rand*factoredProb.getnrAct),floor(rand*factoredProb.getnrObs));
    times(end+1,:) = toc;
    b1 = b2;
end
mean(times)
