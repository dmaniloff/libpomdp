/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: BlindPolicy.java
 * Description: 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.solve.offline.heuristic;

import libpomdp.common.AlphaVector;
import libpomdp.common.Pomdp;
import libpomdp.common.ValueFunctionFactory;
import libpomdp.solve.IterationStats;
import libpomdp.solve.offline.ValueIteration;

public class BlindPolicy extends ValueIteration {

    public BlindPolicy(Pomdp pomdp) {
	startTimer();
	initValueIteration(pomdp);	
	// Blind is |A| x |S| - initialize each \alpha^a_{0} to \min_s
	// {R(s,a)/(1-\gamma)}
	current = ValueFunctionFactory.getLowerBoundPerAction(pomdp);
	registerInitTime();
    }

    @Override
    public IterationStats iterate() {
	startTimer();
	old = current.copy();
	for (int a = 0; a < pomdp.nrActions(); a++) {
	    AlphaVector vec = current.getAlpha(a);
	    AlphaVector res = pomdp.mdpValueUpdate(vec, a);
	    vec.set(res);
	}
	registerValueIterationStats();
	return iterationStats;
    }

}