package libpomdp.common.brl;

public class ValueActionPair {
	
	public ValueActionPair(){
		action=-1;
		value=Double.NEGATIVE_INFINITY;
	}
	public int action;
	public double value;
}
