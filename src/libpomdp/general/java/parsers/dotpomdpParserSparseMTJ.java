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

package libpomdp.general.java.parsers;

// imports
import org.antlr.runtime.*;
import java.io.*;

public class dotpomdpParserSparseMTJ {

    static pomdpSpecSparseMTJ dotpomdpSpec = null;

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

    public pomdpSpecSparseMTJ getSpec() {
	return dotpomdpSpec;
    }
}