package libpomdp.solve.offline.exact;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefMdp;
import libpomdp.common.Pomdp;
import libpomdp.common.ValueFunctionFactory;
import libpomdp.solve.IterationStats;
import libpomdp.solve.offline.ValueIteration;

public class OnePass extends ValueIteration {

    BeliefMdp bmdp;

    public OnePass(Pomdp pomdp) {
	startTimer();
	initValueIteration(pomdp);
	bmdp = pomdp.getBeliefMdp();
	current = ValueFunctionFactory.getEmpty(pomdp);
	current.push(pomdp.getEmptyAlpha());
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
	    current.crossSum(bmdp.getRewardValueFunction(a));
	}
	registerValueIterationStats();
	return iterationStats;
    }

}
