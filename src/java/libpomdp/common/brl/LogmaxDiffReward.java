package libpomdp.common.brl;

import libpomdp.common.CustomVector;

public class LogmaxDiffReward implements BrlReward {

	public double get(int state, int action, int nstate, BrlBelief bel) {
		int th=bel.get(state,action,nstate);
		double tot=bel.getMarginal(state,action);
		double retval=Math.log(tot) ;
		if (th>1) 
			retval+=(th-1)*Math.log(th/(th-1));
		return retval;
	}

	public CustomVector get(int state, int action, BrlBelief bel) {
		// TODO Auto-generated method stub
		return null;
	}	
}
