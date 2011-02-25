package libpomdp.solve.online.brl;

import libpomdp.common.brl.BrlBelief;
import libpomdp.common.brl.BrlReward;
import libpomdp.common.brl.GenericBrlTree;
import libpomdp.solve.MaxIterationsCriteria;
import libpomdp.solve.online.OnlineIteration;

public class Birdp extends OnlineIteration {

	protected GenericBrlTree tree;
	protected int states;
	protected int actions;
	protected int horizon;
	private double gamma;
	private BrlReward reward;
	private int current_action;
	private BrlBelief current_belief;
	private int current_state;


	public Birdp(BrlReward func, int horizon, double gamma, double fac,BrlBelief prior) {
		startTimer();
		BrlBelief.checkSpace();
		initIteration();
		current_belief=prior;
		current_state=-1;
		current_action=-1;
		this.gamma = gamma;
		this.reward = func;
		this.states = BrlBelief.states();
		this.actions = BrlBelief.actions();
		this.horizon = horizon;
		addStopCriteria(new MaxIterationsCriteria(horizon));
		tree=new GenericBrlTree(horizon,fac);
		registerInitTime();
	}
	

	public int iterate(int nstate) {
		startTimer();
		if (iterationStats.iterations!=0){
			current_belief.bayesUpdate(current_state, current_action,nstate);
		}
		current_state=nstate;
		current_action=tree.eval(gamma, reward, current_belief, current_state, horizon-iterationStats.iterations);
		this.registerIterationTime();
		return current_action;
	}
}
