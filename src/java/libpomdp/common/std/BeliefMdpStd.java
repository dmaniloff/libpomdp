package libpomdp.common.std;


import java.io.Serializable;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefMdp;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;
import libpomdp.common.Pomdp;

public class BeliefMdpStd implements BeliefMdp,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -444173259260767792L;
	PomdpStd pom;
	CustomMatrix tau[][];
	
	public BeliefMdpStd(CustomMatrix O[], 
		     CustomMatrix T[], 
		     CustomVector R[],
		     int nrSta, int nrAct, int nrObs, 
		     double gamma,
		     String staStr[],
		     String actStr[],
		     String obsStr[],
		     CustomVector init) {
		this(new PomdpStd(O, T, R, nrSta, nrAct, nrObs, gamma,staStr, actStr, obsStr, init));
	}
	
	public BeliefMdpStd(PomdpStd pom) {
		this.pom = pom;
		init();
	}
	
	private void init(){
		tau=new CustomMatrix[nrObservations()][nrActions()];
		for (int a=0;a<nrActions();a++){
			CustomMatrix tMat=this.getTransitionProbs(a);
			CustomMatrix oMat=this.getObservationProbs(a);
			//oMat.transpose();
			//System.out.println(oMat.toString());
			for (int o=0;o<nrObservations();o++){
				CustomMatrix oDiag=new CustomMatrix(nrStates(),nrStates());
				for (int s=0;s<nrStates();s++){
					oDiag.set(s,s,oMat.get(s,o));
				}
				//System.out.println(oDiag.toString());
				tau[o][a]=tMat.mult(oDiag);
			}
		}
	}
	
	public Pomdp getPomdp() {
		return pom;
	}

	public CustomMatrix getTau(int a, int o) {
		return(tau[o][a].copy());
	}

	public String getActionString(int a) {
		return pom.getActionString(a);
	}

	public double getGamma() {
		return pom.getGamma();
	}

	public BeliefState getInitialBelief() {
		return pom.getInitialBelief();
	}

	public CustomMatrix getObservationProbs(int a) {
		return pom.getObservationProbs(a);
	}

	public String getObservationString(int o) {
		return pom.getObservationString(o);
	}

	public ValueFunctionStd getReward(int a) {
		return pom.getReward(a);
	}

	public CustomMatrix getTransitionProbs(int a) {
		return pom.getTransitionProbs(a);
	}

	public int nrActions() {
		return pom.nrActions();
	}

	public int nrObservations() {
		return pom.nrObservations();
	}

	public int nrStates() {
		return pom.nrStates();
	}

	public CustomVector sampleObservationProbs(BeliefState b, int a) {
		return pom.sampleObservationProbs(b, a);
	}

	public BeliefState sampleNextBelief(BeliefState b, int a, int o) {
		CustomVector vect=tau[o][a].mult(b.getPoint());
		vect=vect.scale(1.0/vect.norm(1.0));
		return(new BeliefStateStd(vect));
	}

	public double sampleReward(BeliefState b, int a) {
		return(pom.sampleReward(b, a));
	}

	public int getRandomObservation(BeliefStateStd bel, int a) {
		return pom.getRandomObservation(bel, a);
	}

	public String[] getStateString() {
		return pom.getStateString();
	}

	public AlphaVector projection(AlphaVector alpha, int a, int o) {
		CustomVector vect=new CustomVector(nrStates());
		vect.add(tau[o][a].mult(getGamma(),alpha.getVectorRef()));
		return(new AlphaVector(vect,a));
	}

	public int getRandomAction() {
		return(pom.getRandomAction());
	}

	public double getRewardMax() {
		return(pom.getRewardMax());
	}

	public double getRewardMin() {
		return(pom.getRewardMin());		
	}

	public double getRewardMaxMin() {
		return(pom.getRewardMaxMin());		
	}

}
