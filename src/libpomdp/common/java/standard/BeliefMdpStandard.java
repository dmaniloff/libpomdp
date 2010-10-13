package libpomdp.common.java.standard;


import libpomdp.common.java.BeliefMdp;
import libpomdp.common.java.BeliefState;
import libpomdp.common.java.CustomMatrix;
import libpomdp.common.java.CustomVector;
import libpomdp.common.java.Pomdp;

public class BeliefMdpStandard implements BeliefMdp {

	PomdpStandard pom;
	CustomMatrix tau[][];
	
	public BeliefMdpStandard(CustomMatrix O[], 
		     CustomMatrix T[], 
		     CustomVector R[],
		     int nrSta, int nrAct, int nrObs, 
		     double gamma,
		     String staStr[],
		     String actStr[],
		     String obsStr[],
		     CustomVector init) {
		this(new PomdpStandard(O, T, R, nrSta, nrAct, nrObs, gamma,staStr, actStr, obsStr, init));
	}
	
	public BeliefMdpStandard(PomdpStandard pom) {
		this.pom = pom;
		init();
	}
	
	private void init(){
		tau=new CustomMatrix[nrObservations()][nrActions()];
		for (int a=0;a<nrActions();a++){
			CustomMatrix tMat=this.getTransitionProbs(a);
			CustomMatrix oMat=this.getObservationProbs(a);
			for (int o=0;o<nrObservations();o++){
				CustomMatrix oDiag=new CustomMatrix(nrStates(),nrStates());
				for (int s=0;s<nrStates();s++){
					oDiag.set(s,s,oMat.get(o,s));
				}
				tau[o][a]=oDiag.mult(tMat);
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
