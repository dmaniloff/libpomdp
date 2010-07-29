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
import org.math.array.*;

public class DOSI implements backupHeuristic {

    // pomdp specification
    private pomdp problem;
    
    // constructor
    public DOSI(pomdp prob) {
	this.problem = prob;
    }

    /// heuristic of each orNode = I * \gamma^{D_b}
    public double h_b(orNode o) {
	return o.oneStepDeltaLower * Math.pow(problem.getGamma(), o.depth);
    }

    /// initial computation of the 
    /// reference to best backup candidate
    /// no need for a counterpart with an orNode argument
    public orNode bakStar(andNode a) {
	// form array with bakHeuristic
	double bh[] = new double[problem.getnrObs()];
	for(orNode o : a.children) bh[o.getobs()] = o.bakHeuristic;	
	return a.children[Common.argmax(bh)];

	// STILL TO THINK ABOUT:
	// 	for(int o=0; o<bh.length; o++) {
	// 	    if(a.children[o]!=null) {
	// 		bh[o] = a.children[o].bakHeuristic;	
	// 		System.out.println("its NOT null");
	// 	    } else {
	// 		bh[o] = -1; // set the bak heuristic as that of a fringe node
	// 		System.out.println("its null");
	// 	    }
	// 	}
	// 	System.out.println(DoubleArray.toString(bh));
	// 	return a.children[Common.argmax(bh)];
    } 

    // set bakHeuristic value of this andNode right
    // after its creation
    // in the future this may allow for the introduction of a weighting coeff
    public double bakHStar(andNode a) {
	return a.bakCandidate.bakHeuristicStar * 1; // weight here
    }

    /// compare current bakCandidate with the child's
    /// on the updated branch
    public orNode updateBakStar(andNode a, int o) {
	double challengeH = a.children[o].bakHeuristicStar;
	int argmax = Common.argmax(new double[] {a.bakHeuristicStar, challengeH});
	if(0==argmax) {
	    return a.bakCandidate;
	} else {
	    // update value
	    a.bakHeuristicStar = challengeH;
	    return a.children[o].bakCandidate;
	}
    } 

    // compare this node's bakHeuristic with the child's
    // on the updated branch
    public orNode updateBakStar(orNode o, int a) {
	double challengeH = o.children[a].bakHeuristicStar * 1; // weight here
	int argmax = Common.argmax(new double[] {o.bakHeuristicStar, challengeH}); 
	if(0==argmax) {
	    return o.bakCandidate;
	} else {
	    // update value
	    o.bakHeuristicStar = challengeH;
	    return o.children[a].bakCandidate;
	}
    } // updateBakStar
    
} // DOSI