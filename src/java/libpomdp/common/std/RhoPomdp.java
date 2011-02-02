/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: 
 * Description: Represent a POMDP model using a flat representation and
 *              sparse matrices and vectors. This class can be constructed
 *              from a pomdpSpecSparseMTJ object after parsing a .POMDP file.
 *              Sparse matriced by matrix-toolkits-java, 
 *              every matrix will be CustomMatrix:
 *              
 * S =
 *  (3,1)        1
 *  (2,2)        2
 *  (3,2)        3
 *  (4,3)        4
 *  (1,4)        5
 * A =
 *   0     0     0     5
 *   0     2     0     0
 *   1     3     0     0
 *   0     0     4     0
 * Copyright (c) 2009, 2010 Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.std;

// imports
import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefState;
import libpomdp.common.RhoFunction;
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

    public double getRewardMax(int a) {
	return (reward.max(a));
    }

    public double getRewardMin(int a) {
	return (reward.min(a));
    }

    public AlphaVector mdpValueUpdate(AlphaVector alpha, int a) {
	// TODO
	return alpha;
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
	    System.out
		    .println("ERROR: you have to approximate the reward first (approxReward)");
	    System.exit(1);
	}
	return (rewardCache[a]);
    }

}
