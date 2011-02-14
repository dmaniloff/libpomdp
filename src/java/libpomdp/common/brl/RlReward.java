package libpomdp.common.brl;

import libpomdp.common.CustomVector;

public interface RlReward {
	public double get(int state, int action, int nstate, TransitionModelBelief bel);
	public CustomVector get(int state, int action, TransitionModelBelief bel);
}
