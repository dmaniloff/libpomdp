/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: 
 * Description: value function representation using UJMP
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

// imports
import org.math.array.*;
import org.ujmp.core.*;
import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.doublematrix.impl.*;
import java.io.*;

public class ValueFunctionSparseUJMP implements ValueFunction, Serializable {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // serial id
    static final long serialVersionUID = 3L;

    // represent a value function via a Matrix object
    private DefaultSparseDoubleMatrix v;

    // actions associated to each alpha vector
    private int a[];

    // constructor
    public ValueFunctionSparseUJMP(DefaultSparseDoubleMatrix v, int a[]) {
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
	DefaultSparseDoubleMatrix b = ((BeliefStateSparseUJMP)bel).bSparse;
	DefaultSparseDoubleMatrix dotProdsM  = (DefaultSparseDoubleMatrix) v.mtimes(b);
	// need to convert for now to use Util.argmax
	// there must be a way to avoid this!!
	double dotProds[] =  dotProdsM.toDoubleArray()[0];
	int argmax        = Util.argmax(dotProds);	
	// save the index of the alpha that supports this belief point
	bel.setplanid(argmax);
	double max = dotProds[argmax];
	return max;
    }

    // return flat value function
    // should not need to call this
    public double[][] getvFlat() {
	return v.toDoubleArray();
    }    

} // 
