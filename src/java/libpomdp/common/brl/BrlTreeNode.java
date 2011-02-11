package libpomdp.common.brl;

public class BrlTreeNode extends TransitionModelBelief {

	protected int action=-1;
	protected double value=Double.NEGATIVE_INFINITY;
	protected BrlTreeNode children[][];
	
	public BrlTreeNode(TransitionModelBelief parent,int x,int a, int x_next) {
		super(parent);
		this.bayesUpdate(x, a, x_next);
	}
	
	public BrlTreeNode(TransitionModelBelief prior) {
		super(prior);
	}
	
	public void setTrunc(int states,int actions){
		children=new BrlTreeNode[states][actions];
	}
	
	public void setChildren(BrlTreeNode child,int x, int a){
		children[x][a]=child;
	}
	
}
