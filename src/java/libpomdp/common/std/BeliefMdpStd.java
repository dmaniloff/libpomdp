package libpomdp.common.std;

import java.io.Serializable;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefMdp;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;

public class BeliefMdpStd extends PomdpStd implements BeliefMdp, Serializable {

    private static final long serialVersionUID = -444173259260767792L;
    CustomMatrix tau[][];

    public BeliefMdpStd(CustomMatrix O[], CustomMatrix T[], CustomVector R[],
	    int nrSta, int nrAct, int nrObs, double gamma, String staStr[],
	    String actStr[], String obsStr[], CustomVector init) {
	this(new PomdpStd(O, T, R, nrSta, nrAct, nrObs, gamma, staStr, actStr,
		obsStr, init));
    }

    public BeliefMdpStd(PomdpStd pom) {
    	super(pom);
    	init();
    }

    private void init() {
	tau = new CustomMatrix[nrObservations()][nrActions()];
	for (int a = 0; a < nrActions(); a++) {
	    for (int o = 0; o < nrObservations(); o++) {
		CustomMatrix oDiag = new CustomMatrix(nrStates(), nrStates());
			for (int s = 0; s < nrStates(); s++) {
				oDiag.set(s, s, O.getValue(o,s,a));
			}
		// System.out.println(oDiag.toString());
		tau[o][a] = oDiag.mult(T.getMatrix(a));
	    }
	}
    }

    
    public BeliefState nextBeliefState(BeliefState b, int a, int o) {
	CustomVector vect = tau[o][a].mult(b.getPoint());
	vect.normalize();
	return (new BeliefStateStd(vect));
    }
    
    public CustomMatrix getTau(int a, int o) {
	return (tau[o][a].copy());
    }

    public AlphaVector project(AlphaVectorStd alpha, int a, int o) {
	CustomVector vect = new CustomVector(nrStates());
	vect.add(tau[o][a].mult(getGamma(), alpha));
	return (new AlphaVectorStd(vect, a));
    }


	public BeliefMdp getBeliefMdp() {
		return this;
	}

	public AlphaVector project(AlphaVector prev, int a, int o) {
		return project((AlphaVectorStd)prev,a,o);
	}

}
