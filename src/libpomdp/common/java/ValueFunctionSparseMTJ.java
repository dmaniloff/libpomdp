/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: ValueFunctionSparseMTJ.java
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

// imports
import java.io.*;
import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.sparse.*;

public class ValueFunctionSparseMTJ implements ValueFunction, Serializable {
    
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
    public ValueFunctionSparseMTJ(CompColMatrix v, int a[]) {
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
	long start = System.currentTimeMillis();
	SparseVector b = ((BeliefStateSparseMTJ)bel).bSparse;
	SparseVector dotProds = new SparseVector(v.numRows());
	dotProds = (SparseVector) v.mult(b, dotProds);
	// there must be a way to avoid this!!
	int argmax = Util.argmax(Matrices.getArray(dotProds)); 
	//Matrix argmax = dotProds.indexOfMax(Ret.NEW, 0);
	// save the index of the alpha that supports this belief point
	bel.setplanid(argmax);
	double max = dotProds.norm(Vector.Norm.Infinity);
	System.out.println("elapsed in V: " + (System.currentTimeMillis() - start));
	return max;
    }

    // return flat value function
    public double[][] getvFlat() {
	return Matrices.getArray(v);
    }    

} // ValueFunctionSparseMTJ
