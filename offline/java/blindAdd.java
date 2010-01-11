/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: blindAdd.java
 * Description: compute blind policy lower bound
 * Copyright (c) 2010, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import org.math.array.*;

public class blindAdd {

    // parameters
    final int MAXITERATIONS = 500;
    final double EPSILON    = 1e-5;
    
    public valueFunctionAdd getBlindAdd(pomdpAdd factoredProb) {

	// decls
	int a, iter;
	DD[] adds;//       = new DD[3];
	double deltas[] = new double[factoredProb.getnrAct()];
	double maxdelta;

	// allocate a alpha vectors
	DD alphas[]     = new DD[factoredProb.getnrAct()];
	DD old_alphas[] = new DD[factoredProb.getnrAct()];

	// allocate policy - one vec per action
	int policy[]    = new int [factoredProb.getnrAct()];
	DD ddDiscFact   = DDleaf.myNew(factoredProb.getGamma());


	// initialize alphas and policy
	// \alpha_0 = \min_s {R(s,a} / (1-\gamma)
	for (a=0; a<factoredProb.getnrAct(); a++) {
	    alphas[a] = DDleaf.myNew(OP.minAll(factoredProb.R[a]));
	    policy[a] = a;
	}

	for(iter=0; iter<MAXITERATIONS; iter++) {
	    // save alphas
	    System.arraycopy(alphas, 0, old_alphas, 0, alphas.length);
	    // prime vars forward in the |A| alphas
	    alphas = OP.primeVarsN(alphas, factoredProb.getnrTotV());

	    for(a=0; a<factoredProb.getnrAct(); a++) {
		// concat all ADDs into one array        
		adds                = new DD[1+factoredProb.T[a].length+1];
		adds[0]             = ddDiscFact;
		System.arraycopy(factoredProb.T[a], 0, adds, 1, factoredProb.T[a].length);
		adds[adds.length-1] = alphas[a];		
		//new DD[] {ddDiscFact, factoredProb.T[a], alphas[a]};
		// \alpha_t^a = R(s,a) + \gamma \sum_{s'} {T(s,a,s') \alpha_{t-1}^a}
		alphas[a]           = OP.addMultVarElim(adds, factoredProb.staIdsPr);        
		//		alphas[a]           = OP.addMultVarElimNoMem(adds, factoredProb.staIdsPr);        
		alphas[a]           = OP.add(factoredProb.R[a], alphas[a]);
		//alphas[a]           = OP.addNoMem(factoredProb.R[a], alphas[a]);
	    }
   
	    // convergence check 
	    for(a=0; a<factoredProb.getnrAct(); a++) {
		deltas[a] = OP.maxAll(OP.abs(OP.sub(old_alphas[a], alphas[a])));
		//deltas[a] = OP.maxAll(OP.abs(OP.subNoMem(old_alphas[a], alphas[a])));
	    }
	    maxdelta = DoubleArray.max(deltas);
	    System.out.println("Max delta at iteration " +  iter + " is: "+maxdelta);

	    if (maxdelta <= EPSILON){
		System.out.println("CONVERGED at iteration: " + iter);
		break;
	    }
	} // blind loop
	// return
	return new valueFunctionAdd(alphas, factoredProb.staIds, policy);	     
    } // getBlindAdd

} // blindAdd

