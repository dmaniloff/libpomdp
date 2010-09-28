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

package libpomdp.solve.java.offline;

// imports
import libpomdp.common.java.flat.PomdpFlat;
import libpomdp.common.java.flat.ValueFunctionFlat;

import org.math.array.DoubleArray;
import org.math.array.IntegerArray;
import org.math.array.LinearAlgebra;

public class QmdpFlat {
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // parameters
    int    MAX_ITER = 500;
    double EPSILON  = 1e-4;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// compute Vmdp and Qmdp
    public ValueFunctionFlat getqmdpFlat(PomdpFlat problem) {
	
	// Vmdp.v is always 1 x |S|
	double Vmdpv[][] = new double[1][problem.getnrSta()];
	// this is equiv to init with max_a R(s,a)
	Vmdpv[0]         = DoubleArray.fill(problem.getnrSta(), 0.0); 

	// Qmdp is |A| x |S|
	double Qmdpv[][] = new double[problem.getnrAct()][problem.getnrSta()];
	int    Qmdpa[]   = new int [problem.getnrAct()];

	// declarations
	double oldVmdp[];
	double gTaV[];
	double delta[];
	double conv;

	for(int iter=0; iter<MAX_ITER; iter++) {
	    // initialize Qmdp - this may not be necessary
	    Qmdpv = DoubleArray.fill(problem.getnrAct(), problem.getnrSta(), 0.0);
	    Qmdpa = IntegerArray.fill(problem.getnrAct(), -1);

	    // asynchronous update
	    oldVmdp = DoubleArray.copy(Vmdpv[0]);
	    	    
	    for(int a=0; a<problem.getnrAct(); a++) {
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
	    System.out.println("Conv at iteration " + iter + " is: " + conv);
	    if (conv <= EPSILON)
		break;
	}

	return new ValueFunctionFlat(Qmdpv, Qmdpa);

    } // getqmdpFlat

} // qmdpFlat