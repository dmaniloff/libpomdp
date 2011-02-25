package libpomdp.solve.offline.brl;

import libpomdp.common.brl.BrlBelief;
import libpomdp.common.brl.BrlReward;
import libpomdp.common.brl.SpecificBrlTree;
import libpomdp.solve.IterationStats;
import libpomdp.solve.MaxIterationsCriteria;
import libpomdp.solve.offline.OfflineIteration;

public class BrlDP extends OfflineIteration {

	private BrlBelief prior;
	protected SpecificBrlTree tree;
	protected int states;
	protected int actions;
	protected int horizon;
	protected int init_state;
	private double gamma;
	private BrlReward reward;


	public BrlDP(BrlBelief prior, BrlReward reward, int horizon,
			int init_state, double gamma, double fac) {
		startTimer();
		BrlBelief.checkSpace();
		initIteration();
		this.prior = prior;
		this.gamma = gamma;
		this.reward = reward;
		this.states = BrlBelief.states();
		this.actions = BrlBelief.actions();
		this.horizon = horizon;
		this.init_state = init_state;
		//this.saved=new int[horizon];
		addStopCriteria(new MaxIterationsCriteria(horizon));
		//System.out.println(fac);
		tree=new SpecificBrlTree(horizon,init_state,fac);
		registerInitTime();
	}
	
	


	@Override
	public IterationStats iterate() {
		startTimer();
		tree.eval(horizon - iterationStats.iterations -1, gamma, reward, prior);
		this.registerIterationTime();
		return iterationStats;
	}


	public SpecificBrlTree getTree() {
		return tree;
	}

}
