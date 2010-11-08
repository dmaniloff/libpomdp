package libpomdp.common.std;


import java.io.Serializable;

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

	public CustomVector getRewardValues(int a) {
		return pom.getRewardValues(a);
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
		return(pom.sampleNextBelief(b,a,o));
	}

	public double sampleReward(BeliefState b, int a) {
		return(pom.sampleReward(b, a));
	}

}
