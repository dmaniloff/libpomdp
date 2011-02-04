package libpomdp.common.add;

import java.util.Vector;

import libpomdp.common.BeliefState;
import libpomdp.common.RewardFunction;
import libpomdp.common.ValueFunction;
import libpomdp.common.add.symbolic.DD;
import libpomdp.common.add.symbolic.OP;

public class RewardFunctionAdd extends RewardFunction {

	protected AddConfiguration conf;
	protected DD[] model;

	public RewardFunctionAdd(DD reward, Vector<DD> actCosts,
			AddConfiguration conf) {
		this.conf=conf;
		model=new DD[conf.nrAct];
		for (int a = 0; a < conf.nrAct; a++) {
			model[a] = OP.sub(reward, actCosts.get(a));
		}
	}

	@Override
	public double sample(BeliefState b, int a) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double max(int a) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double min(int a) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ValueFunction getValueFunction(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double max() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double min() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
