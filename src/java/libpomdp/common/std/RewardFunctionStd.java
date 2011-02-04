package libpomdp.common.std;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.RewardFunction;

public class RewardFunctionStd extends RewardFunction {

	protected int states;
	protected int actions;
	protected double rewardMax[];
	protected double rewardMin[];
	protected double totalMax;
	protected double totalMin;
	protected AlphaVectorStd func[];

	
	public RewardFunctionStd(CustomVector[] r) {
		actions=r.length;
		states=r[0].size();
		func=new AlphaVectorStd[actions];
		rewardMax=new double[actions];
		rewardMin=new double[actions];
		
		for(int a=0;a<actions;a++){
			func[a]=AlphaVectorStd.transform(r[0]);
			func[a].setAction(a);
		}
		totalMin = Double.POSITIVE_INFINITY;
		totalMax = Double.NEGATIVE_INFINITY;
		for (int a = 0; a < actions; a++) {
		    rewardMin[a]=Double.POSITIVE_INFINITY;;
		    rewardMax[a]=Double.NEGATIVE_INFINITY;;
		    for (int s=0;s<states;s++){
		    	if (func[a].get(s) > rewardMax[a])
		    		rewardMax[a]=func[a].get(s);
		    	if (func[a].get(s) < rewardMin[a])
		    		rewardMin[a]=func[a].get(s);
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
		return func[a].copy();
	}

	public double sample(BeliefState b, int a) {
		func[a].dot((BeliefStateStd)b);
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

	@Override
	public int size() {
		return actions;
	}

	public ValueFunctionStd getValueFunction(int a) {
		ValueFunctionStd vf = new ValueFunctionStd();
		vf.push(func[a].copy());
		return vf;
	}
	
	


}
