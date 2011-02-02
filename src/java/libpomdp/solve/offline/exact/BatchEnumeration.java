/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CustomMatrix.java
 * Description: Wrapper for sparse vector implementation.
 * Copyright (c) 2010, 2011 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.solve.offline.exact;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefMdp;
import libpomdp.common.Pomdp;
import libpomdp.common.ValueFunctionFactory;
import libpomdp.solve.IterationStats;
import libpomdp.solve.offline.ValueIterationStats;
import libpomdp.solve.offline.ValueIteration;

public class BatchEnumeration extends ValueIteration {

    BeliefMdp bmdp;
    double delta;

    public BatchEnumeration(Pomdp pomdp, double delta) {
	startTimer();
	initValueIteration(pomdp);
	this.delta = delta;
	bmdp = pomdp.getBeliefMdp();
	current = ValueFunctionFactory.getEmpty(pomdp);
	current.push(bmdp.getEmptyAlpha());
	registerInitTime();
    }

    @Override
    public IterationStats iterate() {
	startTimer();
	old = current;
	current = ValueFunctionFactory.getEmpty(pomdp);
	for (int a = 0; a < bmdp.nrActions(); a++) {
	    for (int idx = 0; idx < old.size(); idx++) {
		AlphaVector prev = old.getAlpha(idx);
		AlphaVector alpha = bmdp.getEmptyAlpha();
		for (int o = 0; o < bmdp.nrObservations(); o++) {
		    alpha.add(bmdp.project(prev, a, o));
		}
		current.push(alpha);
	    }
	    current.crossSum(ValueFunctionFactory.getRewardValueFunction(pomdp,a));
	}
	((ValueIterationStats) iterationStats).registerLp(current.prune(delta));
	registerValueIterationStats();
	return iterationStats;
    }

}
