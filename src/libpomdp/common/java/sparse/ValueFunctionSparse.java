/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: valueFunctionSparseMTJ.java
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.sparse;

// imports
import java.io.Serializable;

import libpomdp.common.java.BelState;
import libpomdp.common.java.Utils;
import libpomdp.common.java.ValueFunction;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.CompColMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

public class ValueFunctionSparse implements ValueFunction, Serializable {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // serial id
    static final long serialVersionUID = 4L;

    // represent a value function via a Matrix object
    private CompColMatrix v;

    // actions associated to each alpha vector
    private int a[];

    // constructor
    public ValueFunctionSparse(CompColMatrix v, int a[]) {
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
	long start = System.currentTimeMillis();
	SparseVector b = ((BelStateSparse)bel).bSparse;
	SparseVector dotProds = new SparseVector(v.numRows());
	dotProds = (SparseVector) v.mult(b, dotProds);
	// there must be a way to avoid this!!
	int argmax = Utils.argmax(Matrices.getArray(dotProds)); 
	//Matrix argmax = dotProds.indexOfMax(Ret.NEW, 0);
	// save the index of the alpha that supports this belief point
	bel.setAlpha(argmax);
	double max = dotProds.norm(Vector.Norm.Infinity);
	System.out.println("elapsed in V: " + (System.currentTimeMillis() - start));
	return max;
    }

    // return flat value function
    public double[][] getvFlat() {
	return Matrices.getArray(v);
    }    

} // valueFunctionSparseMTJ
