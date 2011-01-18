/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: QmdpPolicyStd.java
 * Description: 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.solve.offline.heuristic;

// imports

import libpomdp.common.AlphaVector;
import libpomdp.common.std.PomdpStd;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.IterationStats;
import libpomdp.solve.offline.ValueIterationStd;

public class QmdpPolicyStd extends ValueIterationStd {

    public AlphaVector Vt;

    public QmdpPolicyStd(PomdpStd pomdp) {
	startTimer();
	initValueIteration(pomdp);
	current = new ValueFunctionStd(pomdp.nrStates());
	for (int a = 0; a < pomdp.nrActions(); a++)
	    current.push(new AlphaVector(pomdp.nrStates(), a));
	Vt = new AlphaVector(pomdp.nrStates());
	registerInitTime();
    }

    @Override
    public IterationStats iterate() {
	startTimer();
	old = current.copy();
	current = new ValueFunctionStd(pomdp.nrStates());
	for (int a = 0; a < pomdp.nrActions(); a++) {
	    AlphaVector res = pomdp.mdpValueUpdate(Vt, a);
	    current.push(res);
	}
	for (int s = 0; s < pomdp.nrStates(); s++) {
	    double colmax = Double.NEGATIVE_INFINITY;
	    for (int a = 0; a < pomdp.nrActions(); a++) {
		double val = current.getAlphaElement(a, s);
		if (val > colmax)
		    colmax = val;
	    }
	    Vt.setValue(s, colmax);
	}
	registerValueIterationStats();
	return iterationStats;
    }
} // qmdpFlat