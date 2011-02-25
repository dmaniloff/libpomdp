package libpomdp.common.brl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import libpomdp.common.CustomVector;
import libpomdp.common.IntegerVector;
import libpomdp.common.Utils;

public abstract class BrlTree {
	BrlTreeNode head;
	ArrayList<HashMap<Integer,BrlTreeNode>> levels ;
	int depth;
	
	public void init(int lev){
		BrlBelief.checkSpace();
		depth=lev;
		if (lev>BrlBelief.horizon) {
			Utils.error("Cannot define  level > horizon");
		}
		levels=new ArrayList<HashMap<Integer,BrlTreeNode>>();
		for (int i=0;i<lev;i++){
			levels.add(new HashMap<Integer,BrlTreeNode>());
		}
	}
	
	protected void genNextLevel(int lev,double fac) {
		Iterator<BrlTreeNode> hmapIter = levels.get(lev).values().iterator();
		BrlTreeNode node;
		int pruner=(int) ((lev+1)*fac);
		HashMap<Integer, BrlTreeNode> next_level = levels.get(lev+1);
		while (hmapIter.hasNext()){
			node=hmapIter.next();
			Iterator<Entry<Integer, BrlTreeNodeData>> iter = node.getDataIterator();
			Entry<Integer, BrlTreeNodeData> elem;
			while (iter.hasNext()){
				elem=iter.next();
				int x=Integer.valueOf(elem.getKey());
				for (int a = 0; a < BrlBelief.actions; a++) {
					for (int nx = 0; nx < BrlBelief.states; nx++) {
						BrlTreeNode child = new BrlTreeNode(node);
						child.bayesUpdate(x, a, nx);
						BrlTreeNode unique=next_level.get(new Integer(child.hashCode()));
						if (pruner != 0 && unique==null){
							unique=findNearest(child,next_level,pruner);
						}
						if (unique==null){
							unique=child;
							register(unique, lev + 1);
						}
						elem.getValue().setChild(a, nx,unique);
						unique.activate(nx);
					}
				}
			}
		}
		System.out.println("Gen l="+(lev+1)+" s="+next_level.size());
	}
	
	private BrlTreeNode findNearest(BrlTreeNode child,
			HashMap<Integer, BrlTreeNode> current_level,int distance) {
		Iterator<BrlTreeNode> lev = current_level.values().iterator();
		IntegerVector vec = child.vector.copy();
		vec.scale(-1);
		BrlTreeNode nearest=null;
		//int nearValue=Integer.MAX_VALUE;
		while (lev.hasNext()){
			BrlTreeNode test=lev.next();
			IntegerVector other = test.vector.copy();
			//System.out.println(vec);
			//System.out.println(other);
			other.add(vec);
			int val = other.norm1();
			/*if (val >= distance)
				continue;
			/*if (val < nearValue ){
				nearValue=val;
				nearest=test;
			}
			*/
			if (val < distance){
				nearest=test;
				break;
			}
		}
		return nearest;
	}
	

	protected void register(BrlTreeNode node, int lev) {
		levels.get(lev).put(node.hashCode(), node);
	}

	public HashMap<Integer,BrlTreeNode> getLevel(int i) {
		return levels.get(i);
	}
	
	protected void evalLevel(int lev,double gamma,BrlReward reward,BrlBelief prior, boolean le){
		//System.out.println("Saved in this level = "
		//		+ saved[horizon - iterationStats.iterations - 1]);
		boolean leaves = le;
		if (lev == depth-1)
			leaves = true;
		int count = 0;
		Iterator<BrlTreeNode> level = getLevel(lev).values().iterator();
		while (level.hasNext()) {
			count++;
			BrlTreeNode node = level.next();
			// System.out.print(node);
			BrlBelief current = prior.copy();
			//System.out.println(current);
			//System.out.println("+");
			//System.out.println(node);
			//System.out.println("=");
			current.add(node);
			//System.out.println(current);
			Iterator<Entry<Integer, BrlTreeNodeData>> iter = node.getDataIterator();
			Entry<Integer, BrlTreeNodeData> elem;
			while (iter.hasNext()){
				elem=iter.next();
				int x=Integer.valueOf(elem.getKey());
				double max_value = Double.NEGATIVE_INFINITY;
				int max_a = -1;
				for (int a = 0; a < BrlBelief.actions; a++) {
					CustomVector vminus = new CustomVector(BrlBelief.states);
					if (!leaves)
						for (int nx = 0; nx < BrlBelief.states; nx++) {
							// System.out.println("-> nx="+nx+" a="+a);
							// System.out.println(node.getChildren(nx, a));
							BrlTreeNode child = elem.getValue().getChild(a,nx);
							vminus.set(nx,child.getValue(nx));
						}
					vminus.scale(gamma);
					CustomVector vrew = reward.get(x, a, node);
					vminus.add(vrew);
					//System.out.println(vminus);
					CustomVector expect = current.expected(x,a);
					//System.out.println(expect);
					double test_value = expect.dot(vminus);
					if (test_value > max_value) {
						max_value = test_value;
						max_a = a;
					}
				}
				//if (lev==0)
				//System.out.println("SET x="+x+" a="+max_a+" v="+max_value);
				elem.getValue().setValueAction(max_value, max_a);
			}
		}
		//System.out.println("Elements Evaluated = " + count);
	}
	
}
