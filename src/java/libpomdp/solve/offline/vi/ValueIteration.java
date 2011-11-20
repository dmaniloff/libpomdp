package libpomdp.solve.offline.vi;

import libpomdp.common.ValueFunction;
import libpomdp.solve.offline.Iteration;
import libpomdp.solve.offline.IterationStats;

public abstract class ValueIteration extends Iteration {
	
	public IterationStats getStats() {
		return(iterationStats);
	}
	
	public abstract void registerValueIterationStats();
		
	public abstract ValueFunction getValueFunction();
	public abstract ValueFunction getOldValueFunction();
}
