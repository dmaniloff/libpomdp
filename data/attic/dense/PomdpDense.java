/** ------------------------------------------------------------------------- *
  * libpomdp
 * ========
 * File: pomdpFlat.java
 * Description: class to represent a pomdp problem specification
 *              and to compute certain useful functions
 *              at the moment this class is instantiated from Matlab
 *              using the parameters from the .POMDP parser by Spaan
 *              the representation used is a flat specification from
 *              Cassandra's format
 * Copyright (c) 2009, 2010 Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.dense;

// imports
import libpomdp.common.java.BeliefState;
import libpomdp.common.java.Pomdp;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.sparse.CompColMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

import org.math.array.DoubleArray;

public class PomdpDense implements Pomdp {

    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // number of states
    private int nrSta;

    // private nrAct
    private int nrAct;

    // private nrObs
    private int nrObs;

    // observation model: a x s' x o
    private DenseMatrix O[];

    // transition model: a x s' x s
    private DenseMatrix T[];

    // reward model: a x s
    private DenseVector R[];

    // discount factor
    private double gamma;

    // action names
    private String actStr[];

    // observation names
    private String obsStr[];

    // starting belief
    private BeliefStateDense initBelief;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public PomdpDense(double O[][], 
		     double T[][], 
		     double R[][],
		     int nrSta, int nrAct, int nrObs, 
		     double gamma,
		     String actStr[],
		     String obsStr[],
		     double init[]){
    	this(new CompColMatrix[nrAct],new CompColMatrix[nrAct],new SparseVector[nrAct],nrSta,nrAct, nrObs,gamma, actStr, obsStr, new DenseVector(init));
    	for(int a = 0; a < nrAct; a++) {
    		this.O[a] = new DenseMatrix(DoubleArray.getColumnsRangeCopy(O, a*nrObs, a*nrObs + nrObs-1));
   	    	this.T[a] = new DenseMatrix(DoubleArray.getColumnsRangeCopy(T, a*nrSta, a*nrSta + nrSta-1));
   	    	this.R[a] = new DenseVector(DoubleArray.getColumnCopy(R, a)); 
    	}
    }
    
    // constructor
    public PomdpDense(CompColMatrix O[], 
		     CompColMatrix T[], 
		     SparseVector R[],
		     int nrSta, int nrAct, int nrObs, 
		     double gamma,
		     String actStr[],
		     String obsStr[],
		     DenseVector init) {
	// allocate space for the pomdp models
	this.nrSta  = nrSta;
	this.nrAct  = nrAct;
	this.nrObs  = nrObs;
	fot 
	this.O=O;
	this.T=T;
	this.R=R;
	//this.O      = new double [nrAct][nrSta][nrObs];
	//this.T      = new double [nrAct][nrSta][nrSta];
	//this.R      = new double [nrAct][nrSta];
	this.gamma  = gamma;
	this.actStr = actStr;
	this.obsStr = obsStr;
	// set initial belief state
	this.initBelief = new BeliefStateDense(init, 0.0);
	
    }

	// P(o|b,a) in vector form for all o's
    //public double[] sampleObservationProbs(BeliefState b, int a) {
    //	double Tb[]   = LinearAlgebra.times(T[a],b.getPoint());
    //	double Poba[] = LinearAlgebra.times(DoubleArray.transpose(O[a]),Tb);
    //	return Poba;
    //}
    // P(o|b,a) in vector form for all o's
    // THIS IS NOT MAKING THE RIGHT USE OF SPARSITY
    public DenseVector sampleObservationProbs(BeliefState b, int a) {
    	DenseVector  b1  = (DenseVector) b.getPoint();
    	DenseVector  Tb  = new DenseVector(nrSta);
    	Tb                = (DenseVector) T[a].mult(b1, Tb);
    	DenseVector Poba = new DenseVector(nrObs);
    	Poba              = (DenseVector) O[a].transMult(Tb, Poba);
    	return Poba;
    }
    
    /// tao(b,a,o)
    public BeliefState sampleNextBelief(BeliefState b, int a, int o) {
    	//long start = System.currentTimeMillis();
    	//System.out.println("made it to tao");
    	BeliefState bPrime;
    	// compute T[a]' * b1
    	DenseVector b1   = ((BeliefStateDense)b).getPoint();
    	DenseVector b2   = new DenseVector(nrSta);
    	b2 = (DenseVector) T[a].transMult(b1, b2);
    	//System.out.println("Elapsed in tao - T[a] * b1" + (System.currentTimeMillis() - start));
    	
    	// element-wise product with O[a](:,o)
    	for (VectorEntry e : b2)
    	    b2.set(e.index(), e.get() * O[a].get(e.index(),o));
    	//System.out.println("Elapsed in tao - O[a] .* b2" + (System.currentTimeMillis() - start));

    	// compute P(o|b,a) - norm1 is the sum of the absolute values
    	double poba = b2.norm(Vector.Norm.One);
    	// make sure we can normalize
    	if (poba < 0.00001) {
    	    //System.err.println("Zero prob observation - resetting to init");
    	    // this branch will have poba = 0.0
    	    bPrime = initBelief;
    	} else {
    	    // safe to normalize now
    	    b2 = b2.scale(1.0/poba);    
    	    bPrime = new BeliefStateDense(b2, poba);
    	}
    	//System.out.println("Elapsed in tao" + (System.currentTimeMillis() - start));
    	// return
    	return bPrime;
    }
    public double sampleReward(BeliefState bel, int a) {
    	DenseVector b = (DenseVector)bel.getPoint();
    	return b.dot(R[a]);
    }

    /// compare two belief vectors to a given accuracy - used??
    /* 
     * public boolean equalB(double[] b1, double[] b2) {
     * 	double acc = 0.00001;
     * 	double diff[];
     * 	diff = LinearAlgebra.minus(b1,b2);
     * 	return (DoubleArray.max(DoubleArray.f(diff,abs)) <= acc);
     * }
     * 
     * // define absolute value function to apply to vectors
     * private Function abs = new Function() { 
     * 	    public double f(double x) { return Math.abs(x); }};
     */

    public DenseMatrix getTransitionProbs(int a) {
	// this is used by mdp.java and there
	// we need an s x s' matrix T_a
    	return T[a];
    }

    public DenseVector getRewardValues(int a) {
    	return R[a];
    }

    public int nrStates() {
    	return nrSta;
    }

    public int nrActions() {
    	return nrAct;
    }

    public int nrObservations() {
    	return nrObs;
    }

    public double getGamma() {
    	return gamma;
    }

    public String getActionString(int a) {
    	return actStr[a];
    }

    public String getObservationString(int o) {	
    	return obsStr[o];
    }

    public BeliefState getInitialBelief() {
    	return initBelief;
    }

	public DenseMatrix getObservationProbs(int a) {
		return O[a];
	}



} // flatpomdp

