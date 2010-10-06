function ret  = loadPomdp(filename,filetype,sparsity)
% function ret  = loadPomdp(filename,filetype,sparsity)
% Load a POMDP file. 
% Filetype can be: 'mat', 'pomdp' and 'spudd'
% Sparsity 0 or 1 (false or true, only for 'pomdp', if not ignored)
    ret=[];
    switch filetype
        case 'mat'
            error('libpomdp:loadPomdp', 'Loading .mat is not supported yet');
        case 'pomdp'
            ret=libpomdp.parser.java.FileParser.loadPomdp(filename,libpomdp.parser.java.FileParser.PARSE_CASSANDRA_POMDP,sparsity);
            return
        case 'spudd'
            ret=libpomdp.parser.java.FileParser.loadPomdp(filename,libpomdp.parser.java.FileParser.PARSE_SPUDD,sparsity);
            return
        otherwise 
            error('libpomdp:loadPomdp', 'Unknown Filetype');
    end         
end

