/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: mdp.java
 * Description: offline upper bounds based on the underlying
 *              fully observable MDP (Vmdp and Qmdp)
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

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
	// pre-allocate arrays
	this.Vmdp.v[] = DoubleArray.fill(problem.nrSta,0.0);
	double oldVmdp[];
	double gTaV[];
	double delta[];

	int iter, a = 0;
	for(iter=0; iter<max_iter; iter++) {
	    // initialize Qmdp
	    this.Qmdp.v = DoubleArray.fill(problem.nrAct, problem.nrSta, 0.0);
	    this.Qmdp.a = IntegerArray.fill(problem.nrAct, -1);

	    // asynchronous update
	    oldVmdp = this.Vmdp.v;
	    
	    // for each action calculate the Q vector
	    for(a=0; a<problem.nrAct; a++) {
		gTaV = LinearAlgebra.times(LinearAlgebra.times(problem.T[a], oldVmdp),
					  problem.gamma);
		this.Qmdp.v[a] = LinearAlgebra.plus(R[a], gTV);
		this.Qmdp.a[a] = a;
	    }

	    // update Vmdp
	    this.Vmdp.v = DoubleArray.max(this.Qmdp);

	    // convergence check
	    delta = LinearAlgebra.minus(Vmdp.v, oldVmdp);
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