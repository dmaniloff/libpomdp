/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: mdp.java
 * Description: offline upper and lower bounds based on the underlying
 *              fully observable MDP (Vmdp, Qmdp, and Blind)
 *              have a look at the README references [6,2]
 *              do not try on large problems as this will run out of mem
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
    private pomdp problem;

    // Vmdp approximation
    private valueFunction Vmdp;

    // Qmdp approximation 
    private valueFunction Qmdp;

    // Blind approximation
    private valueFunction Blind;

    // parameters
    int max_iter   = 1000;
    double epsilon = 0.00001;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// constructor
    public mdp(pomdp prob) {
	this.problem = prob;
	// upon construction, compute both Vmdp and Qmdp
	// given that the computation of one results in the other's
	computeVQ();
    }
    
    /// compute Vmdp and Qmdp
    public void computeVQ() {
	
	// Vmdp.v is always 1 x |S|
	this.Vmdp = new valueFunction(new double[1][problem.getnrSta()], null);
	this.Vmdp.v[0] = DoubleArray.fill(problem.getnrSta(), 0.0); // this is equiv to init with max_a R(s,a)

	// Qmdp is |A| x |S|
	this.Qmdp = new valueFunction(new double[problem.getnrAct()][problem.getnrSta()], null);
		
	double oldVmdp[];
	double gTaV[];
	double delta[];
	double conv;

	int iter, a = 0;
	for(iter=0; iter<max_iter; iter++) {
	    // initialize Qmdp - this may not be necessary
	    this.Qmdp.v = DoubleArray.fill(problem.getnrAct(), problem.getnrSta(), 0.0);
	    this.Qmdp.a = IntegerArray.fill(problem.getnrAct(), -1);

	    // asynchronous update
	    oldVmdp = DoubleArray.copy(this.Vmdp.v[0]);
	    	    
	    for(a=0; a<problem.getnrAct(); a++) {
		// Qmdp:
		// Q_a = R(s,a) + \gamma \sum_{s'} {T(s,a,s') Vmdp_{t-1}(s')
		gTaV = LinearAlgebra.times(LinearAlgebra.times(problem.getT(a), oldVmdp),
					   problem.getGamma());
		this.Qmdp.v[a] = LinearAlgebra.plus(problem.getR(a), gTaV);
		this.Qmdp.a[a] = a; // remember actions here start from 0!!		
	    }

	    // Vmdp:
	    // Vmdp = \max_a {Qmdp}
	    this.Vmdp.v[0] = DoubleArray.max(this.Qmdp.v);

	    // convergence check
	    delta = LinearAlgebra.minus(Vmdp.v[0], oldVmdp);
	    //System.out.println(DoubleArray.toString(delta));
	    conv  = DoubleArray.sum(LinearAlgebra.raise(delta, 2.0));
	    //System.out.println("Conv at iteration " + iter + " is: " + conv);
	    if (conv <= epsilon)
		break;
	}

    } // computeVQ

    // return Vmpd approximation
    public valueFunction getVmdp() {
	return Vmdp;
    }

    // return Qmdp approximation
    public valueFunction getQmdp() {
	return Qmdp;
    }

    // return Blind approximation
    public valueFunction getBlind() {
	computeBlind();
	return Blind;
    }

    // the computation of the blind policy is
    // done here because it seems that the convergence
    // checks should be different
    private void computeBlind() {
	
	double oldBlind[][];
	double gTaA[];
	// delta now is a matrix
	double delta[][], dists[];
	double conv;
	
	int iter, a = 0;

	// Blind is |A| x |S| - initialize each \alpha^a_{0} to \min_s {R(s,a)/(1-\gamma)}
	this.Blind = new valueFunction(new double[problem.getnrAct()][problem.getnrSta()], 
				       IntegerArray.fill(problem.getnrAct(), -1));
	for(a=0; a<problem.getnrAct(); a++) {
	    this.Blind.v[a] = 
		DoubleArray.fill(problem.getnrSta(), DoubleArray.min(problem.getR(a)));
	}

		      
	for(iter=0; iter<max_iter; iter++) {
	    // copy old values
	    oldBlind = DoubleArray.copy(this.Blind.v);
	    for(a=0; a<problem.getnrAct(); a++) {

		// Blind:
		// \alpha_a = R(s,a) + \gamma \sum_{s'} {T(s,a,s') \alpha^a_{t-1}(s')
		gTaA = LinearAlgebra.times(LinearAlgebra.times(problem.getT(a), this.Blind.v[a]),
					   problem.getGamma());
		this.Blind.v[a]  = LinearAlgebra.plus(problem.getR(a), gTaA);
		this.Blind.a[a]  = a;
	    }
	    
	    // convergence check
	    delta = LinearAlgebra.minus(this.Blind.v, oldBlind);
	    delta = LinearAlgebra.raise(delta, 2.0);
	    dists = DoubleArray.sum(DoubleArray.transpose(delta));
	    //System.out.println(DoubleArray.toString(delta));
	    conv  = DoubleArray.max(dists);
	    //System.out.println("Max euclid dist at iteration " + iter + " is: " + conv);
	    if (conv <= epsilon)
		break;
	}

    } // computeBlind

} // mdp