/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: pomdp.java
 * Author: Diego Maniloff
 * Description: class to represent a pomdp problem specification
 *              and to compute certain useful functions
 *              At the moment this class is instantiated from Matlab
 *              using the parameters from a .POMDP parser.
 * Copyright (c) 2009, Diego Maniloff.  
 --------------------------------------------------------------------------- */

// imports
import org.math.array.*;

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

    // reward model: s x a
    private double R[][];

    // discount factor
    private double gamma;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    // constructor receives cell2mat(problem.observationS)
    // and cell2mat(problem.transitionS)
    public pomdp(double O[][], double T[][], int nrSta, int nrAct, int nrObs, double gamma) {
	// allocate space for the pomdp models
	this.nrSta = nrSta;
	this.nrAct = nrAct;
	this.nrObs = nrObs;
	this.O = new double [nrAct][nrSta][nrObs];
	this.T = new double [nrAct][nrSta][nrSta];
	this.gamma = gamma;
	// copy the model matrices
	int i;
	for(i = 0; i < nrAct; i++) {
	    this.O[i] = DoubleArray.getColumnsRangeCopy(O, i*nrSta, i*nrSta + nrSta-1);
	    this.T[i] = DoubleArray.getColumnsRangeCopy(T, i*nrSta, i*nrSta + nrSta-1);
	}
    }

    // P(o|b,a)
    // remember here that o's and a's start from zero
    public double P_oba(int o, double b[], int a) {
	double Tb[]   = LinearAlgebra.times(T[a],b);
	double Poba[] = LinearAlgebra.times(DoubleArray.transpose(O[a]),Tb);
	return Poba[o];
    }

    // tao(b,a,o)
    public double[] tao(double b[], int a, int o) {
	double[] b1, b2;
	b1 = LinearAlgebra.times(T[a],b);
	b2 = LinearAlgebra.times(b1,DoubleArray.getColumnCopy(O[a],o));
	double sum = DoubleArray.sum(b2);
	if (sum > 0) b2 = LinearAlgebra.divide(b2,sum);
	return b2;
    }

    // R(b,a)
    public double Rba(double b[], int a) {
	return LinearAlgebra.sum(LinearAlgebra.
				 times(b,DoubleArray.getRowCopy(R,a)));
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

    public double getGamma(){
	return gamma;
    }
}