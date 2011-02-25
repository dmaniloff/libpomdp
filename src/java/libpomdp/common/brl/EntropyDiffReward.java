package libpomdp.common.brl;

import libpomdp.common.CustomVector;

public class EntropyDiffReward implements BrlReward {

	public double get(int state, int action, int nstate, BrlBelief bel) {
		int nrStates=BrlBelief.states();
		int th=bel.get(state,action,nstate);
		double tot=bel.getMarginal(state,action);
		double retval=(nrStates-1.0)/tot - Math.log(th/(double)tot);
		for (int i=th;i<tot;i++){
			retval-=1.0/(double)i;
		}
		return retval;
	}

	public CustomVector get(int state, int action, BrlBelief bel) {
	//	CustomVector.convert(bel.getSubVector(state,action));
		return null;
	}	
}
