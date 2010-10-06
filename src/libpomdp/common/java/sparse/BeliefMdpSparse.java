package libpomdp.common.java.sparse;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.CompColMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;
import libpomdp.common.java.BeliefMdp;
import libpomdp.common.java.BeliefState;
import libpomdp.common.java.Pomdp;

public class BeliefMdpSparse implements BeliefMdp {

	PomdpSparse pom;
	CompColMatrix tau[][];
	
	public BeliefMdpSparse(DenseMatrix O[], 
		     DenseMatrix T[], 
		     SparseVector R[],
		     int nrSta, int nrAct, int nrObs, 
		     double gamma,
		     String actStr[],
		     String obsStr[],
		     SparseVector init) {
		this(new PomdpSparse(O, T, R, nrSta, nrAct, nrObs, gamma, actStr, obsStr, init));
	}
	
	public BeliefMdpSparse(PomdpSparse pom) {
		this.pom = pom;
		init();
	}
	
	private void init(){
		// TODO: Not Using Sparsity... :(
		tau=new CompColMatrix[nrObservations()][nrActions()];
		for (int a=0;a<nrActions();a++){
			CompColMatrix tMat=this.getTransitionProbs(a);
			CompColMatrix oMat=this.getObservationProbs(a);
			for (int o=0;o<nrObservations();o++){
				DenseMatrix oDiag=new DenseMatrix(nrStates(),nrStates());
				for (int s=0;s<nrStates();s++){
					Matrices.zeroColumns(oDiag, oMat.get(o,s), s);
				}
				tau[o][a]=new CompColMatrix(oDiag.mult(tMat,tau[o][a]));
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

	public CompColMatrix getObservationProbs(int a) {
		return pom.getObservationProbs(a);
	}

	public String getObservationString(int o) {
		return pom.getObservationString(o);
	}

	public SparseVector getRewardValues(int a) {
		return pom.getRewardValues(a);
	}

	public CompColMatrix getTransitionProbs(int a) {
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

	public SparseVector sampleObservationProbs(BeliefState b, int a) {
		return pom.sampleObservationProbs(b, a);
	}

	public BeliefState sampleNextBelief(BeliefState b, int a, int o) {
		return(pom.sampleNextBelief(b,a,o));
	}

	public double sampleReward(BeliefState b, int a) {
		return(pom.sampleReward(b, a));
	}

}
