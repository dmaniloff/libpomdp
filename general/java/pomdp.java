/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: pomdp.java
 * Description: class to represent a pomdp problem specification
 *              and to compute certain useful functions
 *              At the moment this class is instantiated from Matlab
 *              using the parameters from a .POMDP parser.
 * Copyright (c) 2009, Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import org.math.array.*;
import org.math.array.util.*; //dont't know why the above is not enough

public class pomdp {

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
    public double R[][];

    // discount factor
    private double gamma;

    // action names
    private String actStr[];

    // observation names
    private String obsStr[];

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    // constructor receives:
    // cell2mat(problem.observationS)
    // cell2mat(problem.transitionS)
    // cell2mat(problem.rewardS)
    public pomdp(double O[][], 
		 double T[][], 
		 double R[][],
		 int nrSta, int nrAct, int nrObs, 
		 double gamma,
		 String actStr[],
		 String obsStr[]) {
	// allocate space for the pomdp models
	this.nrSta = nrSta;
	this.nrAct = nrAct;
	this.nrObs = nrObs;
	this.O = new double [nrAct][nrSta][nrObs];
	this.T = new double [nrAct][nrSta][nrSta];
	this.R = new double [nrAct][nrSta];
	this.gamma = gamma;
	this.actStr = actStr;
	this.obsStr = obsStr;
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
    public double P_oba(int o, double b[], int a) {
	double Tb[]   = LinearAlgebra.times(T[a],b);
	double Poba[] = LinearAlgebra.times(DoubleArray.transpose(O[a]),Tb);
	return Poba[o];
    }

    // P(o|b,a) in vector form for all o's
    public double[] P_Oba(double b[], int a) {
	double Tb[]   = LinearAlgebra.times(T[a],b);
	double Poba[] = LinearAlgebra.times(DoubleArray.transpose(O[a]),Tb);
	return Poba;
    }

    // tao(b,a,o)
    public double[] tao(double b[], int a, int o) {
	// get current time
	long start = System.currentTimeMillis();
	double[] b1, b2;
	b1 = LinearAlgebra.times(T[a],b);
	//System.out.println(DoubleArray.toString(O[a])+"o is"+o+"and a is"+a);
	b2 = LinearAlgebra.times(b1,DoubleArray.getColumnCopy(O[a],o));
	double sum = DoubleArray.sum(b2);
	// normalization
	if (sum > 0) b2 = LinearAlgebra.divide(b2,sum);
	//System.out.format("tao took: %tL\n", System.currentTimeMillis()-start);
	return b2;
    }

    // R(b,a)
    public double Rba(double b[], int a) {
	return LinearAlgebra.sum(LinearAlgebra.
				 times(b,DoubleArray.getRowCopy(R,a)));
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

    public double[][][] getT() {
	return T;
    }

    public double[][] getR() {
	return R;
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

    public String[] getactStr() {
	return actStr;
    }

    public String[] getobsStr() {
	return obsStr;
    }

} // pomdp

