package libpomdp.simulation;

import libpomdp.common.RewardFunction;
import libpomdp.common.TransitionModel;

public class SimMDP {
	protected int states;
	protected int actions;
	protected TransitionModel model;

	public SimMDP(int states,int actions,TransitionModel model,RewardFunction func){
		this.states=states;
		this.actions=actions;
		this.model=model;
	}
	
	public double simulate(int iniState,Agent actor,long horizon){
		
		return 0.0;
	}
	
}
