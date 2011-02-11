package libpomdp.common;

import libpomdp.solve.offline.pointbased.PointSet;

public interface RhoFunction extends RewardFunction {

    abstract public ValueFunction approximate(int a, PointSet bset);
	public abstract ValueFunction getValueFunction(int i);
	public abstract double sample(BeliefState b, int a);
	
}
