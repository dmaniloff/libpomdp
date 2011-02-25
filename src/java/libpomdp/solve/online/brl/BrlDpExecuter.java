package libpomdp.solve.online.brl;

import libpomdp.common.brl.BrlBelief;
import libpomdp.common.brl.BrlReward;
import libpomdp.common.brl.BrlTreeNode;
import libpomdp.common.brl.SpecificBrlTree;
import libpomdp.solve.offline.brl.BrlDP;
import libpomdp.solve.online.OnlineIteration;

public class BrlDpExecuter extends OnlineIteration {

	protected SpecificBrlTree policy;
	protected int states;
	protected int actions;
	protected int horizon;
	private int current_action;
	private BrlTreeNode current_belief;
	private int current_state;


	public BrlDpExecuter(BrlReward func, int horizon, double gamma, double fac,BrlBelief prior, int init_state) {
		startTimer();
		BrlBelief.checkSpace();
		initIteration();
		this.states = BrlBelief.states();
		this.actions = BrlBelief.actions();
		this.horizon = horizon;
		BrlDP base = new BrlDP(prior, func, horizon, init_state, gamma, fac);
		base.run();
		policy=base.getTree();
		current_belief=policy.head();
		current_state=init_state;
		current_action=current_belief.getAction(current_state);
		registerInitTime();
	}
	

	public int iterate(int nstate) {
		startTimer();
		if (iterationStats.iterations!=0){
			current_belief=current_belief.getChild(current_state, current_action,nstate);
		}
		current_state=nstate;
		current_action=current_belief.getAction(current_state);
		
		this.registerIterationTime();
		return current_action;
	}

}
