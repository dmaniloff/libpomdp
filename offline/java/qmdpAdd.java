/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: qmdpAdd.java
 * Description: compute offline qmdp upper bound using Adds as representation
 *              see README reference [6]
 * Copyright (c) 2010, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public class qmdpAdd {

    // parameters
    final int MAXITERATIONS = 500;
    final double EPSILON    = 1e-5;

    public valueFunctionAdd getqmdpAdd(pomdpAdd factoredProb) {

	// decls
	int a, iter;
	DD[] adds;
	double maxdelta;

	// allocate alpha vectors
	DD Vqmdp[]  = new DD[factoredProb.getnrAct()];
	DD Vmdp     = DD.zero;
	DD old_Vmdp = DD.zero;

	// allocate policy - one vec per action
	int policy[]    = new int [factoredProb.getnrAct()];
	DD ddDiscFact   = DDleaf.myNew(factoredProb.getGamma());

	for(iter=0; iter<MAXITERATIONS; iter++) {	

	    // copy Vmdp
	    //System.arraycopy(Vmdp, 0, old_Vmdp, 0, 1);
	    old_Vmdp = Vmdp;	//  why does this work?

	    // prime vars forward
	    Vmdp = OP.primeVars(Vmdp, factoredProb.getnrTotV());

	    for(a=0; a<factoredProb.getnrAct(); a++) {
		// concat all ADDs into one array        
		adds                = new DD[1+factoredProb.T[a].length+1];
		adds[0]             = ddDiscFact;
		System.arraycopy(factoredProb.T[a], 0, adds, 1, factoredProb.T[a].length);
		adds[adds.length-1] = Vmdp;		
		// Vmdp = \max_a {R(s,a) + \gamma \sum_{s'} T(s,a,s') Vmdp(s')}
		//Vqmdp[a]            = OP.addMultVarElim(adds, factoredProb.staIdsPr);
		Vqmdp[a]            = OP.addMultVarElimNoMem(adds, factoredProb.staIdsPr);
		//		Vqmdp[a]            = OP.add(factoredProb.R[a], Vqmdp[a]);
		Vqmdp[a]            = OP.addNoMem(factoredProb.R[a], Vqmdp[a]);
	    }
	    // compute max_a
	    Vmdp = OP.maxN(Vqmdp);

	    // convergence check
	    //maxdelta = OP.maxAll(OP.abs(OP.sub(Vmdp, old_Vmdp)));
	    maxdelta = OP.maxAll(OP.abs(OP.subNoMem(Vmdp, old_Vmdp)));
	    System.out.println("Max delta at iteration " +  iter + " is: "+maxdelta);
	    if (maxdelta <= EPSILON){
		System.out.println("CONVERGED at iteration: " + iter);
		break;
	    }
	} // qmdp loop

	return new valueFunctionAdd(Vqmdp, factoredProb.staIds, policy);

    } // getqmdpAdd

} // qmdpAdd


