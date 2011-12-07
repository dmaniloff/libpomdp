/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: qmdpAdd.java
 * Description: compute offline qmdp upper bound using Adds as representation
 *              see README reference [6]
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.solve.offline.bounds;

// imports
import libpomdp.common.add.PomdpAdd;
import libpomdp.common.add.ValueFunctionAdd;
import libpomdp.common.add.symbolic.DD;
import libpomdp.common.add.symbolic.DDleaf;
import libpomdp.common.add.symbolic.OP;

public class QmdpAdd {

    // parameters
    final int MAXITERATIONS = 500;
    final double EPSILON    = 1e-5;
    PomdpAdd factoredProb;

    public QmdpAdd(PomdpAdd problem) {
        this.factoredProb = problem;
    }

    public ValueFunctionAdd getValueFunction() {

	// decls
	DD[] adds;
	double maxdelta;

	// allocate alpha vectors
	DD Vqmdp[]  = new DD[factoredProb.nrActions()];
	DD Vmdp     = DD.zero;
	DD old_Vmdp = DD.zero;

	// allocate policy - one vec per action
	int policy[]    = new int [factoredProb.nrActions()]; 
	for (int a=0; a<factoredProb.nrActions(); a++) policy[a] = a;

	DD ddDiscFact   = DDleaf.myNew(factoredProb.getGamma());

	for(int iter=0; iter<MAXITERATIONS; iter++) {	

	    // copy Vmdp
	    old_Vmdp = Vmdp;	//  why does this work?

	    // prime vars forward
	    Vmdp = OP.primeVars(Vmdp, factoredProb.getnrTotV());

	    for(int a=0; a<factoredProb.nrActions(); a++) {
		// concat all ADDs into one array        
		adds                = new DD[1+factoredProb.T[a].length+1];
		adds[0]             = ddDiscFact;
		System.arraycopy(factoredProb.T[a], 0, adds, 1, factoredProb.T[a].length);
		adds[adds.length-1] = Vmdp;	
		// Vmdp = \max_a {R(s,a) + \gamma \sum_{s'} T(s,a,s') Vmdp(s')}		
		Vqmdp[a]            = OP.addMultVarElim(adds, factoredProb.getstaIdsPr());
		Vqmdp[a]            = OP.add(factoredProb.R[a], Vqmdp[a]);
	    }

	    // compute max_a
	    Vmdp = OP.maxN(Vqmdp);

	    // convergence check
	    maxdelta = OP.maxAll(OP.abs(OP.sub(Vmdp, old_Vmdp)));	    
	    System.out.println("Max delta at iteration " +  iter + " is: "+maxdelta);

	    if (maxdelta <= EPSILON){
		System.out.println("CONVERGED at iteration: " + iter);
		break;
	    }
	} // qmdp loop

	// return
	return new ValueFunctionAdd(Vqmdp, factoredProb.getstaIds(), policy);

    } // getqmdpAdd

} // qmdpAdd


