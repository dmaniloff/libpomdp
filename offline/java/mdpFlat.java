/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: mdp.java
 * Description: offline upper and lower bounds based on the underlying
 *              fully observable MDP (Vmdp, Qmdp, and Blind)
 *              have a look at the README references [6,2]
 *              do not try on large problems as this will run out of mem
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import org.math.array.*;

public class mdpFlat {
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // pomdp problem specification
    private pomdpFlat problem;

    // Vmdp approximation
    private valueFunctionFlat Vmdp;

    // Qmdp approximation 
    private valueFunctionFlat Qmdp;

    // Blind approximation
    private valueFunctionFlat Blind;

    // parameters
    int max_iter   = 500;
    double epsilon = 1e-4;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// constructor
    public mdpFlat(pomdpFlat prob) {
	this.problem = prob;
	// upon construction, compute both Vmdp and Qmdp
	// given that the computation of one results in the other's
	computeVQ();
    }
    
    /// compute Vmdp and Qmdp
    public void computeVQ() {
	
	// Vmdp.v is always 1 x |S|
	double Vmdpv[][] = new double[1][problem.getnrSta()];
	Vmdpv[0]         = DoubleArray.fill(problem.getnrSta(), 0.0); // this is equiv to init with max_a R(s,a)

	// Qmdp is |A| x |S|
	double Qmdpv[][] = new double[problem.getnrAct()][problem.getnrSta()];
	int    Qmdpa[]   = new int [problem.getnrAct()];

	// declarations
	double oldVmdp[];
	double gTaV[];
	double delta[];
	double conv;

	int iter, a = 0;
	for(iter=0; iter<max_iter; iter++) {
	    // initialize Qmdp - this may not be necessary
	    Qmdpv = DoubleArray.fill(problem.getnrAct(), problem.getnrSta(), 0.0);
	    Qmdpa = IntegerArray.fill(problem.getnrAct(), -1);

	    // asynchronous update
	    oldVmdp = DoubleArray.copy(Vmdpv[0]);
	    	    
	    for(a=0; a<problem.getnrAct(); a++) {
		// Qmdp:
		// Q_a = R(s,a) + \gamma \sum_{s'} {T(s,a,s') Vmdp_{t-1}(s')
		gTaV = LinearAlgebra.times(LinearAlgebra.times(problem.getT(a), oldVmdp),
					   problem.getGamma());
		Qmdpv[a] = LinearAlgebra.plus(problem.getR(a), gTaV);
		Qmdpa[a] = a; // remember actions here start from 0!!		
	    }

	    // Vmdp:
	    // Vmdp = \max_a {Qmdp}
	    Vmdpv[0] = DoubleArray.max(Qmdpv);

	    // convergence check
	    delta = LinearAlgebra.minus(Vmdpv[0], oldVmdp);
	    //System.out.println(DoubleArray.toString(delta));
	    conv  = DoubleArray.sum(LinearAlgebra.raise(delta, 2.0));
	    //System.out.println("Conv at iteration " + iter + " is: " + conv);
	    if (conv <= epsilon)
		break;
	}

	// build value function structures
	this.Vmdp = new valueFunctionFlat(Vmdpv, null);
	this.Qmdp = new valueFunctionFlat(Qmdpv, Qmdpa);

    } // computeVQ

    

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
	double Blindv[][] = new double[problem.getnrAct()][problem.getnrSta()];
	int Blinda[]      = IntegerArray.fill(problem.getnrAct(), -1);

	for(a=0; a<problem.getnrAct(); a++) {
	    Blindv[a] = 
		DoubleArray.fill(problem.getnrSta(), 
				 DoubleArray.min(problem.getR(a))/(1.0-problem.getGamma()));
	}

		      
	for(iter=0; iter<max_iter; iter++) {
	    // copy old values
	    oldBlind = DoubleArray.copy(Blindv);
	    for(a=0; a<problem.getnrAct(); a++) {

		// Blind:
		// \alpha_a = R(s,a) + \gamma \sum_{s'} {T(s,a,s') \alpha^a_{t-1}(s')
		gTaA = LinearAlgebra.times(LinearAlgebra.times(problem.getT(a), Blindv[a]),
					   problem.getGamma());
		Blindv[a]  = LinearAlgebra.plus(problem.getR(a), gTaA);
		Blinda[a]  = a;
	    }
	    
	    // convergence check
	    delta = LinearAlgebra.minus(Blindv, oldBlind);
	    delta = LinearAlgebra.raise(delta, 2.0);
	    dists = DoubleArray.sum(DoubleArray.transpose(delta));
	    //System.out.println(DoubleArray.toString(delta));
	    conv  = DoubleArray.max(dists);
	    //System.out.println("Max euclid dist at iteration " + iter + " is: " + conv);
	    if (conv <= epsilon)
		break;
	}

	// build value functions
	Blind = new valueFunctionFlat(Blindv, Blinda);

    } // computeBlind

    // return Vmpd approximation
    public valueFunctionFlat getVmdp() {
	return Vmdp;
    }

    // return Qmdp approximation
    public valueFunctionFlat getQmdp() {
	return Qmdp;
    }

    // return Blind approximation
    public valueFunctionFlat getBlind() {
	computeBlind();
	return Blind;
    }


} // mdpFlat