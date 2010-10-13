/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: 
 * Description: Represent a POMDP model using a flat representation and
 *              sparse matrices and vectors. This class can be constructed
 *              from a pomdpSpecSparseMTJ object after parsing a .POMDP file.
 *              Sparse matriced by matrix-toolkits-java, 
 *              every matrix will be CustomMatrix:
 *              
 * S =
 *  (3,1)        1
 *  (2,2)        2
 *  (3,2)        3
 *  (4,3)        4
 *  (1,4)        5
 * A =
 *   0     0     0     5
 *   0     2     0     0
 *   1     3     0     0
 *   0     0     4     0
 * Copyright (c) 2009, 2010 Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.standard;

// imports
import libpomdp.common.java.BeliefState;
import libpomdp.common.java.CustomMatrix;
import libpomdp.common.java.CustomVector;
import libpomdp.common.java.Pomdp;

public class PomdpStandard implements Pomdp {

    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // number of states
    private int nrSta;

    // private nrAct
    private int nrAct;

    // private nrObs
    private int nrObs;

    // transition model: a x s x s'
    private CustomMatrix T[];

    // observation model: a x s' x o
    private CustomMatrix O[];

    // reward model: a x s'
    private CustomVector  R[];

    // discount factor
    private double gamma;

    // action names
    private String actStr[];

    // observation names
    private String obsStr[];

    // starting belief
    private BeliefStateStandard initBelief;

	private String staStr[];

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// constructor
    public PomdpStandard(CustomMatrix[]  O, 
			  CustomMatrix[]  T, 
			  CustomVector[]  R,
			  int          nrSta, 
			  int          nrAct, 
			  int          nrObs, 
			  double       gamma,
			  String 	   staStr[],
			  String       actStr[],
			  String       obsStr[],
			  CustomVector init) {

	// allocate space for the pomdp models
	this.nrSta  = nrSta;
	this.nrAct  = nrAct;
	this.nrObs  = nrObs;
	this.T      = new CustomMatrix[nrAct];
	this.O      = new CustomMatrix[nrAct];
	this.R      = new CustomVector[nrAct];
	this.gamma  = gamma;
	this.staStr = staStr;
	this.actStr = actStr;
	this.obsStr = obsStr;

	// set initial belief state
	this.initBelief = new BeliefStateStandard(init, 0.0);

	// copy the model matrices - transform from dense to comprow
	// do we really need this? dense is in sparse form already...
	int a;
	for(a = 0; a < nrAct; a++) {
		this.R[a] = new CustomVector(R[a]);
	    this.T[a] = new CustomMatrix(O[a]);
	    this.O[a] = new CustomMatrix(T[a]);	    
	}
    } // constructor

    // P(o|b,a) in vector form for all o's
    public CustomVector sampleObservationProbs(BeliefState b, int a) {
    	CustomVector  b1  = b.getPoint();
    	CustomVector  Tb  = new CustomVector(nrSta);
    	Tb=T[a].mult(b1);
    	CustomVector Poba = new CustomVector(nrObs);
		Poba = O[a].transMult(Tb);
    	return Poba;
    }

    /// tao(b,a,o)
    public BeliefState sampleNextBelief(BeliefState b, int a, int o) {
	//long start = System.currentTimeMillis();
	//System.out.println("made it to tao");
	BeliefState bPrime;
	// compute T[a]' * b1
	CustomVector b1   = b.getPoint();
	CustomVector b2   = new CustomVector(nrSta);
	b2 = T[a].transMult(b1);
	//System.out.println("Elapsed in tao - T[a] * b1" + (System.currentTimeMillis() - start));
	
	// element-wise product with O[a](:,o)
	b2.elementMult(O[a].getColumn(o));
	//System.out.println("Elapsed in tao - O[a] .* b2" + (System.currentTimeMillis() - start));

	// compute P(o|b,a) - norm1 is the sum of the absolute values
	double poba = b2.norm(1.0);
	// make sure we can normalize
	if (poba < 0.00001) {
	    //System.err.println("Zero prob observation - resetting to init");
	    // this branch will have poba = 0.0
	    bPrime = initBelief;
	} else {
	    // safe to normalize now
	    b2 = b2.scale(1.0/poba);    
	    bPrime = new BeliefStateStandard(b2, poba);
	}
	//System.out.println("Elapsed in tao" + (System.currentTimeMillis() - start));
	// return
	return bPrime;
    }

    /// R(b,a)
    public double sampleReward(BeliefState bel, int a) {
	CustomVector b = ((BeliefStateStandard)bel).bSparse;
	return b.dot(R[a]);
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
	return initBelief.copy();
    }

	public CustomMatrix getObservationProbs(int a) {
		return O[a].copy();
	}

	public CustomVector getRewardValues(int a) {
		return R[a].copy();
	}

	public CustomMatrix getTransitionProbs(int a) {
		return T[a].copy();
	}

	public String[] getStateString() {
		return staStr;
	}

} // pomdpSparseMTJ

