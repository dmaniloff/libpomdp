package libpomdp.solve.java.vi;

import libpomdp.common.ValueFunction;
import libpomdp.solve.Iteration;
import libpomdp.solve.IterationStats;

public abstract class ValueIteration extends Iteration {
	
	public IterationStats getStats() {
		return(iterationStats);
	}
	
	public abstract void registerValueIterationStats();
		
	public abstract ValueFunction getValueFunction();
	public abstract ValueFunction getOldValueFunction();
}
