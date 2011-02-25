package libpomdp.common.brl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class BrlTreeNode extends BrlBelief {

	private HashMap<Integer,BrlTreeNodeData> data;
	// Please note that actions here are stored in the form a+1 to use sparsity
	
	public BrlTreeNode(){
		super();
		data=new HashMap<Integer,BrlTreeNodeData>();
	}

	public BrlTreeNode(BrlTreeNode init){
		super(init);
		data=new HashMap<Integer,BrlTreeNodeData>();
	}
	
	public double getValue(int x) {
		return data.get(new Integer(x)).value;
	}
	


	public int getAction(int x) {
		return data.get(new Integer(x)).action;
	}

	
	//public void setTrunc(){
	//	childs=new BrlTreeNode[states][actions];
	//}
	
	
	
	public void activate(int x) {
		data.put(new Integer(x), new BrlTreeNodeData());
	}

	public Iterator<Entry<Integer, BrlTreeNodeData>> getDataIterator() {
		return data.entrySet().iterator();
	}

	public BrlTreeNode getChild(int x, int a, int nx) {
		return data.get(new Integer(x)).childs[nx][a];
	}

	
	//public boolean enabled(int x) {
	//	if (parentlist.get(x)==1)
	//		return true;
	//	return false;
	//}



}
