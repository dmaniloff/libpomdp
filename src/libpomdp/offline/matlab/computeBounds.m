function computeBounds(problem_filename, output_dir)

% --------------------------------------------------------------------------- %
% libpomdp
% ========
% File: computeBounds
% Description: compute upper and lower bounds for a given problem
%              assumes blind and qmdp for now, and the SPUDD format
% Copyright (c) 2010, Diego Maniloff
% W3: http://www.cs.uic.edu/~dmanilof
% --------------------------------------------------------------------------- %

% check arguments
if nargin ~= 2
  error('Error in calling function: wrong number of arguments');
end

%% preparation

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
import libpomdp.problems.rocksample.*;

% add to the matlab path - only needed if we use Poupart's QMDP solver
% addpath     '../../external/symPerseusMatlab' -end

%% load problem parameters - factored representation
factoredProb = pomdpAdd(problem_filename);

%% compute offline lower and upper bounds
blindCalc = blindAdd;
lBound    = blindCalc.getBlindAdd(factoredProb);

qmdpCalc  = qmdpAdd;
uBound    = qmdpCalc.getqmdpAdd(factoredProb);

%% save data
problem_name = textscan(problem_filename,'%s','Delimiter', '/');
problem_name = problem_name{1}{end};
problem_name = textscan(problem_name, '%s', 'Delimiter', '.');
problem_name = problem_name{1}{end};

save(strcat(output_dir, '/', problem_name, '_blind_ADD.mat'), 'lBound');
save(strcat(output_dir, '/', problem_name, '_qmdp_ADD.mat' ), 'uBound');
