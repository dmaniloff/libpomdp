/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: QmdpPolicy.java
 * Description: 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.solve.offline.heuristic;

// imports

import libpomdp.common.AlphaVector;
import libpomdp.common.Pomdp;
import libpomdp.common.ValueFunctionFactory;
import libpomdp.solve.IterationStats;
import libpomdp.solve.offline.ValueIteration;

public class QmdpPolicy extends ValueIteration {

    public AlphaVector Vt;

    public QmdpPolicy(Pomdp pomdp) {
	startTimer();
	initValueIteration(pomdp);
	current = ValueFunctionFactory.getZeroPerActions(pomdp);
	Vt = pomdp.getEmptyAlpha();
	registerInitTime();
    }

    @Override
    public IterationStats iterate() {
	startTimer();
	old = current.copy();
	current = ValueFunctionFactory.getEmpty(pomdp);
	for (int a = 0; a < pomdp.nrActions(); a++) {
	    AlphaVector res = pomdp.mdpValueUpdate(Vt, a);
	    current.push(res);
	}
	Vt=current.getUpperBound();
	
	registerValueIterationStats();
	return iterationStats;
    }
} // qmdpFlat