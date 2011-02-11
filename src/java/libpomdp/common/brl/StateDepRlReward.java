package libpomdp.common.brl;

import libpomdp.common.CustomVector;



public class StateDepRlReward implements RlReward{
	
	CustomVector[][] func;
	
	public StateDepRlReward(CustomVector[][] r) {
		func=r;
	}

	public double get(int state, int action, int nstate, TransitionModelBelief bel) {
		return func[state][action].get(nstate);
	}

	public static StateDepRlReward getRandom(int states,int actions,double rmin,double rmax){
		CustomVector[][] func=new CustomVector[states][actions];
		CustomVector minv=CustomVector.getHomogene(states, rmin);
		for (int a=0;a<actions;a++){
			for (int x=0;x<states;x++){
				func[x][a]=CustomVector.getRandomUnitary(states);
				func[x][a].scale(rmax-rmin);
				func[x][a].add(minv);
			}
		}
		return new StateDepRlReward(func);
	}
	
}
