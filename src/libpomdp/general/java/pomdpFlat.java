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

// imports
import org.math.array.*;
import org.math.array.util.*; //dont't know why the above is not enough

public class pomdpFlat implements pomdp {

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
    private double O[][][];

    // transition model: a x s' x s
    private double T[][][];

    // reward model: a x s
    private double R[][];

    // discount factor
    private double gamma;

    // action names
    private String actStr[];

    // observation names
    private String obsStr[];

    // starting belief
    private belStateFlat initBelief;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    // constructor
    public pomdpFlat(double O[][], 
		     double T[][], 
		     double R[][],
		     int nrSta, int nrAct, int nrObs, 
		     double gamma,
		     String actStr[],
		     String obsStr[],
		     double init[]) {
	// allocate space for the pomdp models
	this.nrSta  = nrSta;
	this.nrAct  = nrAct;
	this.nrObs  = nrObs;
	this.O      = new double [nrAct][nrSta][nrObs];
	this.T      = new double [nrAct][nrSta][nrSta];
	this.R      = new double [nrAct][nrSta];
	this.gamma  = gamma;
	this.actStr = actStr;
	this.obsStr = obsStr;
	// set initial belief state
	this.initBelief = new belStateFlat(init, 0.0);
	// copy the model matrices
	int a;
	for(a = 0; a < nrAct; a++) {
	    this.O[a] = DoubleArray.getColumnsRangeCopy(O, a*nrObs, a*nrObs + nrObs-1);
	    this.T[a] = DoubleArray.getColumnsRangeCopy(T, a*nrSta, a*nrSta + nrSta-1);
	    this.R[a] = DoubleArray.getColumnCopy(R, a); 
	}
    }

    // P(o|b,a)
    // remember here that o's and a's start from zero
    // public double P_oba(int o, double b[], int a) {
    // 	double Tb[]   = LinearAlgebra.times(T[a],b);
    // 	double Poba[] = LinearAlgebra.times(DoubleArray.transpose(O[a]),Tb);
    // 	return Poba[o];
    // }

    // P(o|b,a) in vector form for all o's
    public double[] P_Oba(belState b, int a) {
    	double Tb[]   = LinearAlgebra.times(T[a],b.getbPoint());
    	double Poba[] = LinearAlgebra.times(DoubleArray.transpose(O[a]),Tb);
    	return Poba;
    }

    /// tao(b,a,o)
    public belState tao(belState b, int a, int o) {
	double b1[], b2[];
	belState bPrime;
	b1 = b.getbPoint();
	b2 = LinearAlgebra.times(T[a], b1); // matrix by vector
	b2 = LinearAlgebra.times(b2, DoubleArray.getColumnCopy(O[a],o)); // element-wise
	double poba = DoubleArray.sum(b2);
	// make sure we can normalize
	if (poba < 0.00001) {
	    //System.err.println("Zero prob observation - resetting to init");
	    // this branch will have poba = 0.0
	    bPrime = initBelief;
	} else {
	    // safe to normalize now
	    b2 = LinearAlgebra.divide(b2,DoubleArray.sum(b2));    
	    bPrime = new belStateFlat(b2, poba);
	}
	// return
	return bPrime;
    }

    /// R(b,a)
    public double Rba(belState bel, int a) {
	return LinearAlgebra.sum(LinearAlgebra.
				 times(bel.getbPoint(), DoubleArray.getRowCopy(R,a)));
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

    public double[][] getT(int a) {
	// this is used by mdp.java and there
	// we need an s x s' matrix T_a
    	return DoubleArray.transpose(T[a]);
    }

    public double[] getR(int a) {
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

    public belState getInit() {
	return initBelief;
    }

} // flatpomdp

