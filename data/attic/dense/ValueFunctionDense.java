/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: 
 * Description: flat representation of a value function
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.dense;

// imports
import java.io.Serializable;

import libpomdp.common.BeliefState;
import libpomdp.common.Utils;
import libpomdp.common.ValueFunction;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Vector;

public class ValueFunctionDense implements ValueFunction, Serializable {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // serial id
    static final long serialVersionUID = 2L;

    // represent a value function via an array of doubles
    private DenseMatrix v;

    // actions associated to each alpha vector
    private int a[];

    // constructor
    public ValueFunctionDense(double v[][], int a[]) {
    	this(new DenseMatrix(v),a);
    }

    public ValueFunctionDense(DenseMatrix v, int a[]) {
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
    public double V(BeliefState bel) {
    	long start = System.currentTimeMillis();
    	DenseVector b = ((BeliefStateDense)bel).getPoint();
    	DenseVector dotProds = new DenseVector(v.numRows());
    	dotProds = (DenseVector) v.mult(b, dotProds);
    	// there must be a way to avoid this!!
    	int argmax = Utils.argmax(Matrices.getArray(dotProds)); 
    	//Matrix argmax = dotProds.indexOfMax(Ret.NEW, 0);
    	// save the index of the alpha that supports this belief point
    	bel.setAlpha(argmax);
    	double max = dotProds.norm(Vector.Norm.Infinity);
    	System.out.println("elapsed in V: " + (System.currentTimeMillis() - start));
    	return max;
     }
    	//DenseVector b = (DenseVector) bel.getPoint();
    	//double dotProds[] = LinearAlgebra.times(v, b);
    	//int argmax = Utils.argmax(dotProds);
    	// save the index of the alpha that supports this belief point
    	//bel.setAlpha(argmax);
    	//double max = dotProds[argmax];
    	//return max;
    //}

    // return flat value function
    public double[][] getvFlat() {
    	return Matrices.getArray(v);
    }    

    // direct contol using this value function as a policy
    // actions start from 0
 //   public int directControl(BeliefState bel) {
 //       double b[] = Matrices.getArray((DenseVector) bel.getPoint());
 //   	double dotProds[] = LinearAlgebra.times(v, b);
 //   	int argmax = Utils.argmax(dotProds);
 //   	return a[argmax];
 //   }

} // valueFunctionFlat
