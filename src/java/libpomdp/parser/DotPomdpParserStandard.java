/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: DotPomdpParserSparse.java
 * Description: Simple class to parse a .POMDP file and return
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.parser.java;


import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class DotPomdpParserStandard {

    //static PomdpSpecStandard dotpomdpSpec = null;

    public static PomdpSpecStandard parse (String filename) throws Exception {
	DotPomdpLexer lex = new DotPomdpLexer(new ANTLRFileStream(filename));
       	CommonTokenStream tokens = new CommonTokenStream(lex);
        DotPomdpParser parser = new DotPomdpParser(tokens);
        try {
            parser.dotPomdp();
        } catch (RecognitionException e)  {
            e.printStackTrace();
        }

	//dotpomdpSpec = parser.getSpec();
	
	return parser.getSpec();

    }

//    public static PomdpSpecStandard getSpec() {
//	return dotpomdpSpec;
//    }
}