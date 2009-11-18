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
import java.io.*;
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

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// constructor
    public AndOrTree(pomdp prob, heuristic h, valueFunction L, valueFunction U) {
	this.problem = prob;
	this.H = h;
	this.offlineLower = L;
	this.offlineUpper = U;
    }

    /// initializer
    public void init(belState belief) {
	this.root = new orNode();
	this.root.init(belief, -1, null);
	this.root.l = offlineLower.V(this.root.belief);
	this.root.u = offlineUpper.V(this.root.belief);
    }

    /**
     * expand(orNode en):
     * one-step expansion of |A||O| orNodes
     */
    public void expand(orNode en){
	// make sure this node hasn't been expanded before
	if (en.children != null) { 
	    System.err.println("node cannot be expanded");
	    return;
	}
	// iterators
	int action, observation;
	// allocate space for the children AND nodes (do we have to do this here?)
	en.children = new andNode[problem.getnrAct()];
	for(action=0; action<problem.getnrAct(); action++) 
	    en.children[action] = new andNode();
	// iterate through them
	// start actions at zero here
	action = 0;
	for(andNode a : en.children) {
	    // initialize this node, precompute Rba
	    a.init(action,en,problem);
	    // allocate space for the children OR nodes (do we have to do this here?)
	    a.children = new orNode[problem.getnrObs()];
	    for(observation=0; observation<problem.getnrObs(); observation++)
		a.children[observation] = new orNode();
	    // iterate through new fringe nodes
	    // start observations at zero 
	    observation = 0;
	    for (orNode o : a.children) {
		// initialize this node
		// the belief property contains bPoint and poba
		o.init(problem.tao(en.belief,action,observation), observation, a);
		o.l = offlineLower.V(o.belief);
		o.u = offlineUpper.V(o.belief);
		// H(b)
		o.h_b = H.h_b(o);
		// H(b,a,o)	
		o.h_bao = H.h_bao(o);		
		// H*(b) will be H(b) upon creation
		o.hStar = o.h_b;
		// bStar is a reference to itself since o is a fringe node
		o.bStar = o;
		// iterate
		observation++;
	    } // orNode loop
	    
	    // update values in a
	    // L(b,a) = R(b,a) + \gamma \sum_o P(o|b,a)L(tao(b,a,o))
	    a.l = ANDpropagateL(a);
	    a.u = ANDpropagateU(a); 
	    // observation in the path to the next node to expand
	    a.oStar = H.oStar(a);
	    // H*(b,a)
	    a.hStar = H.hANDStar(a); 
	    //a.hStar = a.children[a.bestO].h_bao * a.children[a.bestO].hStar;
	    // b*(b,a) - propagate ref of b*
	    a.bStar = a.children[a.oStar].bStar;
	    // iterate
	    action++;
	}  // andNode loop
	// update values in en
	en.l = ORpropagateL(en);
	en.u = ORpropagateU(en);
	// update H(b)
	en.h_b = H.h_b(en);
	// H(b,a)
	en.h_ba = H.h_ba(en);
	// best action
	// a_b = argmax_a {H(b,a) * H*(b,a)}
	en.aStar = H.aStar(en);
	// value of best heuristic in the subtree of en
	// H*(b) = H(b,a_b) * H*(b,a_b)
	//en.hStar = en.h_ba[en.bestA] * en.children[en.bestA].hStar;
	en.hStar = H.hORStar(en);
	// update reference to best fringe node in the subtree of en
	en.bStar = en.children[en.aStar].bStar;
	// the number of nodes under en increases by |A||O|
	en.subTreeSize += problem.getnrAct() * problem.getnrObs();
	// return
	//return nodes;
    } // expand

    
    /**
     * updateAncestors(orNode n):
     * update the ancestors of a given orNode
     */
    public void updateAncestors(orNode n) {
	andNode a;
	orNode  o;
	// there could be repeated beliefs!!!
	// make sure that using the hashCode here makes sense...
	// this can be optimized..........................................
	//while(!problem.equalB(n.belief,this.root.belief)) {
	while(n.hashCode() != this.root.hashCode()) {  
	    // get the AND parent node
	    a = n.getParent();
	    // update the andNode that is parent of n
	    a.l = ANDpropagateL(a);
	    a.u = ANDpropagateU(a);
	    // best obs
	    a.oStar = H.oStar(a);
	    // H*(b,a)
	    //a.hStar = a.h_o[a.bestO] * a.children[a.bestO].hStar;
	    a.hStar = H.hANDStar(a);
	    // b*(b,a) - propagate ref of b*
	    a.bStar = a.children[a.oStar].bStar;
	    // get the OR parent of the parent
	    o = a.getParent();
	    // update the orNode that is parent of the parent
	    o.l = ORpropagateL(o);
	    o.u = ORpropagateU(o);
	    // H(b,a)
	    o.h_ba = H.h_ba(o);
	    // best action
	    o.aStar = H.aStar(o);
	    // value of best heuristic in the subtree of en
	    o.hStar = o.h_ba[o.aStar] * o.children[o.aStar].hStar;
	    // update reference to best fringe node in the subtree of en
	    o.bStar = o.children[o.aStar].bStar;
	    // this orNode now has a larger subtree underneath
	    //o.subTreeSize += n.subTreeSize;
	    o.subTreeSize += problem.getnrAct() * problem.getnrObs();
	    // iterate
	    n = n.getParent().getParent();
	}
    } // updateAncestors

    /// return dot product of a belief point and a value function - CHANGE THIS!!!!
    //    private double getOfflineLower(orNode o) {
	// double maxV = Double.NEGATIVE_INFINITY;
// 	double dotProd = 0;
// 	for (double alphaV[] : offlineLower.vFlat) {
// 	    // compute dot product
// 	    dotProd = 
// 		DoubleArray.sum(LinearAlgebra.times(alphaV, o.belief.bPoint));
// 	    // keep the max - maybe doing this outside the loop is faster
// 	    maxV = DoubleArray.max(dotProd,maxV);
// 	}
// 	return maxV;
	
	// figure out a way to detect the extending class type
	// for now, assume valueFunctionAdd
	// DD b = ((belStateAdd)o.belief).ddB;
// 	DD l = ((valueFunctionAdd)offlineLower).vAdd;
// 	double dotProds[] = OP.dotProduct(b, l, problem.staIds);
// 	return DoubleArray.max(dotProds);
//	return offlineLower.V(o.belief);
    //   }

    /// return dot product of a belief point and a value function - can we merge these two functions?
    //private double getOfflineUpper(orNode o) {
	// double maxV = Double.NEGATIVE_INFINITY;
// 	double dotProd = 0;
// 	for (double alphaV[] : offlineUpper.vFlat) {
// 	    // compute dot product
// 	    dotProd = 
// 		DoubleArray.sum(LinearAlgebra.times(alphaV, o.belief.bPoint));
// 	    // keep the max
// 	    maxV = DoubleArray.max(dotProd,maxV);
// 	}
// 	return maxV;
//	return offlineUpper.V(o.belief);
    //  }

    /// L(b,a) = R(b,a) + \gamma \sum_o P(o|b,a) L(tao(b,a,o))
    private double ANDpropagateL(andNode a) {
	double Lba = 0;
	for(orNode o : a.children) {
	    Lba += o.belief.poba * o.l;
	}
	//return problem.Rba(a.getParent().belief,a.getAct()) + problem.getGamma() * Lba;
	return a.rba + problem.getGamma() * Lba;
    }

    /// U(b,a) = R(b,a) + \gamma\sum P(o|b,a) U(tao(b,a,o))
    private double ANDpropagateU(andNode a) {
	//int o;
	double Uba = 0;
	//for(o = 0; o < problem.getnrObs(); o++) {
	for(orNode o : a.children) {
	    //Uba += problem.P_oba(o,a.getParent().belief,a.getact()) * a.children[o].u;
	    //Uba += problem.P_oba(o.getobs(),a.getParent().belief,a.getact()) * o.u;
	    //Uba += a.poba[o.getobs()] * o.u;
	    Uba += o.belief.poba * o.u;
	}
	// consider storing rba to avoid re-computing it, especially during updateancestors
	//return problem.Rba(a.getParent().belief,a.getAct()) + problem.getGamma() * Uba;
	return a.rba + problem.getGamma() * Uba;
    }

    /// L(b) = max{max_a L(b,a),L(b)}
    private double ORpropagateL(orNode o) {
	// form array of doubles
	//	double L[] = new double[problem.getnrAct()];
	double maxLba = Double.NEGATIVE_INFINITY;
	for(andNode a : o.children) {
	    //  L[a.getAct()] = a.l;
	    if(a.l > maxLba) maxLba = a.l;
	}
	// compare to current bound
	if(maxLba > o.l)
	    return maxLba;
	else
	    return o.l;
	//	return DoubleArray.max(L);
	// double maxLba = Double.NEGATIVE_INFINITY;
	// 	for(andNode a : o.children) {
	// 	    maxLba = DoubleArray.max(maxLba,a.l);
	// 	}       
	//return DoubleArray.max(maxLba,o.l);	
    }

    /// U(b) = min{max_a U(b,a),U(b)}
    private double ORpropagateU(orNode o) {
	// form array of doubles
	//	double U[] = new double[problem.getnrAct()];
	double maxUba = Double.NEGATIVE_INFINITY;
	for(andNode a : o.children) {
	    //U[a.getAct()] = a.u;
	    if(a.u > maxUba) maxUba = a.u;
	}
	// compare to current bound
	if (maxUba < o.u)
	    return maxUba;
	else
	    return o.u;
	//	return DoubleArray.max(U);
	// 	for(andNode a : o.children) {
	// 	    maxUba = DoubleArray.max(maxUba,a.u);
	// 	}	
	// is this correct?	
	//return DoubleArray.min(maxUba,o.u);
    }

    public orNode getRoot() {
	return root;
    }

    /// if I understood the gc's behaviour
    /// correctly, moving the root of the tree
    /// and setting the new root's parent to null
    /// automagically deletes all the useless subtrees
    public void moveTree(orNode newroot) {
	this.root = newroot;
	//BUG HERE
	//this.root.init(newroot.belief, -1, null);
	this.root.disconnect();
    }
    

    /// return best action given the current state
    /// of expansion in the tree
    public int currentBestAction() {
	// construct array with L(b,a)
	double Lba[] = new double[problem.getnrAct()];
	for(andNode a : root.children)
	    Lba[a.getAct()]=a.l;
	return ((aems2)H).argmax(Lba);
    }

    /* 
     * /// free or node's parent
     * private void orfreeE(orNode o, int excepted) {
     * 	// clear refs to parent andNode
     * 	o.parent = null;
     * 	// make sure this is not the excepted node
     * 	if (o.hashCode() == excepted) return;
     * 	// call andfree on children
     * 	for(andNode a : o.children)
     * 	    andfreeE(a, excepted);
     * }
     * 
     * /// free and node's parent
     * private void andfreeE(andNode a, int excepted) {
     * 	// clear ref to parent orNode
     * 	a.parent = null;
     * 	// call orfree on children
     * 	for(orNode o : a.children)
     * 	    orfreeE(o, excepted);
     * }
     */

    /// output a dot-formatted file to print the tree
    /// starting from a given orNode
    public void printdot(String filename) {
	orNode root = this.root;
	PrintStream out = null;
	try {
	    out = new 
		PrintStream(filename);
	}catch(Exception e) {
	    System.err.println(e.toString());
	}
	//out = System.out;
	// print file headers
	out.println("strict digraph T {");
	// print node
	orprint(root,out);
	// print closing
	out.println("}");
    }

    /// print orNode
    private void orprint(orNode o, PrintStream out) {
	// print this node
	out.format(o.hashCode() + "[label=\"" +
		   //"b=[" + DoubleArray.toString("%.2f",o.belief.bPoint) + "]\\n" +
		   "U(b)= %.2f\\n" +
		   "L(b)= %.2f\\n" + 
		   "H(b)= %.2f" +
		   "\"];\n", o.u, o.l, o.h_b);
	// every or node has a reference to be best node in its subtree
	out.println(o.hashCode() + "->" + o.bStar.hashCode() +
		    "[label=\"b*\",weight=0,color=blue];");
	// check it's not in the fringe before calling andprint
	if (o.children == null) return;	
	// print outgoing edges from this node
	for(andNode a : o.children) {
	    out.print(o.hashCode() + "->" + a.hashCode() +
		      "[label=\"" + 
		      "H(b,a)=" + o.h_ba[a.getAct()] + 
		      "\"];");
	}
	out.println();
	// recurse
	for(andNode a : o.children) andprint(a, out);
    }

    /// print andNode
    private void andprint(andNode a, PrintStream out) {
	// print this node
	out.format(a.hashCode() + "[label=\"" + 
		   "a=" + problem.getactStr(a.getAct()) + "\\n" + 	
		   "U(b,a)= %.2f\\n" +
		   "L(b,a)= %.2f" +
		   "\"];\n", a.u, a.l);
	// print outgoing edges for this node
	for(orNode o : a.children) {
	    out.format(a.hashCode() + "->" + o.hashCode() + 
		       "[label=\"" +
		       "obs: " + problem.getobsStr(o.getobs()) + "\\n" +
		       "P(o|b,a)= %.2f\\n" + 
		       "H(b,a,o)= %.2f" +  
		       "\"];\n",
		       //problem.P_oba(o.getobs(), a.getParent().belief,a.getact()),
		       //a.poba[o.getobs()],
		       o.belief.poba,
		       o.h_bao);
	}
	out.println();
	// recurse
	for(orNode o : a.children) orprint(o,out);
    }
}     // AndOrTree