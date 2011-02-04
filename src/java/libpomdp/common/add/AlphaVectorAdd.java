package libpomdp.common.add;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefState;
import libpomdp.common.Utils;
import libpomdp.common.add.symbolic.DD;
import libpomdp.common.add.symbolic.OP;

public class AlphaVectorAdd implements AlphaVector {
	
	protected DD v;
	protected int a;
	
    public AlphaVectorAdd(DD v, int a) {
    	this.v = v;
    	this.a = a;
    }
	

	public void add(AlphaVector alpha) {
		add(((AlphaVectorAdd)alpha).v);
	}

	
	public void add(DD iref) {
		v=OP.add(iref,v);
	}

	public int compareTo(AlphaVector vec) {
		Utils.error("compareTo not implemented for Adds");
		// TODO Auto-generated method stub
		return 0;
	}


	public AlphaVector copy() {
		Utils.error("copy not implemented for Adds");
		// TODO Auto-generated method stub
		return null;
	}


	public double eval(BeliefState bel) {
		Utils.error("eval not implemented for Adds");
		// TODO Auto-generated method stub
		return 0;
	}


	public double get(int s) {
		Utils.error("get not implemented for Adds");
		// TODO Auto-generated method stub
		return 0;
	}


	public void set(AlphaVector res) {
		Utils.error("set not implemented for Adds");
		// TODO Auto-generated method stub

	}


	public int getAction() {
		return a;
	}


	public void setAction(int a) {
		this.a=a;
	}



}
