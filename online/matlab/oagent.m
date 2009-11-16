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
addpath     '../../external/symPerseusMatlab/' -end
addpath     '../../offline/matlab/' -end

%% load problem parameters - factored representation

% factoredProb = pomdpAdd  ('../../general/problems/tiger/tiger.95.SPUDD');
% symDD        = parsePOMDP('../../general/problems/tiger/tiger.95.SPUDD');
% factoredProb = pomdpAdd('../../general/problems/coffee/coffee.90.SPUDD');
% factoredProb = pomdpAdd  ('../../general/problems/rocksample/RockSample_2_1/RockSample_2_1.SPUDD');
factoredProb = pomdpAdd  ('../../general/problems/rocksample/RockSample_7_8/RockSample_7_8.SPUDD');
symDD        = parsePOMDP('../../general/problems/rocksample/RockSample_7_8/RockSample_7_8.SPUDD');

%% load problem parameters - flat representation
% 
% % run ../../general/problems/tiger/initProblem.m
% % run ../../general/problems/tag/initProblem.m
% run ../../general/problems/rocksample/RockSample_2_1/initProblem.m
% global problem;
% nrS = problem.nrStates;
% nrA = problem.nrActions;
% nrO = problem.nrObservations;
% T   = problem.transitionS;
% R   = problem.rewardS;
% O   = problem.observationS;
% g   = problem.gamma;
% 
% % instantiate a java pomdpFlat object
% flatProb = pomdpFlat(cell2mat(O),...
%     cell2mat(T),...
%     cell2mat(R),...
%     nrS,...
%     nrA,...
%     nrO,...
%     g,...
%     problem.actions,...
%     problem.observations,...
%     problem.start);
% 
% javaProb = flatProb;

%% compute offline lower and upper bounds
% use Qmdp as offline upper bound
% V_mdp  = mdp(nrS,nrA,T,R,g);
% Q_mdp  = Q_vec(nrS,nrA,T,R,g,V_mdp);
% uBound = valueFunction(Q_mdp, 1:nrA);

%  fomdpBounds = mdp(factoredProb);
%  uBound      = fomdpBounds.getQmdp();
%  lBound      = fomdpBounds.getBlind();
% V_rmin = repmat(min(-10)/(1-factoredProb.getGamma),1, factoredProb.getnrSta);
% lBound = valueFunction(V_rmin, []);

% use Poupart's QMDP solver
[Vqmdp qmdpP] = solveQMDP(symDD);
uBound        = valueFunction(OP.convert2array(Vqmdp, factoredProb.staIds), qmdpP);

[blind blindP]= blindAdd(factoredProb);
lBound        = valueFunction(OP.convert2array(blind, factoredProb.staIds), blindP);

%% create heuristic search AND-OR tree
% instantiate an aems2 heuristic object
aems2h  = aems2(factoredProb);
% instantiate AndOrTree
aoTree = AndOrTree(factoredProb, aems2h, lBound, uBound);
aoTree.init(factoredProb.getInit());
rootNode = aoTree.getRoot();

%% play the pomdp

MAXPLANNINGTIME   = 18.0;
MAXEPISODELENGTH  = 100;

% stats counters
stats.R           = [];
stats.cumR        = 0;

% rocksample parameters
GRID_SIZE         = 7;
ROCK_POSITIONS    = [2 0; 0 1; 3 1; 6 3; 2 4; 3 4; 5 5; 1 6];

% initial belief and state
factoredS         = OP.sampleMultinomial(factoredProb.getInit.ddB, factoredProb.staIds);

for i = 1:MAXEPISODELENGTH
    
    fprintf(1, '******************** INSTANCE %d ********************\n', i);
    fprintf(1, 'Current world state is:          %s\n', cell(factoredProb.printS(factoredS)){1});
    rocksampleGraph(GRID_SIZE, ROCK_POSITIONS,factoredS);
    fprintf(1, 'Current belief agree prob:      %d\n', OP.eval(rootNode.belief.ddB, factoredS));
    fprintf(1, 'Current |T| is:                 %d\n', rootNode.subTreeSize);
    
    % reset expand counter
    expC = 0;
    % start stopwatch
    tic
    while toc < MAXPLANNINGTIME
%         fprintf(1,'before expand: %d\n', rootNode.subTreeSize);
        % expand best node
        aoTree.expand(rootNode.bStar);
%         fprintf(1,'after expand, before update: %d\n', rootNode.subTreeSize);
        % update its ancestors
        aoTree.updateAncestors(rootNode.bStar);
%         fprintf(1,'after update: %d\n', rootNode.subTreeSize);
        % expand counter
        expC = expC + 1;
    end
    
    % obtain the best action for the root
    % remember that a's and o's in matlab should start from 1
    a = rootNode.bestA;
    a = a + 1;
    
    % execute it and receive new belief and new o
    % may not need to waste time computing b1 here
    % [s1,b1,o] = getSuccessor(b,s,a);
    restrictedT = OP.restrictN(factoredProb.T(a), factoredS); 
    factoredS1  = OP.sampleMultinomial(restrictedT, factoredProb.staIdsPr);
    restrictedO = OP.restrictN(factoredProb.O(a), [factoredS, factoredS1]);
    factoredO   = OP.sampleMultinomial(restrictedO, factoredProb.obsIdsPr);

    
    % save stats
    stats.R(end+1) = OP.eval(factoredProb.R(a), factoredS);
    stats.cumR     = stats.cumR + factoredProb.getGamma^(i - 1) * stats.R(end);
    
    % output some stats
    fprintf(1, 'Expansion finished, # expands:  %d\n', expC);
    % this will count an extra |A||O| nodes given the expansion of the root
    fprintf(1, '|T|:                            %d\n', rootNode.subTreeSize);
    fprintf(1, 'Outputting action:              %s\n', cell(factoredProb.getactStr(a-1)){1});
    fprintf(1, 'Perceived observation:          %s\n', cell(factoredProb.printO(factoredO)){1});
    fprintf(1, 'Received reward:                %.2f\n', stats.R(end));
    fprintf(1, 'Cumulative reward:              %.2f\n', stats.cumR);
    
    % check whether this episode ended
    if(factoredS1(2,1)==7)
        fprintf(1, 'Episode ended at instance %d\n', i);
        break;
    end
%     if (problem.reward(s,a) == problem.goodReward)
%         fprintf(1, 'Episode ended at instance %d\n', i);
%         break;
%     end
%     pause;
    
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
