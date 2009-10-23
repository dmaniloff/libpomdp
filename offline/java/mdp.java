/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: mdp.java
 * Description: offline upper bounds based on the underlying
 *              fully observable MDP (Vmdp and Qmdp)
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import org.math.array.*;

public class mdp {
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // pomdp problem specification
    public pomdp problem;

    // Vmdp approximation
    private valueFunction Vmdp;

    // Qmdp approximation (may need pruning here??)
    private valueFunction Qmdp;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// constructor
    public mdp(pomdp prob) {
	this.problem = prob;
	compute();
    }
    
    /// approximation using the underlying FOMDP
    public void compute() {
	// parameters
	int max_iter = 1000;
	double epsilon = 0.00001;

	// pre-allocate arrays - Vmdp.v in this part case is 1D
	this.Vmdp = new valueFunction();
	this.Qmdp = new valueFunction();

	this.Vmdp.v = new double[1][problem.getnrSta()];
	this.Vmdp.v[0] = DoubleArray.fill(problem.getnrSta(),0.0);

	double oldVmdp[];
	double gTaV[];
	double delta[];

	int iter, a = 0;
	for(iter=0; iter<max_iter; iter++) {
	    // initialize Qmdp
	    this.Qmdp.v = DoubleArray.fill(problem.getnrAct(), problem.getnrSta(), 0.0);
	    this.Qmdp.a = IntegerArray.fill(problem.getnrAct(), -1);

	    // asynchronous update
	    oldVmdp = this.Vmdp.v[0];
	    
	    // for each action calculate the Q vector
	    for(a=0; a<problem.getnrAct(); a++) {
		gTaV = LinearAlgebra.times(LinearAlgebra.times(problem.getT()[a], oldVmdp),
					   problem.getGamma());
		this.Qmdp.v[a] = LinearAlgebra.plus(problem.getR()[a], gTaV);
		this.Qmdp.a[a] = a; // remember actions here start from 0!!
	    }

	    // update Vmdp
	    this.Vmdp.v[0] = DoubleArray.max(this.Qmdp.v);

	    // convergence check
	    delta = LinearAlgebra.minus(Vmdp.v[0], oldVmdp);
	    if (DoubleArray.sum(LinearAlgebra.raise(delta, 2.0)) <= epsilon)
		break;
	}
    } // compute

    // return Vmpd approximation
    public valueFunction getVmdp() {
	return Vmdp;
    }

    // return Qmdp approximation
    public valueFunction getQmdp() {
	return Qmdp;
    }

} // mdp