package libpomdp.common.brl;

public class EntropyDiffReward implements RlReward {

	public double get(int state, int action, int nstate, TransitionModelBelief bel) {
		int nrStates=bel.getNrStates();
		DirichletBelief select=bel.getDirichlet(state,action);
		int th=(int)select.getParameter(nstate);
		double tot=select.getParameterNorm();
		double retval=(nrStates-1.0)/tot - Math.log(th/(double)tot);
		for (int i=th;i<tot;i++){
			retval-=1.0/(double)i;
		}
		return retval;
	}	
}
