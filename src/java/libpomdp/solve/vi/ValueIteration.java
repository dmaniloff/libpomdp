package libpomdp.solve.vi;

import libpomdp.common.ValueFunction;
import libpomdp.solve.Iteration;

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
