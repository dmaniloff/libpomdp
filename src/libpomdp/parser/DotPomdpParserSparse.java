/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: dotpomdpParserSparseMTJ.java
 * Description: Simple class to parse a .POMDP file and return
 *              an object of type pomdpSpecSparseMTJ with all the problem
 *              parameters
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
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
        dotpomdpMTJParser parser = new dotpomdpMTJParser(tokens);

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