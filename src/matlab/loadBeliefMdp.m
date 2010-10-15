function ret = loadBeliefMdp(filename,filetype)
% function ret  = loadBeliefMdp(filename,filetype)
% Load a POMDP file and creates the BeliefMDP. 
% Filetype can be: 'mat', 'pomdp' and 'spudd'
% Sparsity 0 or 1 (false or true, only for 'pomdp', if not ignored)
    ret=[];
    switch filetype
        case 'mat'
            error('libpomdp:loadBeliefMdp', 'Loading .mat is not supported yet');
        case 'pomdp'
            ret=libpomdp.parser.java.FileParser.loadBeliefMdp(filename,libpomdp.parser.java.FileParser.PARSE_CASSANDRA_POMDP);
            return
        case 'spudd'
            ret=libpomdp.parser.java.FileParser.loadBeliefMdp(filename,libpomdp.parser.java.FileParser.PARSE_SPUDD);
            return
        otherwise 
            error('libpomdp:loadBeliefMdp', 'Unknown Filetype');
    end         
end

