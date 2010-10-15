function ret  = loadPomdp(filename,filetype)
% function ret  = loadPomdp(filename,filetype,sparsity)
% Load a POMDP file. 
% Filetype can be: 'mat', 'pomdp' and 'spudd'
    ret=[];
    switch filetype
        case 'mat'
            error('libpomdp:loadPomdp', 'Loading .mat is not supported yet');
        case 'pomdp'
            ret=libpomdp.parser.java.FileParser.loadPomdp(filename,libpomdp.parser.java.FileParser.PARSE_CASSANDRA_POMDP);
            return
        case 'spudd'
            ret=libpomdp.parser.java.FileParser.loadPomdp(filename,libpomdp.parser.java.FileParser.PARSE_SPUDD);
            return
        otherwise 
            error('libpomdp:loadPomdp', 'Unknown Filetype');
    end         
end

