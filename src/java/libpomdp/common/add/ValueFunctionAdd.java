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

package libpomdp.common.add;

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

    // actions associated to each alpha vector
    private int a[];

    private AddConfiguration conf;
    
    // constructor
    public ValueFunctionAdd(DD vAdd[], int a[],AddConfiguration conf) {
	this.vAdd = vAdd;
	this.a = a;
	this.conf = conf;
    }

    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------



	// return value of a belief state
    public double value(BeliefState bel) {
	// declarations
	DD b;
	DD m[];
	double dotProds[];
	// compute dot products
	if (bel instanceof BeliefStateAdd) {
	    b = ((BeliefStateAdd) bel).bAdd;
	    dotProds = OP.dotProductNoMem(b, vAdd, conf.staIds);
	} else {
	    m = ((BeliefStateFactoredAdd) bel).marginals;
	    dotProds = OP.factoredExpectationSparseNoMem(m, vAdd);
	}
	// find best vector
	int argmax = Utils.argmax(dotProds);
	// save the index of the alpha that supports this belief point
	bel.setAlpha(argmax);
	return dotProds[argmax];
    }

    // list of actions associated with each alpha
    
    public int[] getActions() {
    	return a;
    }    
    
    public AlphaVector getAlpha(int idx) {
    	return(new AlphaVectorAdd(vAdd[idx],a[idx]));
    }

    
    public CustomVector getAlphaValues(int idx) {
    	double[][] val = OP.convert2array(vAdd, conf.staIds);
    	return new CustomVector(val[idx]);
    }

    
    public int size() {
	return a.length;
    }

    
    public void sort() {
    	// Can a set of Adds be sorted?
	// TODO Auto-generated method stub
    	Utils.warning("Add-based value functions cannot be sorted yet...");
    }

    // return Add representation of this value function
    public DD[] getvAdd() {
	return vAdd;
    }

	public ValueFunction copy() {
		DD newVAdd[] = new DD[vAdd.length];
		int newA[] = new int[a.length];
		System.arraycopy(vAdd, 0, newVAdd, 0, vAdd.length);
		System.arraycopy(a, 0, newA, 0, a.length);
		return(new ValueFunctionAdd(newVAdd,newA,conf));
	}

	public void crossSum(ValueFunction rewardValueFunction) {
		// Can ADDs be crossSummed?
		// TODO Auto-generated method stub
		Utils.error("crossSum not implemented yet for Adds");
	}

	public AlphaVector getBestAlpha(BeliefState bel) {
		// TODO Auto-generated method stub
		Utils.error("getBestAlpha not implemented yet for Adds");
		return null;
	}

	public AlphaVector getUpperBound() {
		Utils.error("getUpperBound not implemented yet for Adds");
		// TODO Auto-generated method stub
		return null;
	}

	public void merge(ValueFunction vfA) {
		Utils.error("merge not implemented yet for Adds");
		// TODO Auto-generated method stub
		
	}

	public double performance(ValueFunction oldv, int convCriteria) {
		Utils.error("performance not implemented yet for Adds");
		// TODO Auto-generated method stub
		return 0;
	}

	public long prune(double delta) {
		Utils.error("prune not implemented yet for Adds");
		// TODO Auto-generated method stub
		return 0;
	}

	public long prune() {
		Utils.error("prune not implemented yet for Adds");
		// TODO Auto-generated method stub
		return 0;
	}

	public void push(AlphaVector vec) {
		Utils.error("prune not implemented yet for Adds");
		// TODO Auto-generated method stub
		
	}

} // valueFunctionAdd
