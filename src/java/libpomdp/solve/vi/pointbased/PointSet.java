package libpomdp.solve.vi.pointbased;

import java.util.ArrayList;
import java.util.Iterator;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.Utils;

public class PointSet implements Iterable<BeliefState> {
	ArrayList<BeliefState> set;

	PointSet(){
		set=new ArrayList<BeliefState>();
	}
	
	public boolean add(BeliefState arg0) {
		if (!member(arg0))
			return set.add(arg0);
		return false;
	}

	public boolean member(BeliefState arg0) {
		for (BeliefState it:set){
			//System.out.println(it);
			if (it.compare(arg0))
				return true;
		}
		return false;
	}

	public void clear() {
		set.clear();
	}

	public boolean contains(BeliefState arg0) {
		return set.contains(arg0);
	}

	public BeliefState get(int arg0) {
		return set.get(arg0);
	}

	public BeliefState remove(int arg0) {
		return set.remove(arg0);
	}

	public boolean remove(BeliefState arg0) {
		return set.remove(arg0);
	}

	public void addAll(PointSet adder) {
		for (BeliefState it:adder){
			set.add(it);
		}
	}

	public int size() {
		return(set.size());
	}

	public Iterator<BeliefState> iterator() {
		return(set.iterator());
	}

	public BeliefState getRandom() {
		return get(Utils.gen.nextInt(Integer.MAX_VALUE)%size());
	}

	public PointSet copy() {
		PointSet retval = new PointSet();
		for (BeliefState bel:set){
			retval.add(bel.copy());
		}
		return retval;
	}
	
	public String toString(){
    	String retval="Point Set\n";
    	for (int i=0;i<size();i++){
    		retval+="p"+i+"\t[";
    		CustomVector v=set.get(i).getPoint();
    		for (int j=0;j<v.size();j++){
    			retval+=v.get(j)+" ";
    		}
    		retval+="]\n";
    	}
    	return retval;
    }
	
}
