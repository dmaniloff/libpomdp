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

package libpomdp.general.java;

// imports
import org.math.array.*;
import java.io.*;

public class valueFunctionAdd implements valueFunction, Serializable {
    
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
    public valueFunctionAdd(DD vAdd[], int staIds[], int a[]) {
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
    public double V(belState bel) {
	// declarations
	DD     b;
	DD     m[];
	double dotProds[];
	// compute dot products
	if (bel instanceof belStateAdd) {
	    b = ((belStateAdd)bel).bAdd; 
	    dotProds = OP.dotProductNoMem(b, vAdd, staIds);
	} else {
	    m = ((BelStateFactoredADD)bel).marginals;
	    dotProds = OP.factoredExpectationSparseNoMem(m, vAdd);
	}
	// find best vector
	int argmax = Common.argmax(dotProds);
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
