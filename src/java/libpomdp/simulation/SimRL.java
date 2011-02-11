package libpomdp.simulation;

import libpomdp.common.RewardFunction;
import libpomdp.common.TransitionModel;
import libpomdp.common.Utils;
import libpomdp.solve.online.OnlineIteration;

public class SimRL {
	protected int states;
	protected int actions;
	protected TransitionModel model;
	protected RewardFunction reward;

	public SimRL(int states,int actions,TransitionModel model,RewardFunction reward){
		this.states=states;
		this.actions=actions;
		this.model=model;
		this.reward=reward;
	}
	
	public double simulate(int iniState,OnlineIteration algo,long horizon, long seed, double discount){
		Utils.setSeed(seed);
		int state=iniState;
		int action=-1;
		double total_reward=0.0;
		for (long i=0;i<horizon;i++){
			action=algo.iterate(state);
			state=model.sampleNextState(state,action);
			total_reward+=Math.pow(discount,i)*reward.get(state,action);
		}
		return total_reward;
	}
	
	public double simulate(int iniState,OnlineIteration algo,long horizon, long seed){
		return(simulate(iniState,algo,horizon,seed,1.0));
	}
	
	public double simulate(int iniState,OnlineIteration algo,long horizon){
		return(simulate(iniState,algo,horizon,System.currentTimeMillis(),1.0));
	}
	
}
