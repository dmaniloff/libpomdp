package libpomdp.common;


public abstract class RewardFunction {
	
	public abstract int size();
	
	public abstract ValueFunction getValueFunction(int i);

	public abstract double sample(BeliefState b, int a);

	public abstract double min(int i);

	public abstract double max(int i);
	
	public abstract double min();

	public abstract double max();
	

}
