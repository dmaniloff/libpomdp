/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: 
 * Description: flat representation of a value function
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

// imports
import org.math.array.*;
import java.io.*;

public class ValueFunctionFlat implements ValueFunction, Serializable {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // serial id
    static final long serialVersionUID = 2L;

    // represent a value function via an array of doubles
    private double v[][];

    // actions associated to each alpha vector
    private int a[];

    // constructor
    public ValueFunctionFlat(double v[][], int a[]) {
	this.v   = v; 
	this.a   = a;
    }

    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------

    // list of actions associated with each alpha
    public int[] getActions() {
	return a;
    }

    public int getSize() {
	return a.length;
    }
    
    // return value of a belief state
    public double V(BeliefState bel) {
	double b[] = bel.getbPoint();
	double dotProds[] = LinearAlgebra.times(v, b);
	int argmax = Util.argmax(dotProds);
	// save the index of the alpha that supports this belief point
	bel.setplanid(argmax);
	double max = dotProds[argmax];
	return max;
    }

    // return flat value function
    public double[][] getvFlat() {
	return v;
    }    

    // direct contol using this value function as a policy
    // actions start from 0
    public int directControl(BeliefState bel) {
	double b[] = bel.getbPoint();
	double dotProds[] = LinearAlgebra.times(v, b);
	int argmax = Util.argmax(dotProds);
	return a[argmax];
    }

} // ValueFunctionFlat
