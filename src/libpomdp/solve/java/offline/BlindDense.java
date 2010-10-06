/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: blindFlat.java
 * Description: blind policy value function approximation with a flat
 *              representation
 *              do not try on large problems as this will run out of mem
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.solve.java.offline;

// imports
import libpomdp.common.java.dense.PomdpDense;
import libpomdp.common.java.dense.ValueFunctionDense;

import no.uib.cipr.matrix.Matrices;

import org.math.array.DoubleArray;
import org.math.array.IntegerArray;
import org.math.array.LinearAlgebra;

public class BlindDense {
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // parameters
    final int    MAX_ITER = 500;
    final double EPSILON  = 1e-4;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    // the computation of the blind policy is
    // done here because it seems that the convergence
    // checks should be different
	private ValueFunctionDense getBlindFlat(PomdpDense problem) {
	
	double oldBlind[][];
	double gTaA[];

	// delta now is a matrix
	double delta[][], dists[];
	double conv;
	
	// Blind is |A| x |S| - initialize each \alpha^a_{0} to \min_s {R(s,a)/(1-\gamma)}
	double Blindv[][] = new double[problem.nrActions()][problem.nrStates()];
	int Blinda[]      = IntegerArray.fill(problem.nrActions(), -1);

	for(int a=0; a<problem.nrActions(); a++) {
	    Blindv[a] = 
		DoubleArray.fill(problem.nrStates(), 
				 DoubleArray.min(Matrices.getArray(problem.getRewardValues(a)))/(1.0-problem.getGamma()));
	}

		      
	for(int iter=0; iter<MAX_ITER; iter++) {
	    // copy old values
	    oldBlind = DoubleArray.copy(Blindv);
	    for(int a=0; a<problem.nrActions(); a++) {

		// Blind:
		// \alpha_a = R(s,a) + \gamma \sum_{s'} {T(s,a,s') \alpha^a_{t-1}(s')
		gTaA = LinearAlgebra.times(LinearAlgebra.times(Matrices.getArray(problem.getTransitionProbs(a)), Blindv[a]),
					   problem.getGamma());
		Blindv[a]  = LinearAlgebra.plus(Matrices.getArray(problem.getRewardValues(a)), gTaA);
		Blinda[a]  = a;
	    }
	    
	    // convergence check
	    delta = LinearAlgebra.minus(Blindv, oldBlind);
	    delta = LinearAlgebra.raise(delta, 2.0);
	    dists = DoubleArray.sum(DoubleArray.transpose(delta));
	    //System.out.println(DoubleArray.toString(delta));
	    conv  = DoubleArray.max(dists);
	    System.out.println("Max euclid dist at iteration " + iter + " is: " + conv);
	    if (conv <= EPSILON)
		break;
	}

	// build value functions
	return new ValueFunctionDense(Blindv, Blinda);

    } // getBlindFlat

} // blindFlat