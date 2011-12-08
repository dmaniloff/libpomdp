package libpomdp.solve.offline.vi;

import libpomdp.common.std.PomdpStd;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.offline.IterationStats;

public abstract class ValueIterationStd extends ValueIteration {

	protected PomdpStd pomdp;
	protected ValueFunctionStd current;
	protected ValueFunctionStd old;

	protected void initValueIteration(PomdpStd pomdp)
	{
		this.pomdp=pomdp;
		initIteration();
		iterationStats=new ValueIterationStats(pomdp);
	}

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

	public void registerValueIterationStats(){
		if (current!=null){
			((ValueIterationStats)iterationStats).iteration_vector_count.add(new Integer(current.size()));
		}
	registerIterationTime();
	}
}
