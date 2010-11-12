/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: DOSI.java
 * Description: discounted one step improvement heuristic to backup nodes
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.hybrid.java;

// imports
import libpomdp.general.java.Common;
import libpomdp.general.java.pomdp;
import libpomdp.online.java.andNode;
import libpomdp.online.java.orNode;

public class DOSI implements backupHeuristic {

    // pomdp specification
    private pomdp problem;
    
    // constructor
    public DOSI(pomdp prob) {
	this.problem = prob;
    }

    /// heuristic of each orNode = I * \gamma^{D_b}
    public double h_b(orNode o) {
	// think about this, using getdepth() prob makes sense
	// for the root only, which is good for now, but not very general
	// will make this consider the normalized entropy of the belief node as well,
	// probably need to change this class' name as well!
	return o.oneStepDeltaLower * 
		Math.pow(problem.getGamma(), o.getdepth()) *
		o.belief.getEntropy() / Math.log(problem.getnrSta()); 
    }

    /// compare current bakCandidate with the child's
    /// on the updated branch
    /// now takes the index of the alpha vec to know what region we are comparing
    public orNode updateBakStar(andNode a, int o, int i) {
	double challengeH = a.children[o].bakHeuristicStar[i];
	int argmax = Common.argmax(new double[] {a.bakHeuristicStar[i], challengeH});
	if(0==argmax) {
	    return a.bakCandidate[i];
	} else {
	    // update value
	    a.bakHeuristicStar[i] = challengeH;
	    return a.children[o].bakCandidate[i];
	}
    } 

    // compare this node's bakHeuristic with the child's
    // on the updated branch
    public orNode updateBakStar(orNode o, int a, int i) {
	double challengeH = o.children[a].bakHeuristicStar[i] * 1; // weight here
	int argmax = Common.argmax(new double[] {o.bakHeuristicStar[i], challengeH}); 
	if(0==argmax) {
	    return o.bakCandidate[i];
	} else {
	    // update value
	    o.bakHeuristicStar[i] = challengeH;
	    return o.children[a].bakCandidate[i];
	}
    } // updateBakStar
    
} // DOSI