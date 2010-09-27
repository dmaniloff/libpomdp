/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: 
 * Description: Represent a POMDP model using a flat representation and
 *              sparse matrices and vectors. This class can be constructed
 *              from a pomdpSpecSparseMTJ object after parsing a .POMDP file.
 *              Sparse matriced by matrix-toolkits-java, 
 *              every matrix will be CompColMatrix:
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

package libpomdp.general.java;

// imports
import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.sparse.*;

public class pomdpSparseMTJ implements Pomdp {

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
    CompColMatrix T[];

    // observation model: a x s' x o
    CompColMatrix O[];

    // reward model: a x s'
    SparseVector  R[];

    // discount factor
    private double gamma;

    // action names
    private String actStr[];

    // observation names
    private String obsStr[];

    // starting belief
    private BelStateSparseMTJ initBelief;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// constructor
    public pomdpSparseMTJ(DenseMatrix  T[], 
			  DenseMatrix  O[], 
			  SparseVector R[],
			  int          nrSta, 
			  int          nrAct, 
			  int          nrObs, 
			  double       gamma,
			  String       actStr[],
			  String       obsStr[],
			  SparseVector init) {

	// allocate space for the pomdp models
	this.nrSta  = nrSta;
	this.nrAct  = nrAct;
	this.nrObs  = nrObs;
	this.T      = new CompColMatrix[nrAct];
	this.O      = new CompColMatrix[nrAct];
	this.R      = R;
	this.gamma  = gamma;
	this.actStr = actStr;
	this.obsStr = obsStr;

	// set initial belief state
	this.initBelief = new BelStateSparseMTJ(init, 0.0);

	// copy the model matrices - transform from dense to comprow
	// do we really need this? dense is in sparse form already...
	int a;
	for(a = 0; a < nrAct; a++) {
	    this.T[a] = new CompColMatrix(T[a]);
	    this.O[a] = new CompColMatrix(O[a]);	    
	}
    } // constructor

    // P(o|b,a) in vector form for all o's
    // THIS IS NOT MAKING THE RIGHT USE OF SPARSITY
    public double[] P_Oba(BelState b, int a) {
	SparseVector  b1  = ((BelStateSparseMTJ)b).bSparse;
    	SparseVector  Tb  = new SparseVector(nrSta);
	Tb                = (SparseVector) T[a].mult(b1, Tb);
    	SparseVector Poba = new SparseVector(nrObs);
	Poba              = (SparseVector) O[a].transMult(Tb, Poba);
    	return Matrices.getArray(Poba);
    }

    /// tao(b,a,o)
    public BelState tao(BelState b, int a, int o) {
	long start = System.currentTimeMillis();
	//System.out.println("made it to tao");
	BelState bPrime;
	// compute T[a]' * b1
	SparseVector b1   = ((BelStateSparseMTJ)b).bSparse;
	SparseVector b2   = new SparseVector(nrSta);
	b2 = (SparseVector) T[a].transMult(b1, b2);
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
	    bPrime = new BelStateSparseMTJ(b2, poba);
	}
	//System.out.println("Elapsed in tao" + (System.currentTimeMillis() - start));
	// return
	return bPrime;
    }

    /// R(b,a)
    public double Rba(BelState bel, int a) {
	SparseVector b = ((BelStateSparseMTJ)bel).bSparse;
	return b.dot(R[a]);
    }

    public double[][] getT(int a) {
	// this is used by mdp.java and there
	// we need an s x s' matrix T_a
    	return Matrices.getArray(T[a]);
    }
    
    public double[][] getO(int a) {
	// this is used by mdp.java and there
	// we need an s x s' matrix T_a
    	return Matrices.getArray(O[a]);
    }

    // not part of the interface, to replace getT
    public CompColMatrix T(int a) {
	return T[a];
    }

    public double[] getR(int a) {
    	return Matrices.getArray(R[a]);
    }

    public SparseVector R(int a) {
	return R[a];
    }

    public int getnrSta() {
	return nrSta;
    }

    public int getnrAct() {
	return nrAct;
    }

    public int getnrObs() {
	return nrObs;
    }

    public double getGamma() {
	return gamma;
    }

    public String getactStr(int a) {
	return actStr[a];
    }

    public String getobsStr(int o) {	
	return obsStr[o];
    }

    public BelState getInit() {
	return initBelief;
    }

} // pomdpSparseMTJ

