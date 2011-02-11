package libpomdp.common.add;

import java.util.Vector;

import libpomdp.common.BeliefState;
import libpomdp.common.RhoFunction;
import libpomdp.common.ValueFunction;
import libpomdp.common.add.symbolic.DD;
import libpomdp.common.add.symbolic.OP;
import libpomdp.solve.offline.pointbased.PointSet;

public class LinearRhoAdd implements RhoFunction {

	protected AddConfiguration conf;
	protected DD[] model;

	public LinearRhoAdd(DD reward, Vector<DD> actCosts,
			AddConfiguration conf) {
		this.conf=conf;
		model=new DD[conf.nrAct];
		for (int a = 0; a < conf.nrAct; a++) {
			model[a] = OP.sub(reward, actCosts.get(a));
		}
	}

	public double max(int a) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public double min(int a) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public double max() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public double min() {
		// TODO Auto-generated method stub
		return 0;
	}


	public ValueFunction getValueFunction(int bestA) {
		// TODO Auto-generated method stub
		return null;
	}


	
	public ValueFunction approximate(int a, PointSet bset) {
		// TODO Auto-generated method stub
		return null;
	}


	
	public double sample(BeliefState b, int a) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public double get(int state, int action) {
		// TODO Auto-generated method stub
		return 0;
	}

}
