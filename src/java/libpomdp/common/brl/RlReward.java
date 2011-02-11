package libpomdp.common.brl;

public interface RlReward {
	public abstract double get(int state, int action, int nstate, TransitionModelBelief bel);

}
