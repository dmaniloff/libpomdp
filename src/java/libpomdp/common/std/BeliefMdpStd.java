package libpomdp.common.std;

import java.io.Serializable;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefMdp;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;
import libpomdp.common.Pomdp;

public class BeliefMdpStd implements BeliefMdp, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -444173259260767792L;
    PomdpStd pom;
    CustomMatrix tau[][];

    public BeliefMdpStd(CustomMatrix O[], CustomMatrix T[], CustomVector R[],
	    int nrSta, int nrAct, int nrObs, double gamma, String staStr[],
	    String actStr[], String obsStr[], CustomVector init) {
	this(new PomdpStd(O, T, R, nrSta, nrAct, nrObs, gamma, staStr, actStr,
		obsStr, init));
    }

    public BeliefMdpStd(PomdpStd pom) {
	this.pom = pom;
	init();
    }

    private void init() {
	tau = new CustomMatrix[nrObservations()][nrActions()];
	for (int a = 0; a < nrActions(); a++) {
	    CustomMatrix tMat = this.getTransitionTable(a);
	    CustomMatrix oMat = this.getObservationTable(a);
	    // oMat.transpose();
	    // System.out.println(oMat.toString());
	    for (int o = 0; o < nrObservations(); o++) {
		CustomMatrix oDiag = new CustomMatrix(nrStates(), nrStates());
		for (int s = 0; s < nrStates(); s++) {
		    oDiag.set(s, s, oMat.get(s, o));
		}
		// System.out.println(oDiag.toString());
		tau[o][a] = tMat.mult(oDiag);
	    }
	}
    }

    
    public BeliefState nextBeliefState(BeliefState b, int a, int o) {
	CustomVector vect = tau[o][a].mult(b.getPoint());
	vect = vect.scale(1.0 / vect.norm(1.0));
	return (new BeliefStateStd(vect));
    }

    
    public double expectedImmediateReward(BeliefState b, int a) {
	return (pom.expectedImmediateReward(b, a));
    }

    
    public CustomVector observationProbabilities(BeliefState b, int a) {
	return pom.observationProbabilities(b, a);
    }

    
    public CustomMatrix getTransitionTable(int a) {
	return pom.getTransitionTable(a);
    }

    
    public CustomMatrix getObservationTable(int a) {
	return pom.getObservationTable(a);
    }

    
    // / R(s,a): 1 x s vector
    public CustomVector getImmediateRewards(int a) {
	return pom.getImmediateRewards(a);
    }

    public ValueFunctionStd getRewardValueFunction(int a) {
	return pom.getRewardValueFunction(a);
    }

    
    public BeliefState getInitialBeliefState() {
	return pom.getInitialBeliefState();
    }

    
    public int nrStates() {
	return pom.nrStates();
    }

    
    public int nrActions() {
	return pom.nrActions();
    }

    
    public int nrObservations() {
	return pom.nrObservations();
    }

    
    public double getGamma() {
	return pom.getGamma();
    }

    
    public String getActionString(int a) {
	return pom.getActionString(a);
    }

    
    public String getObservationString(int o) {
	return pom.getObservationString(o);
    }

    
    public String getStateString(int s) {
	return pom.getStateString(s);
    }

    public Pomdp getPomdp() {
	return pom;
    }

    
    public CustomMatrix getTau(int a, int o) {
	return (tau[o][a].copy());
    }

    public int getRandomObservation(BeliefStateStd bel, int a) {
	return pom.sampleObservation(bel, a);
    }

    public AlphaVector projection(AlphaVectorStd alpha, int a, int o) {
	CustomVector vect = new CustomVector(nrStates());
	vect.add(tau[o][a].mult(getGamma(), alpha.getInternalRef()));
	return (new AlphaVectorStd(vect, a));
    }

    public int getRandomAction() {
	return (pom.getRandomAction());
    }

    public double getRewardMax() {
	return (pom.getRewardMax());
    }

    public double getRewardMin() {
	return (pom.getRewardMin());
    }

    public double getRewardMaxMin() {
	return (pom.getRewardMaxMin());
    }

}
