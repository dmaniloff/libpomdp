package libpomdp.solve.offline.brl;

import java.util.ArrayList;
import java.util.Iterator;

import libpomdp.common.CustomVector;
import libpomdp.common.Utils;
import libpomdp.common.brl.BrlTreeNode;
import libpomdp.common.brl.RlReward;
import libpomdp.common.brl.StateDepRlReward;
import libpomdp.common.brl.TransitionModelBelief;
import libpomdp.solve.IterationStats;
import libpomdp.solve.MaxIterationsCriteria;
import libpomdp.solve.offline.OfflineIteration;

public class BayesDP extends OfflineIteration {

	protected TransitionModelBelief prior;
	protected BrlTreeNode tree;
	//protected int saved[];
	protected int states;
	protected int actions;
	protected int horizon;
	protected int init_state;
	private double gamma;
	private RlReward reward;
	private ArrayList<ArrayList<BrlTreeNode>> levels;

	public BayesDP(TransitionModelBelief prior, RlReward func, int horizon,
			int init_state, double gamma) {
		startTimer();
		initIteration();
		this.prior = prior;
		this.gamma = gamma;
		this.reward = func;
		this.states = prior.getNrStates();
		this.actions = prior.getNrActions();
		this.horizon = horizon;
		this.init_state = init_state;
		//this.saved=new int[horizon];
		addStopCriteria(new MaxIterationsCriteria(horizon));
		generate_tree();
		registerInitTime();
	}

	private void generate_tree() {
		tree = new BrlTreeNode(prior);
		levels = new ArrayList<ArrayList<BrlTreeNode>>();
		for (int l = 0; l < horizon; l++) {
			levels.add(new ArrayList<BrlTreeNode>());
		}
		register(tree, 0);
		tree.activate(init_state);
		recursion(tree, init_state, 0);
	}

	public static double polyhedral(int n, int r) {
		if (r == 1)
			return (n);
		return ((n + r - 1) / (double) r) * polyhedral(n, r - 1);
	}

	public void recursion(BrlTreeNode bel, int x, int level) {
		if (level + 2 > horizon)
			return;
		bel.setTrunc(states, actions);
		for (int a = 0; a < actions; a++) {
			for (int nx = 0; nx < states; nx++) {
				// System.out.println(bel);
				BrlTreeNode child = new BrlTreeNode(bel, x, a, nx);
				BrlTreeNode uniquechild = unique(child, level + 1, nx);
				bel.setChildren(uniquechild, nx, a);
				// System.out.println("level="+(level+1)+" nx="+nx);
				// System.out.println(uniquechild);
				if (uniquechild == child) {
					register(uniquechild, level + 1);
					recursion(uniquechild, nx, level + 1);
				}
			}
		}
	}

	private void register(BrlTreeNode child, int i) {
		levels.get(i).add(child);
	}

	private BrlTreeNode unique(BrlTreeNode child, int i, int nx) {
		ArrayList<BrlTreeNode> test = levels.get(i);
		Iterator<BrlTreeNode> it = test.iterator();
		while (it.hasNext()) {
			BrlTreeNode node = it.next();
			// System.out.println("COMPARE");
			// System.out.println(node);
			// System.out.println("VS");
			// System.out.println(child);

			if (node.compare(child)) {
				//saved[i]++;
				node.activate(nx);
				return node;
			}
			// System.out.println("=FALSE\n");
		}
		child.activate(nx);
		return child;
	}

	@Override
	public IterationStats iterate() {
		startTimer();
		//System.out.println("Saved in this level = "
		//		+ saved[horizon - iterationStats.iterations - 1]);
		boolean leaves = false;
		if (iterationStats.iterations == 0)
			leaves = true;
		int count = 0;
		Iterator<BrlTreeNode> lev = levels.get(
				horizon - iterationStats.iterations - 1).iterator();
		while (lev.hasNext()) {
			count++;
			BrlTreeNode node = lev.next();
			// System.out.print(node);

			for (int x = 0; x < states; x++) {
				if (!node.enabled(x))
					continue;
				// System.out.println("enabled x="+x);
				double max_value = Double.NEGATIVE_INFINITY;
				int max_a = -1;
				;
				for (int a = 0; a < actions; a++) {
					CustomVector vminus = new CustomVector(states);
					if (!leaves)
						for (int nx = 0; nx < states; nx++) {
							// System.out.println("-> nx="+nx+" a="+a);
							// System.out.println(node.getChildren(nx, a));
							vminus
									.set(nx, node.getChildren(nx, a).getValue(
											nx));
						}
					vminus.scale(gamma);
					CustomVector vrew = reward.get(x, a, node);
					vminus.add(vrew);
					CustomVector expect = node.getDirichlet(x, a)
							.expectedValue();
					double test_value = expect.dot(vminus);
					if (test_value > max_value) {
						max_value = test_value;
						max_a = a;
					}
				}
				// System.out.println("SET x="+x+" a="+max_a+" v="+max_value);
				node.setValue(max_value, x);
				node.setAction(max_a, x);
			}
		}
		System.out.println("Elements " + count);
		this.registerIterationTime();
		return iterationStats;
	}

	public static void main(String[] args) {
		int horizon = 3;
		int states = 5;
		int actions = 3;
		Utils.setSeed(0);
		// TransitionModelBelief.setInfoCriteria(TransitionModelBelief.IC_ENTROPY);
		TransitionModelBelief prior = new TransitionModelBelief(states, actions);
		// prior.bayesUpdate(1, 0, 1);
		// prior.bayesUpdate(1, 0, 1);
		// prior.bayesUpdate(1, 0, 1);
		// prior.bayesUpdate(1, 0, 1);
		// prior.bayesUpdate(1, 0, 1);
		RlReward reward = StateDepRlReward.getRandom(states, actions, -1, 10);
		BayesDP algo = new BayesDP(prior, reward, horizon, 0, 1.0);

		IterationStats stats = algo.run();
		System.out.println("VALUE " + algo.getTree().getValue(0));
		System.out.println("ACTION " + algo.getTree().getAction(0));
		System.out.println(stats);
		double numb = polyhedral((int)polyhedral(actions,horizon-2)*states * states, horizon - 2);
		System.out.println("magic = " + numb);
	}

	private BrlTreeNode getTree() {
		return tree;
	}

}
