package libpomdp.common.brl;

public class BrlTreeNodeData {
	double value;
	int action;
	BrlTreeNode childs[][];
	
	BrlTreeNodeData(){
		childs=null;
		action=-1;
		value=0;
	}
	
	public void setChild(int a,int xp,BrlTreeNode child){
		if (childs==null){
			childs=new BrlTreeNode[BrlBelief.states][BrlBelief.actions];
		}
		childs[xp][a]=child;
	}
	
	public BrlTreeNode getChild(int a, int nx) {
		return childs[nx][a];
	}
	
	public void setValueAction(double val,int act) {
		value=val;
		action=act;
	}

	
}
