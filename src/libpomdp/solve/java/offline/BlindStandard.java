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
import libpomdp.common.java.CustomVector;
import libpomdp.common.java.Utils;
import libpomdp.common.java.standard.PomdpStandard;
import libpomdp.common.java.standard.ValueFunctionStandard;


public class BlindStandard {
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
	public ValueFunctionStandard getBlindFlat(PomdpStandard problem) {
	// delta now is a matrix
	double conv;
	
	// Blind is |A| x |S| - initialize each \alpha^a_{0} to \min_s {R(s,a)/(1-\gamma)}
	ValueFunctionStandard iniv;
	ValueFunctionStandard oldv;
	ValueFunctionStandard newv;
	iniv= new ValueFunctionStandard(problem.nrStates());
	
	for(int a=0; a<problem.nrActions(); a++) {
		CustomVector vr=problem.getRewardValues(a).copy();
		double factor=1.0/(1.0-problem.getGamma());
		vr.scale(factor);
		double varr[]=vr.getArray();
		int idx=Utils.argmin(varr);
	    iniv.push(CustomVector.getHomogene(problem.nrStates(),vr.get(idx)),a);
	}

	oldv=iniv.copy();
	newv=iniv.copy();
	for(int iter=0; iter<MAX_ITER; iter++) {
		conv=Double.NEGATIVE_INFINITY;
	    for(int a=0; a<problem.nrActions(); a++) {
	    	CustomVector vec=newv.getVector(a);
	    	vec=problem.getTransitionProbs(a).mult(problem.getGamma(),vec);
	    	vec.add(problem.getRewardValues(a));
	    	CustomVector perf=oldv.getVector(a).copy();
	    	perf.add(-1.0,vec);
	    	double a_value = perf.norm(2.0);
	    	if (a_value > conv)
	    		conv=a_value;
	    } 
	    System.out.println("Max euclid dist at iteration " + iter + " is: " + conv);
	    if (conv <= EPSILON)
		break;
	    oldv= newv.copy();
	}

	// build value functions
	return newv;

    } // getBlindFlat

} // blindFlat