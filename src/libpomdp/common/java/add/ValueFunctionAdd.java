/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: ValueFunctionAdd.java
 * Description: implementation of a value function via ADDs
 *              makes use of Poupart's OP class to manipulate ADDs
 *              see README reference [5]
 *              implements Serializable so we can use the save command
 *              in Matlab
 * Copyright (c) 2009, 2010 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.common.java.add;

// imports
import java.io.Serializable;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.Utils;
import libpomdp.common.ValueFunction;
import libpomdp.common.add.symbolic.DD;
import libpomdp.common.add.symbolic.OP;

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

    // return value of a belief state
    @Override
    public double V(BeliefState bel) {
        // declarations
        DD     b;
        DD     m[];
        double dotProds[];
        // compute dot products
        if (bel instanceof BeliefStateAdd) {
            b = ((BeliefStateAdd)bel).bAdd; 
            dotProds = OP.dotProductNoMem(b, vAdd, staIds);
        } else {
            m = ((BeliefStateFactoredAdd)bel).marginals;
            dotProds = OP.factoredExpectationSparseNoMem(m, vAdd);
        }
        // find best vector
        int argmax = Utils.argmax(dotProds);
        // save the index of the alpha that supports this belief point
        bel.setAlpha(argmax);
        return dotProds[argmax];
    }


    @Override
    public CustomVector getVector(int idx) {
        double[][] val=OP.convert2array(vAdd, staIds);
        return new CustomVector(val[idx]);
    }


    // list of actions associated with each alpha
    
    public int[] getActions() {
	return a;
    }

    
    public AlphaVector getAlpha(int idx) {
	// TODO Auto-generated method stub
	return null;
    }

    
    public CustomVector getAlphaValues(int idx) {
	double[][] val = OP.convert2array(vAdd, staIds);
	return new CustomVector(val[idx]);
    }

    
    public int size() {
	return a.length;
    }

    
    public void sort() {
	// TODO Auto-generated method stub
    }


    // unused?
    // return flat value function
    public double[][] getvFlat() {
	return OP.convert2array(vAdd, staIds);
    }    

    // return Add representation of this value function
    public DD[] getvAdd() {
	return vAdd;
    }

	public CustomVector getAlphaValues(int idx) {
		double[][] val=OP.convert2array(vAdd, staIds);
		return new CustomVector(val[idx]);
	}

	public int size() {
		return a.length;
	}

	public CustomVector getVectorRef(int idx) {
		System.out.println("Warning: getVectorRef is not implemented for ADD representation, passing a copy...");
		return getAlphaValues(idx);
	}

	public void sort() {
		// TODO Auto-generated method stub
		
	}

	public AlphaVector getAlpha(int idx) {
		// TODO Auto-generated method stub
		return null;
	}

} // valueFunctionAdd
