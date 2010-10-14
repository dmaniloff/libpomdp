/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: pomdpSparseUJMP
 * Description: sparse operations via UJMP
 * Copyright (c) 2009, 2010 Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.std;

// imports
import libpomdp.common.java.BelState;
import libpomdp.common.java.Pomdp;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.sparse.SparseVector;

import org.math.array.LinearAlgebra;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.doublematrix.impl.DefaultSparseDoubleMatrix;


public class PomdpSparseUJMP implements Pomdp {

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
    DefaultSparseDoubleMatrix T[];

    // observation model: a x s' x o
    DefaultSparseDoubleMatrix O[];

    // reward model: a x s'
    DefaultSparseDoubleMatrix R[];

    // discount factor
    private double gamma;

    // action names
    private String actStr[];

    // observation names
    private String obsStr[];

    // starting belief
    private BelStateSparseUJMP initBelief;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    // constructor
    public PomdpSparseUJMP(DenseMatrix  T[], 
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
	this.T      = new DefaultSparseDoubleMatrix[nrAct];
	this.O      = new DefaultSparseDoubleMatrix[nrAct];
	this.R      = new DefaultSparseDoubleMatrix[nrAct];
	this.gamma  = gamma;
	this.actStr = actStr;
	this.obsStr = obsStr;
	// set initial belief state
	double sv[] = Matrices.getArray(init);
	this.initBelief = 
	    new BelStateSparseUJMP(new DefaultSparseDoubleMatrix(MatrixFactory.
								 importFromArray(sv)), 0.0);
	// copy the model matrices
	for(int a = 0; a < nrAct; a++) {
	    this.T[a] = 
		new DefaultSparseDoubleMatrix(MatrixFactory.
					      importFromArray(Matrices.getArray(T[a])));
	    this.O[a] = 
		new DefaultSparseDoubleMatrix(MatrixFactory.
					      importFromArray(Matrices.getArray(O[a])));
	    this.R[a] = 
		new DefaultSparseDoubleMatrix(MatrixFactory.
					      importFromArray(Matrices.getArray(R[a])));
	}
    } // constuctor

    // P(o|b,a) in vector form for all o's
    public double[] P_Oba(BelState b, int a) {
	// convert this to use sparsity!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	DefaultSparseDoubleMatrix b1   = ((BelStateSparseUJMP)b).bSparse;
	DefaultSparseDoubleMatrix Tb   = (DefaultSparseDoubleMatrix) T[a].mtimes(b1);
    	DefaultSparseDoubleMatrix Poba = (DefaultSparseDoubleMatrix) O[a].transpose().
	    mtimes(Tb);
    	return Poba.toDoubleArray()[0];
    }

    /// tao(b,a,o)
    public BelState tao(BelState b, int a, int o) {
	long start = System.currentTimeMillis();
	BelState bPrime;
	DefaultSparseDoubleMatrix b1, b2;
	
	b1 = ((BelStateSparseUJMP)b).bSparse;
	// b1 comes in as a column vector, so transpose it and then times with T[a]
	b2 = (DefaultSparseDoubleMatrix) b1.transpose().mtimes(T[a]); 
	System.out.println("Elapsed in tao - T[a] * b1" + 
			   (System.currentTimeMillis() - start));
	// element-wise vector multiplication
	b2 = (DefaultSparseDoubleMatrix) b2.times(O[a].selectColumns(Ret.NEW, o));  
	System.out.println("Elapsed in tao - O[a] .* b2" + 
			   (System.currentTimeMillis() - start));
	// calculate normalizing factor
	double poba = b2.getValueSum();
	// make sure we can normalize
	if (poba < 0.00001) {
	    //System.err.println("Zero prob observation - resetting to init");
	    // this branch will have poba = 0.0
	    bPrime = initBelief;
	} else {
	    // safe to normalize now
	    b2 = (DefaultSparseDoubleMatrix) b2.divide(poba);    
	    bPrime = new BelStateSparseUJMP(b2, poba);
	}
	// return
	return bPrime;
    }

    /// R(b,a)
    public double Rba(BelState bel, int a) {
	return LinearAlgebra.sum(LinearAlgebra.
				 times(bel.getPoint(), R[a].toDoubleArray()[0]));
    }

    public double[][] getT(int a) {
	// this is used by mdp.java and there
	// we need an s x s' matrix T_a
    	return T[a].toDoubleArray();
    }

    public double[] getR(int a) {
    	return R[a].toDoubleArray()[0];
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

} // 

