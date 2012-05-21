/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: DotPomdpParserStd.java
 * Description: Simple class to parse a .POMDP file
 * Copyright (c) 2009, 2010 Diego Maniloff
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.parser;

// imports
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class DotPomdpParserStd {

    static PomdpSpecStd dotpomdpSpec = null;

    public static void parse (String filename) throws Exception {
	DotPomdpLexer lex = new DotPomdpLexer(new ANTLRFileStream(filename));
       	CommonTokenStream tokens = new CommonTokenStream(lex);
        DotPomdpParser parser = new DotPomdpParser(tokens);
        try {
            parser.dotPomdp();
        } catch (RecognitionException e)  {
            e.printStackTrace();
        }

	dotpomdpSpec = parser.getSpec();


    }

    public static PomdpSpecStd getSpec() {
	return dotpomdpSpec;
    }
}