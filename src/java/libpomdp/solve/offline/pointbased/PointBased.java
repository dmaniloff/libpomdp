package libpomdp.solve.offline.pointbased;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefMdp;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.Pomdp;
import libpomdp.common.ValueFunction;
import libpomdp.common.ValueFunctionFactory;
import libpomdp.common.std.BeliefStateStd;
import libpomdp.common.std.RhoPomdp;
import libpomdp.solve.IterationStats;
import libpomdp.solve.offline.ValueIteration;

public class PointBased extends ValueIteration {

    BeliefMdp bmdp;
    PointSet fullBset;
    PointSet newBset;
    PbParams params;

    public AlphaVector getLowestAlpha() {
	double best_val = bmdp.getRewardMaxMin();
	best_val = best_val / (1 - bmdp.getGamma());
	return(bmdp.getHomogeneAlpha(best_val));
    }


    public PointBased(Pomdp pomdp, PbParams params) {
	startTimer();
	initValueIteration(pomdp);
	this.params = params;
	bmdp = pomdp.getBeliefMdp();
	current = ValueFunctionFactory.getEmpty(pomdp);
	current.push(getLowestAlpha());
	registerInitTime();
    }

    public IterationStats iterate() {
	startTimer();
	old = current;
	expand();
	if (bmdp instanceof RhoPomdp) {
	    ((RhoPomdp) bmdp).approxReward(fullBset);
	}
	// System.out.println("size(B)="+fullBset.size());

	for (int i = 0; i < params.backupHorizon; i++) {
	    backup();
	}
	current.prune();
	if (params.isNewPointsOnly())
	    fullBset = newBset;
	// System.out.println(current);
	registerValueIterationStats();
	return iterationStats;
    }

    protected void backup() {
	switch (params.getBackupMethod()) {
	case PbParams.BACKUP_SYNC_FULL:
	    current = syncBackup(fullBset);
	    break;
	case PbParams.BACKUP_SYNC_NEWPOINTS:
	    current = syncBackup(newBset);
	    break;
	case PbParams.BACKUP_ASYNC_FULL:
	    current = asyncBackup(fullBset);
	    break;
	case PbParams.BACKUP_ASYNC_NEWPOINTS:
	    current = asyncBackup(fullBset);
	    break;
	}
    }

    private AlphaVector backup(BeliefState bel, ValueFunction vf) {
	AlphaVector alpha_max = null;
	double alpha_max_val = Double.NEGATIVE_INFINITY;
	for (int a = 0; a < bmdp.nrActions(); a++) {
	    AlphaVector alpha_sum = bmdp.getEmptyAlpha(a);
	    for (int o = 0; o < bmdp.nrObservations(); o++) {
		double max_val = Double.NEGATIVE_INFINITY;
		AlphaVector max_vect = null;
		for (int idx = 0; idx < vf.size(); idx++) {
		    AlphaVector prev = vf.getAlpha(idx);
		    AlphaVector vect = bmdp.project(prev, a, o);
		    double val = vect.eval(bel);
		    if (val > max_val) {
			max_val = val;
			max_vect = vect;
		    }
		}
		alpha_sum.add(max_vect);
	    }
	    AlphaVector re = bmdp.getRewardValueFunction(a).getBestAlpha(bel);
	    alpha_sum.add(re);
	    double alpha_val = alpha_sum.eval(bel);
	    if (alpha_val > alpha_max_val) {
		alpha_max_val = alpha_val;
		alpha_max = alpha_sum;
	    }
	}
	return (alpha_max);
    }

    private ValueFunction asyncBackup(PointSet bset) {
	ValueFunction newv = ValueFunctionFactory.getEmpty(bmdp);
	PointSet testBset = bset.copy();
	while (testBset.size() != 0) {
	    BeliefState bel = testBset.getRandom();
	    bset.remove(bel);
	    AlphaVector alpha = backup(bel, old);
	    if (alpha.eval(bel) >= old.value(bel))
		newv.push(alpha);
	    else
		newv.push(old.getBestAlpha(bel));
	    PointSet tabu = new PointSet();
	    for (BeliefState beltest : testBset) {
		if (newv.value(beltest) >= old.value(beltest)) {
		    tabu.add(beltest);
		}
	    }
	    for (BeliefState beltest : tabu)
		testBset.remove(beltest);
	}
	return newv;
    }

    protected ValueFunction syncBackup(PointSet bset) {
	ValueFunction newv = ValueFunctionFactory.getEmpty(bmdp);
	for (BeliefState bel : bset) {
	    newv.push(backup(bel, old));
	}
	return newv;
    }

    protected void expand() {
	newBset = new PointSet();
	if (fullBset == null) {
	    fullBset = new PointSet();
	    fullBset.add(bmdp.getInitialBeliefState());
	    newBset.add(bmdp.getInitialBeliefState());
	}
	if (fullBset.size() >= params.getMaxTotalPoints())
	    return;

	PointSet testBset = fullBset.copy();
	while (fullBset.size() < params.getMaxTotalPoints()
		|| newBset.size() < params.getMaxNewPoints()) {
	    BeliefState point = null;
	    switch (params.getExpandMethod()) {
	    case PbParams.EXPAND_GREEDY_ERROR_REDUCTION:
		point = collectGreedyErrorReduction(testBset);
		break;
	    case PbParams.EXPAND_EXPLORATORY_ACTION:
		point = collectExploratoryAction(testBset);
		break;
	    case PbParams.EXPAND_RANDOM_EXPLORE_STATIC:
	    case PbParams.EXPAND_RANDOM_EXPLORE_DYNAMIC:
		point = collectRandomExplore(testBset, bmdp);
		break;
	    }
	    if (point != null) {
		fullBset.add(point);
		newBset.add(point.copy());
	    }
	    if (testBset.size() == 0) {
		if (params.getExpandMethod() == PbParams.EXPAND_RANDOM_EXPLORE_STATIC)
		    testBset = fullBset.copy();
		else
		    break;
	    }
	}
    }

    private BeliefState collectExploratoryAction(PointSet testBset) {
    	BeliefState b = (BeliefState) testBset.remove(0);
	double max_dist = Double.NEGATIVE_INFINITY;
	BeliefState bnew = null;
	for (int a = 0; a < bmdp.nrActions(); a++) {
	    int o = bmdp.sampleObservation(b, a);
	    BeliefState ba = (BeliefState) bmdp.nextBeliefState(b, a, o);
	    double dist = distance(ba, fullBset);
	    if (dist > max_dist) {
		max_dist = dist;
		bnew = ba;
	    }
	}
	if (max_dist == 0.0)
	    bnew = null;
	return (bnew);
    }

    private BeliefState collectGreedyErrorReduction(PointSet testBset) {
	double max_val = Double.NEGATIVE_INFINITY;
	BeliefState bprime = null;
	int aprime = -1;
	int oprime = -1;
	for (BeliefState b : testBset) {
	    for (int a = 0; a < bmdp.nrActions(); a++) {
		double sum_err = 0;
		for (int o = 0; o < bmdp.nrObservations(); o++) {
		    double err = (bmdp.getTau(a, o).mult(b.getPoint()))
			    .norm(1.0);
		    err *= minError(bmdp.nextBeliefState(b, a, o), testBset);
		    sum_err += err;
		}

		if (sum_err > max_val) {
		    max_val = sum_err;
		    bprime = b;
		    aprime = a;
		}
	    }
	}
	max_val = Double.NEGATIVE_INFINITY;
	for (int o = 0; o < bmdp.nrObservations(); o++) {
	    double err = (bmdp.getTau(aprime, o).mult(bprime.getPoint()))
		    .norm(1.0);
	    err *= minError(bmdp.nextBeliefState(bprime, aprime, o), testBset);
	    if (err > max_val) {
		max_val = err;
		oprime = o;
	    }
	}
	testBset.remove(bprime);
	return (BeliefState) (bmdp.nextBeliefState(bprime, aprime, oprime));
    }

    private double minError(BeliefState beliefState, PointSet bset) {
	double rmax = bmdp.getRewardMax() / (1.0 - bmdp.getGamma());
	double rmin = bmdp.getRewardMin() / (1.0 - bmdp.getGamma());
	double min_val = Double.POSITIVE_INFINITY;
	for (BeliefState b : bset) {
	    double sum = 0;
	    AlphaVector vect = current.getBestAlpha(b);
	    for (int s = 0; s < bmdp.nrStates(); s++) {
		double bdiff = beliefState.getPoint().get(s)
			- b.getPoint().get(s);
		if (bdiff >= 0)
		    sum += (rmax - vect.get(s)) * bdiff;
		else
		    sum += (rmin - vect.get(s)) * bdiff;
	    }
	    if (sum < min_val)
		min_val = sum;
	}
	return (min_val);
    }

    public static BeliefState collectRandomExplore(PointSet testBset,
	    Pomdp bmdp) {
	BeliefStateStd b = (BeliefStateStd) testBset.remove(0);
	BeliefStateStd bprime;
	int a = bmdp.getRandomAction();
	int o = bmdp.sampleObservation(b, a);
	bprime = (BeliefStateStd) bmdp.nextBeliefState(b, a, o);
	return bprime;
    }

    private double distance(BeliefState ba, PointSet newBset) {
	double min_val = Double.POSITIVE_INFINITY;
	for (BeliefState bprime : newBset) {
	    CustomVector vect = bprime.getPoint().copy();
	    vect.add(-1.0, ba.getPoint());
	    double val = vect.norm(1.0);
	    if (val < min_val)
		min_val = val;
	}
	return min_val;
    }

}
