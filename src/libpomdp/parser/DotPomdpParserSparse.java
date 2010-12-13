/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: DotPomdpParserSparse.java
 * Description: Simple class to parse a .POMDP file and return
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.parser;

// imports
import org.antlr.runtime.*;
import java.io.*;

public class DotPomdpParserSparse {

    static PomdpSpecSparse dotpomdpSpec = null;

    public static void parse (String filename) throws Exception {
	dotpomdpMTJLexer lex = new dotpomdpMTJLexer(new ANTLRFileStream(filename));
       	CommonTokenStream tokens = new CommonTokenStream(lex);
        DotPomdpParser parser = new DotPomdpParser(tokens);
        try {
            parser.dotpomdp();
        } catch (RecognitionException e)  {
            e.printStackTrace();
        }

	dotpomdpSpec = parser.getSpec();

	
    }

    public PomdpSpecSparse getSpec() {
	return dotpomdpSpec;
    }
}