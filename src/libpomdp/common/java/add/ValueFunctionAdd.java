/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: valueFunctionAdd.java
 * Description: implementation of a value function via ADDs
 *              makes use of Poupart's OP class to manipulate ADDs
 *              see README reference [5]
 *              implements Serializable so we can use the save command
 *              in Matlab
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.add;

// imports
import java.io.Serializable;

import libpomdp.common.java.BelState;
import libpomdp.common.java.Utils;
import libpomdp.common.java.ValueFunction;
import libpomdp.common.java.symbolic.DD;
import libpomdp.common.java.symbolic.OP;

public class ValueFunctionAdd implements ValueFunction, Serializable {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // serial id
    static final long serialVersionUID = 5L;

    // represent a value function via an array of Adds
    private DD vAdd[];

    // staIds of the problem
    private int staIds[];

    // actions associated to each alpha vector
    private int a[];

    // constructor
    public ValueFunctionAdd(DD vAdd[], int staIds[], int a[]) {
	this.vAdd   = vAdd; 
	this.a      = a;
	this.staIds = staIds;
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
	// declarations
	DD     b;
	DD     m[];
	double dotProds[];
	// compute dot products
	if (bel instanceof BelStateAdd) {
	    b = ((BelStateAdd)bel).bAdd; 
	    dotProds = OP.dotProductNoMem(b, vAdd, staIds);
	} else {
	    m = ((BelStateFactoredAdd)bel).marginals;
	    dotProds = OP.factoredExpectationSparseNoMem(m, vAdd);
	}
	// find best vector
	int argmax = Utils.argmax(dotProds);
	// save the index of the alpha that supports this belief point
	bel.setplanid(argmax);
	return dotProds[argmax];
    }

    // return flat value function
    public double[][] getvFlat() {
	return OP.convert2array(vAdd, staIds);
    }    

    // return Add representation of this value function
    public DD[] getvAdd() {
	return vAdd;
    }

} // valueFunctionAdd
