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


import libpomdp.common.CustomVector;
import libpomdp.common.ValueFunction;
import libpomdp.common.std.ValueFunctionStd;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class DotAlphaParserStd {

    static Integer actions[];
    static Double alphas[][];

    public static void parse(String filename) throws Exception {
	DotAlphaLexer lex = new DotAlphaLexer(new ANTLRFileStream(filename));
	CommonTokenStream tokens = new CommonTokenStream(lex);
	DotAlphaParser parser = new DotAlphaParser(tokens);

	try {
	    parser.dotAlpha();
	} catch (RecognitionException e) {
	    e.printStackTrace();
	}

	actions = parser.getActions();
	alphas = parser.getAlphas();
    }

    public ValueFunction getValueFunction() {
	int s = actions.length;
	int d = alphas[0].length;
	ValueFunctionStd v = new ValueFunctionStd();
	// convert from Integer to int and Double to double
	for (int i = 0; i < s; i++) {
	    CustomVector vec = new CustomVector(d);
	    for (int j = 0; j < d; j++)
		vec.set(j, alphas[i][j].doubleValue());
	    v.newAlpha(vec, actions[i].intValue());
	}
	// generate flat value function
	return v;
    }

}