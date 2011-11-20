package libpomdp.common.rho;

import libpomdp.common.BeliefState;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.offline.pointbased.PointSet;

public abstract class RewardFunction {

	abstract public double sample(BeliefState b, int a);
	abstract public ValueFunctionStd approximate(int a,PointSet bset);
	abstract public double max(int a);
	abstract public double min(int a);

}
