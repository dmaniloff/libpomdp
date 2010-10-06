%% preparation
% clear
clear java
clear all
% add dynamic classpath
javaaddpath 'lib/mtj.jar'
javaaddpath 'lib/jmatharray.jar'
javaaddpath 'lib/antlr3.jar'
javaaddpath 'dist/libpomdp.jar'

% add to the matlab path
addpath     'src/libpomdp/parser/matlab' -end

