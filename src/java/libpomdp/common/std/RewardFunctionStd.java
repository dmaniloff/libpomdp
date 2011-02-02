package libpomdp.common.std;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.RewardFunction;

public class RewardFunctionStd extends RewardFunction {

	protected int states;
	protected int actions;
	protected CustomVector func[];

	
	public RewardFunctionStd(CustomVector[] r) {
		// TODO Auto-generated constructor stub
	}
	
	public CustomVector getVector(int a) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getExpectation(BeliefState b, int a) {
		func[a].dot(b.getPoint());
		return 0;
	}

	public double min(int a) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double max(int a) {
		// TODO Auto-generated method stub
		return 0;
	}


}
