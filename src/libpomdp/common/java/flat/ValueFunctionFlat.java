/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: 
 * Description: flat representation of a value function
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.flat;

// imports
import java.io.Serializable;

import libpomdp.common.java.BelState;
import libpomdp.common.java.Utils;
import libpomdp.common.java.ValueFunction;

import org.math.array.LinearAlgebra;

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

    // return value of a belief state
    public double V(BelState bel) {
	double b[] = bel.getPoint();
	double dotProds[] = LinearAlgebra.times(v, b);
	int argmax = Utils.argmax(dotProds);
	// save the index of the alpha that supports this belief point
	bel.setAlpha(argmax);
	double max = dotProds[argmax];
	return max;
    }

    // return flat value function
    public double[][] getvFlat() {
	return v;
    }    

    // direct contol using this value function as a policy
    // actions start from 0
    public int directControl(BelState bel) {
	double b[] = bel.getPoint();
	double dotProds[] = LinearAlgebra.times(v, b);
	int argmax = Utils.argmax(dotProds);
	return a[argmax];
    }

} // valueFunctionFlat
