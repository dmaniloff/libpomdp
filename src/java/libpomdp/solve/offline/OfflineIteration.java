package libpomdp.solve.offline;

import libpomdp.solve.Iteration;
import libpomdp.solve.IterationStats;

public abstract class OfflineIteration extends Iteration {	
  
    public abstract IterationStats iterate();
    
    public IterationStats run() {
    	while (!finished()) {
    	    System.out.println("== Iteration " + iterationStats.iterations
    		    + " ==");
    	    iterate();
    	}
    	return iterationStats;
        }
	
}
