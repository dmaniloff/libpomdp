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

% refresh java
clear java
% clear all
clear all
% add dynamic classpath
javaaddpath '../../external/jmatharray.jar'
javaaddpath '../../external/symPerseusClasses'
javaaddpath '../../general/java'
javaaddpath '../../online/java'
javaaddpath '../../offline/java'

% add to the matlab path
addpath     '../../offline/matlab/' -end

%% load problem parameters

%run ../../general/problems/tiger/initProblem.m
run ../../general/problems/tag/initProblem.m
global problem;
nrS = problem.nrStates;
nrA = problem.nrActions;
nrO = problem.nrObservations;
T   = problem.transitionS;
R   = problem.rewardS;
O   = problem.observationS;
g   = problem.gamma;

% instantiate a java pomdpFlat object
flatProb = pomdpFlat(cell2mat(O),...
    cell2mat(T),...
    cell2mat(R),...
    nrS,...
    nrA,...
    nrO,...
    g,...
    problem.actions,...
    problem.observations,...
    problem.start);

%% compute offline lower and upper bounds

% use Qmdp as offline upper bound
% V_mdp  = mdp(nrS,nrA,T,R,g);
% Q_mdp  = Q_vec(nrS,nrA,T,R,g,V_mdp);
% uBound = valueFunction(Q_mdp, 1:nrA);

mdpcalc = mdp(flatProb);
uBound  = mdpcalc.getQmdp();

% use Rmin/(1-g) as offline lower bound
V_rmin = repmat(min(problem.rewards)/(1-g),1, nrS);
lBound = valueFunction(V_rmin, []);

%% create heuristic search AND-OR tree

% instantiate an aems2 heuristic object
aems2h  = aems2(flatProb);
% instantiate AndOrTree
aoTree = AndOrTree(flatProb, aems2h, lBound, uBound);
% create flat representation of the initial belief state
startBel = belStateFlat([problem.start], -1);
aoTree.init(startBel);
rootNode = aoTree.getroot();

%% play the pomdp
MAXPLANNINGTIME   = 5;
MAXEPISODELENGTH  = 100;

stats.R           = [];
stats.cumR        = 0;

% initial belief and state
b = problem.start;
s = find(cumsum(b) > rand, 1, 'first');

for i = 1:MAXEPISODELENGTH
    
    fprintf(1, '******************** INSTANCE %d ********************\n', i);
    fprintf(1, 'Current state is:               %s\n', problem.states(s,:));
    fprintf(1, 'Current |T| is:                 %d\n', rootNode.subTreeSize);
    
    % start stopwatch
    tic
    while toc < MAXPLANNINGTIME
        % expand best node
        aoTree.expand(rootNode.bStar);
        % update its ancestors
        aoTree.updateAncestors(rootNode.bStar);
    end
    
    % obtain the best action for the root
    % remember that a's and o's in matlab should start from 1
    a = rootNode.bestA;
    a = a + 1;
    
    % execute it and receive new belief and new o
    % may not need to waste time computing b1 here
    [s1,b1,o] = getSuccessor(b,s,a);
    
    % save stats
    stats.R(end+1) = problem.reward(s,a);
    stats.cumR     = stats.cumR + problem.gamma^(i - 1) * problem.reward(s,a);
    
    % output some stats
    fprintf(1, 'Expansion finished, |T|:        %d\n', rootNode.subTreeSize);
    fprintf(1, 'Outputting action:              %s\n', problem.actions(a,:));
    fprintf(1, 'Perceived observation:          %s\n', problem.observations(o,:));
    fprintf(1, 'Received reward:                %.2f\n', problem.reward(s,a));
    fprintf(1, 'Cumulative reward:              %.2f\n', stats.cumR);
    
    % check whether this episode ended
%     if (problem.reward(s,a) == problem.goodReward)
%         fprintf(1, 'Episode ended at instance %d\n', i);
%         break;
%     end
    %pause;
    
    % move the tree's root node
    aoTree.moveTree(rootNode.children(a).children(o));
    % update reference to rootNode
    rootNode = aoTree.getroot();
    
    fprintf(1, 'Tree moved, reused |T|:         %d\n', rootNode.subTreeSize);
    
    % iterate
    b = b1;
    s = s1;

end
