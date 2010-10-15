package libpomdp.common.dense;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Matrix;
import libpomdp.common.BeliefMdp;
import libpomdp.common.BeliefState;
import libpomdp.common.Pomdp;

public class BeliefMdpDense implements BeliefMdp {

	PomdpDense pom;
	DenseMatrix tau[][];
	
	public BeliefMdpDense(DenseMatrix O[], 
		     DenseMatrix T[], 
		     DenseVector R[],
		     int nrSta, int nrAct, int nrObs, 
		     double gamma,
		     String actStr[],
		     String obsStr[],
		     DenseVector init) {
		this(new PomdpDense(O, T, R, nrSta, nrAct, nrObs, gamma, actStr, obsStr, init));
	}
	
	  public BeliefMdpDense(double O[][], 
			     double T[][], 
			     double R[][],
			     int nrSta, int nrAct, int nrObs, 
			     double gamma,
			     String actStr[],
			     String obsStr[],
			     double init[]){
		  this(new PomdpDense(O, T, R, nrSta, nrAct, nrObs, gamma, actStr, obsStr, init));
	  }
	
	
	public BeliefMdpDense(PomdpDense pom) {
		this.pom = pom;
		init();
	}
	
	private void init(){
		tau=new DenseMatrix[nrObservations()][nrActions()];
		for (int a=0;a<nrActions();a++){
			DenseMatrix tMat=this.getTransitionProbs(a);
			DenseMatrix oMat=this.getObservationProbs(a);
			for (int o=0;o<nrObservations();o++){
				DenseMatrix oDiag=new DenseMatrix(nrStates(),nrStates());
				for (int s=0;s<nrStates();s++){
					Matrices.zeroColumns(oDiag, oMat.get(o,s), s);
				}
				tau[o][a]=(DenseMatrix) oDiag.mult(tMat,tau[o][a]);
			}
		}
	}
	
	public Pomdp getPomdp() {
		return pom;
	}

	public Matrix getTau(int a, int o) {
		return(tau[o][a]);
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

	public DenseMatrix getObservationProbs(int a) {
		return pom.getObservationProbs(a);
	}

	public String getObservationString(int o) {
		return pom.getObservationString(o);
	}

	public DenseVector getRewardValues(int a) {
		return pom.getRewardValues(a);
	}

	public DenseMatrix getTransitionProbs(int a) {
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

	public DenseVector sampleObservationProbs(BeliefState b, int a) {
		return pom.sampleObservationProbs(b, a);
	}

	public BeliefState sampleNextBelief(BeliefState b, int a, int o) {
		return(pom.sampleNextBelief(b,a,o));
	}

	public double sampleReward(BeliefState b, int a) {
		return(pom.sampleReward(b, a));
	}

}
