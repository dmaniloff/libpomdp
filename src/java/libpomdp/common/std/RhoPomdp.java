
package libpomdp.common.std;

import libpomdp.common.BeliefState;
import libpomdp.common.RhoFunction;
import libpomdp.common.Utils;
import libpomdp.solve.offline.pointbased.PointSet;

public class RhoPomdp extends PomdpStd {

    ValueFunctionStd rewardCache[];

    private static final long serialVersionUID = -5511401938934887929L;

    RhoFunction reward;

    // / constructor
    public RhoPomdp(PomdpStd pomdp, RhoFunction reward) {
	super(pomdp);
	this.reward = reward;
    } // constructor

    public double sampleReward(BeliefState b, int a) {
	return (reward.sample(b, a));
    }

    public void approxReward(PointSet bset) {
	rewardCache = new ValueFunctionStd[nrActions()];
	for (int a = 0; a < nrActions(); a++) {
	    rewardCache[a] = (ValueFunctionStd) reward.approximate(a, bset);
	    // System.out.println(rewardCache[a]);
	}
    }

    public ValueFunctionStd getRewardValueFunction(int a) {
	if (rewardCache == null) {
	    Utils.error("ERROR: you have to approximate the reward first (approxReward)");
	}
	return (rewardCache[a]);
    }

}
