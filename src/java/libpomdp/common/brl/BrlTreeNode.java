package libpomdp.common.brl;

public class BrlTreeNode extends TransitionModelBelief {

	protected ValueActionPair values[];

	public BrlTreeNode(TransitionModelBelief parent,int x,int a, int x_next) {
		super(parent);
		values=new ValueActionPair[states];
		for (int i=0;i<states;i++)
			values[i]=null;
		this.bayesUpdate(x, a, x_next);
	}
	
	public BrlTreeNode(TransitionModelBelief prior) {
		super(prior);
		values=new ValueActionPair[states];
		for (int i=0;i<states;i++)
			values[i]=null;
		
	}
	
	public int getAction(int state) {
		return values[state].action;
	}

	public void setAction(int action,int state) {
		values[state].action=action;
	}

	public double getValue(int state) {
		return values[state].value;
	}

	public void setValue(double value,int state) {
		this.values[state].value = value;
	}

	public BrlTreeNode getChildren(int x,int a) {
		return children[x][a];
	}

	protected BrlTreeNode children[][];
	
	
	public void setTrunc(int states,int actions){
		children=new BrlTreeNode[states][actions];
	}
	
	public void setChildren(BrlTreeNode child,int x, int a){
		children[x][a]=child;
	}

	public void activate(int x) {
		//System.out.println("Activate "+x);
		if (values[x]==null)
			values[x]=new ValueActionPair();
	}
	
	public boolean enabled(int x){
		//System.out.println();
		if (values[x]==null) return false;
		return true;
	}
	
}
