package libpomdp.common.brl;


public class SpecificBrlTree extends BrlTree {

	int init_state;
		
	public SpecificBrlTree(int lev,int init_state,double fac){
		init(lev);
		this.init_state=init_state;
		generateSpecificTree(fac);
	}	
	
	private void generateSpecificTree(double fac) {
		head=new BrlTreeNode();
		register(head, 0);
		head.activate(init_state);
		for (int lev=0;lev<depth-1;lev++)
			genNextLevel(lev,fac);
	}
	
	public void eval(int level,double gamma,BrlReward reward,BrlBelief prior){
		evalLevel(level,gamma,reward,prior,false);
	}

	public BrlTreeNode head() {
		return head;
	}
	
}
