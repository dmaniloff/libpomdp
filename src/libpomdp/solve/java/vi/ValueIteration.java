package libpomdp.solve.java.vi;

import libpomdp.common.java.ValueFunction;
import libpomdp.solve.java.Iteration;

public abstract class ValueIteration extends Iteration {
	
	protected ValueFunction current;
	protected ValueFunction old;
	protected ValueIterationStats iterationStats;

	public ValueIterationStats getStats() {
		return(iterationStats);
	}
	
	public abstract ValueFunction getValueFunction();
	public abstract ValueFunction getOldValueFunction();
}
