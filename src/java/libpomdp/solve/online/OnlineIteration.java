package libpomdp.solve.online;

import libpomdp.solve.Iteration;

public abstract class OnlineIteration extends Iteration {
		
	public abstract int iterate(int evidence);
	
}
