/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: AndOrTree.java
 * Description: data structure to hold the AND-OR tree for online search
 *              The constructor takes a heuristic object H that enables the
 *              implementation of different heuristic search methods
 * Copyright (c) 2009, Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import java.util.*;
import org.math.array.*;

public class AndOrTree {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // pomdp problem specification
    private pomdp problem;

    // heursitic to use
    private heuristic H;
    
    // offline computed bounds
    private valueFunction offlineLower;
    private valueFunction offlineUpper;

    // root of the tree
    private orNode root;

    // fringe of the tree - do we need one??
    private PriorityQueue<orNode> fringe;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// constructor
    public AndOrTree(pomdp prob, /* heuristic h, */ valueFunction L, valueFunction U) {
	this.problem = prob;
	//this.H = h;
	this.offlineLower = L;
	this.offlineUpper = U;
    }

    /// initializer
    public void initialize(orNode root) {
	this.root = root;
	//this.fringe = new PriorityQueue<orNode>();
 	//this.fringe.add(root);
    }

    // expand routine 
    public List<orNode> expand(orNode en){
	// allocate return list
	List<orNode> nodes = new ArrayList<orNode>();

	// allocate space for the children AND nodes (do we have to do this here?)
	en.children = new andNode[problem.getnrAct()];
	// iterate through them
	int action = 1;
	for(andNode a : en.children) {
	    // create new AND node
	    a = new andNode(action,en);
	    //  allocate space for the children OR nodes (do we have to do this here?)
	    a.children = new orNode[problem.getnrObs()];
	    // iterate through new fringe nodes
	    int observation = 1;
	    for (orNode o : a.children) {
		o = new orNode(problem.tao(en.belief,a.act,observation),observation,a);
		o.l = getOfflineLower(o);
		//o.u = getOfflineUpper(o);
		//o.h = hOR(o); // H(b)
		//o.h_o = hOR_o(o); // H(b,a,o)
		//o.hStar = this.H.hOR(o);
		// add newly created nodes to return list
		nodes.add(o);
		// iterate
		observation++;
	    }
	    // update values in a
	    a.l = ANDpropagateL(en.belief, a);
	    //a.u = ANDpropagateU(en.belief, a); 
	    //a.bestO = this.H.bestO(a);
	    //a.h = hAND(a); // H(b,a)
	    // iterate
	    action++;
	}  
	// update values in en
	en.l = ORpropagateL(en);
	//en.u = ORpropagateU(en);
	// return
	return nodes;
    } // expand

    // return dot product of a belief point and a value function
    private double getOfflineLower(orNode o) {
	double maxV = Double.NEGATIVE_INFINITY;
	double dotProd = 0;
	for (double[] alphaV : offlineLower.v) {
	    // compute dot product
	    dotProd = DoubleArray.sum(LinearAlgebra.times(alphaV, o.belief));
	    // keep the max - maybe doing this outside the loop is faster
	    maxV = DoubleArray.max(dotProd,maxV);
	}
	return maxV;
    }

    // return dot product of a belief point and a value function - can we merge these two functions?
    private double getOfflineUpper(orNode o) {
	double maxV = Double.NEGATIVE_INFINITY;
	double dotProd = 0;
	for (double[] alphaV : offlineUpper.v) {
	    // compute dot product
	    dotProd = DoubleArray.sum(LinearAlgebra.times(alphaV, o.belief));
	    // keep the max
	    maxV = DoubleArray.max(dotProd,maxV);
	}
	return maxV;
    }

    /// L(b,a) = R(b,a) + \gamma \sum_o P(o|b,a) L(tao(b,a,o))
    private double ANDpropagateL(double b[], andNode a) {
	int o;
	double Lba = 0;
	for(o = 0; o < problem.getnrObs(); o++) {
	    Lba += problem.P_oba(o,b,a.act) * a.children[o].l;
	}
	return problem.Rba(b,a.act) + problem.getGamma() * Lba;
    }

    /// U(b,a) = R(b,a) + \gamma\sum P(o|b,a) U(tao(b,a,o))
    private double ANDpropagateU(double b[], andNode a) {
	int o;
	double Uba = 0;
	for(o = 0; o < problem.getnrObs(); o++) {
	    Uba += problem.P_oba(o,b,a.act) * a.children[o].u;
	}
	return problem.Rba(b,a.act) + problem.getGamma() * Uba;
    }

    /// L(b) = max{max_a L(b,a),L(b)}
    private double ORpropagateL(orNode o) {
	double maxLba = Double.NEGATIVE_INFINITY;
	for(andNode a : o.children) {
	    maxLba = DoubleArray.max(maxLba,a.l);
	}
	return DoubleArray.max(maxLba,o.l);
    }

    /// U(b) = min{max_a U(b,a),U(b)}
    private double ORpropagateU(orNode o) {
	double maxUba = Double.NEGATIVE_INFINITY;
	for(andNode a : o.children) {
	    maxUba = DoubleArray.max(maxUba,a.u);
	}
	return DoubleArray.min(maxUba,o.u);
    }

}     // AndOrTree