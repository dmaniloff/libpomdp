package libpomdp.common;


public interface RewardFunction {

	public abstract double min(int i);

	public abstract double max(int i);
	
	public abstract double min();

	public abstract double max();

	// Reward without information state
	public abstract double get(int state, int action);

}
