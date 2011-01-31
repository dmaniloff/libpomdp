package libpomdp.solve.offline.exact;

import java.util.ArrayList;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefMdp;
import libpomdp.common.Pomdp;
import libpomdp.common.ValueFunction;
import libpomdp.common.ValueFunctionFactory;
import libpomdp.solve.IterationStats;
import libpomdp.solve.offline.ValueIterationStats;
import libpomdp.solve.offline.ValueIteration;

public class IncrementalPruning extends ValueIteration {

    BeliefMdp bmdp;
    private double delta;

    public IncrementalPruning(Pomdp pomdp, double delta) {
	startTimer();
	initValueIteration(pomdp);
	this.delta = delta;
	bmdp = pomdp.getBeliefMdp();
	current = ValueFunctionFactory.getEmpty(bmdp);
	current.push(pomdp.getEmptyAlpha());
	registerInitTime();
    }

    public IterationStats iterate() {
	startTimer();
	old = current;
	ValueIterationStats iterationStats = (ValueIterationStats) this.iterationStats;
	current = ValueFunctionFactory.getEmpty(bmdp);
	for (int a = 0; a < bmdp.nrActions(); a++) {
	    // Perform Projections
	    ArrayList<ValueFunction> psi = new ArrayList<ValueFunction>();
	    for (int o = 0; o < bmdp.nrObservations(); o++) {
		ValueFunction proj = ValueFunctionFactory.getEmpty(bmdp);
		for (int idx = 0; idx < old.size(); idx++) {
		    AlphaVector alpha = old.getAlpha(idx);
		    AlphaVector res = bmdp.projection(alpha, a, o);
		    proj.push(res);
		}
		iterationStats.registerLp(proj.prune(delta));
		psi.add(proj);
	    }
	    ValueFunction rewFunc = bmdp.getRewardValueFunction(a);
	    // rewFunc.scale(1.0/(double)bmdp.nrObservations());
	    psi.add(rewFunc);
	    // Now Cross sum...
	    while (psi.size() > 1) {
		ValueFunction vfA = psi.remove(0);
		ValueFunction vfB = psi.remove(0);
		vfA.crossSum(vfB);
		iterationStats.registerLp(vfA.prune(delta));
		psi.add(vfA);
	    }
	    ValueFunction vfA = psi.remove(0);
	    current.merge(vfA);
	}
	iterationStats.registerLp(current.prune(delta));
	System.out.println(current.size());
	registerValueIterationStats();
	return iterationStats;
    }

}
