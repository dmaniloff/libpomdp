package libpomdp.solve.offline.brl;

import java.util.ArrayList;
import java.util.Iterator;

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
	protected int states;
	protected int actions;
	protected int horizon;
	protected int init_state;
	private double gamma;
	private RlReward reward;
	private ArrayList<ArrayList<BrlTreeNode>> levels;
	
	
	public BayesDP(TransitionModelBelief prior, RlReward func,int horizon, int init_state, double gamma){
		startTimer();
		this.prior=prior;
		this.gamma=gamma;
		this.reward=func;
		this.states=prior.getNrStates();
		this.actions=prior.getNrActions();
		this.horizon=horizon;
		this.init_state=init_state;
		generate_tree();
		this.registerInitTime();
	}
	
	private void generate_tree() {
		tree=new BrlTreeNode(prior);
		levels = new ArrayList<ArrayList<BrlTreeNode>>();
		for (int l=0;l<horizon;l++){
			levels.add(new ArrayList<BrlTreeNode>());
		}
		register(tree,0);
		recursion(tree,init_state, 0);
	}

	/*public static double polyhedral(int n,int r){
		if (r==1) return (n);
		return ((n + r - 1)/(double)r)*polyhedral(n,r-1);
	}*/
	
	public void recursion(BrlTreeNode bel,int x,int level){
		if (level>=horizon) return;
		bel.setTrunc(states,actions);
		//System.out.println(bel);
		for (int a=0;a<actions;a++){
			for (int nx=0;nx<states;nx++){
				//System.out.println(bel);
				BrlTreeNode child=new BrlTreeNode(bel,x,a,nx);
				BrlTreeNode uniquechild=unique(child,level+1);
				bel.setChildren(child, nx, a);
				if (uniquechild.equals(child)){
					register(child,level+1);
					recursion(child,nx,level+1);
				}
			}
		}
	}

	private void register(BrlTreeNode child, int i) {
		levels.get(i).add(child);
	}

	private BrlTreeNode unique(BrlTreeNode child, int i) {
		ArrayList<BrlTreeNode> test=levels.get(i);
		Iterator<BrlTreeNode> it = test.iterator();
		while(it.hasNext()){
			BrlTreeNode node=it.next();
			if (node.compare(child))
				return node;
		}
		return child;
	}

	
	@Override
	public IterationStats iterate() {
		startTimer();
		int count=0;
		Iterator<BrlTreeNode> lev = levels.get(iterationStats.iterations).iterator();
		while(lev.hasNext()){
			count++;
			BrlTreeNode node=lev.next();
			System.out.print("ELEMENT "+count+"\n");
			System.out.print(node);
		}
		
		this.registerIterationTime();
		return iterationStats;
	}

	public static void main(String[] args){
		//TransitionModelBelief.setInfoCriteria(TransitionModelBelief.IC_ENTROPY);
		TransitionModelBelief prior=new TransitionModelBelief(2, 2);
		RlReward reward=StateDepRlReward.getRandom(2, 2, -1, 10);
		BayesDP algo=new BayesDP(prior,reward,10,0,1.0);
		algo.addStopCriteria(new MaxIterationsCriteria(10));
		algo.run();
	}

}
