package libpomdp.common.std;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.RhoFunction;
import libpomdp.common.ValueFunction;
import libpomdp.solve.offline.pointbased.PointSet;

public class LinearRhoStd implements RhoFunction {

	protected int states;
	protected int actions;
	protected double rewardMax[];
	protected double rewardMin[];
	protected double totalMax;
	protected double totalMin;
	private AlphaVectorStd func[];

	
	public LinearRhoStd(CustomVector[] r) {
		actions=r.length;
		states=r[0].size();
		func = new AlphaVectorStd[actions];
		rewardMax=new double[actions];
		rewardMin=new double[actions];
		
		for(int a=0;a<actions;a++){
			getFunc()[a]=AlphaVectorStd.transform(r[0]);
			getFunc()[a].setAction(a);
		}
		totalMin = Double.POSITIVE_INFINITY;
		totalMax = Double.NEGATIVE_INFINITY;
		for (int a = 0; a < actions; a++) {
		    rewardMin[a]=Double.POSITIVE_INFINITY;;
		    rewardMax[a]=Double.NEGATIVE_INFINITY;;
		    for (int s=0;s<states;s++){
		    	if (getFunc()[a].get(s) > rewardMax[a])
		    		rewardMax[a]=getFunc()[a].get(s);
		    	if (getFunc()[a].get(s) < rewardMin[a])
		    		rewardMin[a]=getFunc()[a].get(s);
		    }
		    if (rewardMax[a] > totalMax){
		    	totalMax=rewardMax[a];
		    }
		    if (rewardMin[a] > totalMin){
		    	totalMin=rewardMin[a];
		    }
		}
	}
		
	public AlphaVectorStd getVector(int a) {
		return getFunc()[a].copy();
	}

	public double sample(BeliefState b, int a) {
		getFunc()[a].dot((BeliefStateStd)b);
		return 0;
	}

	public double min(int a) {
		return rewardMin[a];
	}

	public double max(int a) {
		return rewardMax[a];
	}
	
	public double min() {
		return totalMin;
	}

	public double max() {
		return totalMax;
	}

	public int size() {
		return actions;
	}

	public ValueFunctionStd getValueFunction(int a) {
		ValueFunctionStd vf = new ValueFunctionStd();
		vf.push(getFunc()[a].copy());
		return vf;
	}

	public double get(int s, int a) {
		return getFunc()[a].get(s);
	}

	
	public ValueFunction approximate(int a, PointSet bset) {
		return getValueFunction(a);
	}

	public AlphaVectorStd[] getFunc() {
		return func;
	}

}
