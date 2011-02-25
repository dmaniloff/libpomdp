package libpomdp.common.brl;


public class GenericBrlTree extends BrlTree {
	
	public GenericBrlTree(int lev,double fac){
		init(lev);
		generateGenericTree(fac);
	}		
	
	private void generateGenericTree(double fac) {
		head=new BrlTreeNode();
		register(head, 0);
		for (int i=0;i<BrlBelief.states;i++){
			head.activate(i);
		}
		for (int lev=0;lev<this.depth-1;lev++)
			genNextLevel(lev,fac);	
	}
	
	public int eval(double gamma, BrlReward reward,BrlBelief prior,int init_state,int lev_max){
		evalLevel(lev_max-1,gamma,reward,prior,true);
		for (int lev=lev_max-2;lev>=0;lev--){
			evalLevel(lev,gamma,reward,prior,false);
		}
		return head.getAction(init_state);
	}
	
		
}
