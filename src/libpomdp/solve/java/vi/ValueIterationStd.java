package libpomdp.solve.java.vi;

import libpomdp.common.java.std.PomdpStd;
import libpomdp.common.java.std.ValueFunctionStd;
import libpomdp.solve.java.IterationStats;

public abstract class ValueIterationStd extends ValueIteration {
	
	protected PomdpStd pomdp;
	protected ValueFunctionStd current;
	protected ValueFunctionStd old;
	
	public PomdpStd getPomdp() {
		return pomdp;
	}

	public ValueFunctionStd getValueFunction() {
		return current;
	}

	public ValueFunctionStd getOldValueFunction() {
		return old;
	}
	
	public abstract IterationStats iterate();

}
