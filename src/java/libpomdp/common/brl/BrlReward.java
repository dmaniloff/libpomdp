package libpomdp.common.brl;

import libpomdp.common.CustomVector;

public interface BrlReward {
	public double get(int state, int action, int nstate, BrlBelief bel);
	public CustomVector get(int state, int action, BrlBelief bel);
}
