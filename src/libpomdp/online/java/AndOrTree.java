/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: AndOrTree.java
 * Description: data structure to hold the AND-OR tree for online search
 *              The constructor takes a heuristic object H that enables the
 *              implementation of different heuristic search methods
 * Copyright (c) 2009, 2010 Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.online.java;

// imports
import libpomdp.general.java.*;
import java.util.*;
import java.io.*;
import org.math.array.*;

public class AndOrTree {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // pomdp problem specification
    protected pomdp problem;

    // expansion heursitic 
    protected expandHeuristic expH;
    
    // offline computed bounds
    protected valueFunction offlineLower;
    protected valueFunction offlineUpper;

    // root of the tree
    protected orNode root;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// constructor
    public AndOrTree(pomdp prob, expandHeuristic h, valueFunction L, valueFunction U) {
	this.problem = prob;
	this.expH = h;
	this.offlineLower = L;
	this.offlineUpper = U;
    }

    /// initializer
    public void init(belState belief) {
	this.root = new orNode();
	this.root.init(belief, -1, null);
	this.root.u = offlineUpper.V(this.root.belief);
	this.root.l = offlineLower.V(this.root.belief);
	// should have separate plan ids to avoid this!
    }

    /**
     * expand(orNode en):
     * one-step expansion of |A||O| orNodes
     */
    public void expand(orNode en){
	// make sure this node hasn't been expanded before
	if (en.children != null) { 
	    System.err.println("node not on fringe");
	    return;
	}
	// iterators
	int action, observation;
	// poba vector for each action
	double pOba[];
	// save this node's old bounds
 	double old_l = en.l;
	double old_u = en.u;	
	// allocate space for the children AND nodes (do we have to do this here?)
	en.children = new andNode[problem.getnrAct()];
	for(action=0; action<problem.getnrAct(); action++) 
	    en.children[action] = new andNode();
	// iterate through them
	// start actions at zero here
	action = 0;
	for(andNode a : en.children) {
	    // initialize this node, precompute Rba
	    a.init(action, en, problem.Rba(en.belief, action));
	    // allocate space for the children OR nodes (do we have to do this here?)
	    // could prob do both these operations as part of init
	    a.children = new orNode[problem.getnrObs()];
	    //a.subTreeSize = problem.getnrObs();
	    for(observation=0; observation<problem.getnrObs(); observation++)
		a.children[observation] = new orNode();
	    // pre-compute observation probabilities
	    pOba = problem.P_Oba(en.belief, action);
	    // iterate through new fringe nodes
	    // start observations at zero 
	    observation = 0;
	    for (orNode o : a.children) {
		// ZERO-PROB OBSERVATIONS:
		// here we should continue the loop and avoid re-computing V^L and V^U
		// for belief nodes with poba == 0              
		if (pOba[observation] == 0) {
		    a.children[observation] = null;
		    observation++;
		    continue;
		} 
		// initialize this node, set its poba
		o.init(problem.tao(en.belief,action,observation), observation, a);
		o.belief.setpoba(pOba[observation]);
		// compute upper and lower bounds for this node
		o.u = offlineUpper.V(o.belief);
		o.l = offlineLower.V(o.belief);		
		// H(b)
		o.h_b = expH.h_b(o);
		// H(b,a,o)	
		o.h_bao = expH.h_bao(o);		
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
	    a.oStar = expH.oStar(a);
	    // H*(b,a)
	    a.hStar = expH.hANDStar(a); 
	    // b*(b,a) - propagate ref of b*
	    a.bStar = a.children[a.oStar].bStar;
	    // iterate
	    action++;
	}  // andNode loop

	// update values in en
	en.l = ORpropagateL(en);
	en.u = ORpropagateU(en);
	// update H(b)
	en.h_b = expH.h_b(en);
	// H(b,a)
	en.h_ba = expH.h_ba(en);
	// best action
	// a_b = argmax_a {H(b,a) * H*(b,a)}
	en.aStar = expH.aStar(en);
	// value of best heuristic in the subtree of en
	// H*(b) = H(b,a_b) * H*(b,a_b)	
	en.hStar = expH.hORStar(en);
	// update reference to best fringe node in the subtree of en
	en.bStar = en.children[en.aStar].bStar;
	// the number of nodes under en increases by |A||O|
	en.subTreeSize += problem.getnrAct() * problem.getnrObs();
	// one-step improvement
	en.oneStepDeltaLower = en.l - old_l;
	en.oneStepDeltaUpper = en.u - old_u;
	if(en.oneStepDeltaLower < 0) {
	    System.err.println("Hmmmmmmmmmmm");
	}
    } // expand

    
    /**
     * updateAncestors(orNode n):
     * update the ancestors of a given orNode
     */
    public void updateAncestors(orNode n) {
	// make sure this is not the call after expanding the root
	if (null == n.children) return;
	andNode a;
	orNode  o;
	while(n.hashCode() != this.root.hashCode()) {  
	    // get the AND parent node
	    a = n.getParent();
	    // update the andNode that is parent of n
	    a.l = ANDpropagateL(a);
	    a.u = ANDpropagateU(a);
	    // best obs
	    a.oStar = expH.oStar(a);
	    // H*(b,a)
	    a.hStar = expH.hANDStar(a);
	    // b*(b,a) - propagate ref of b*
	    a.bStar = a.children[a.oStar].bStar;

	    // get the OR parent of the parent
	    o = a.getParent();
	    // update the orNode that is parent of the parent
	    o.l = ORpropagateL(o);
	    o.u = ORpropagateU(o);
	    // H(b,a)
	    o.h_ba = expH.h_ba(o);
	    // best action
	    o.aStar = expH.aStar(o);
	    // value of best heuristic in the subtree of en
	    o.hStar = o.h_ba[o.aStar] * o.children[o.aStar].hStar;
	    // update reference to best fringe node in the subtree of en
	    o.bStar = o.children[o.aStar].bStar;
	    // increase subtree size by the branching factor |A||O|
	    o.subTreeSize += problem.getnrAct() * problem.getnrObs();
	    // iterate
	    n = o;
	}
    } // updateAncestors

    /**
     * updateAncestorsPath:
     * improved version that updates only along
     * the path that was modified
     * STILL IN DEVELOPMENT - NOT YET IN USE
     */
    public void updateAncestorsPath(orNode n) {
	// decls
	double  improvLower;
	double  improvUpper;	
	//double lbanew;
	//double ubanew;
	orNode  o;
	andNode a;	
	boolean updateL = true;
	boolean updateU = true;
	// n will be a node that has just been expanded
	// therefore its improvement will just be oneStepDelta
	improvLower = n.oneStepDeltaLower;
	improvUpper = n.oneStepDeltaUpper;
	// first check
	if(0 >= improvLower) updateL = false;
	if(0 >= improvUpper) updateU = false;	    
	// update loop
	while(n.hashCode() != this.root.hashCode() /*&& (updateL || updateU)*/) {  
	    // get AND parent node
	    a = n.getParent();
	    // AND break
	    if (updateL) a.l  += problem.getGamma() * n.belief.getpoba() * improvLower;
	    if (updateU) a.u  += problem.getGamma() * n.belief.getpoba() * improvUpper; //  ???????????
	    // update best obs - this becomes a question of whether to continue pointing in the same
	    // direction or not given the change in certainty along this path/branch
	    //a.oStar = expH.oStarUpdate(a, n.getobs());
	    a.oStar = expH.oStar(a);
	    // H*(b,a)
	    a.hStar = expH.hANDStar(a);
	    // b*(b,a) - propagate ref of b*
	    a.bStar = a.children[a.oStar].bStar;	    

	    // get OR parent node
	    o = a.getParent();
	    // OR break 
	    // update bounds - same way as comparison against best current
	    if (o.l >= a.l) {		
		updateL = false;
	    } else {
		o.l = a.l;
		improvLower = a.l - o.l;
	    }
	    if (o.u <= a.u) {
		updateU = false;
	    } else {
		o.u = a.u;
		improvUpper = o.u - a.u;
	    }
	    // H(b,a)
	    o.h_ba = expH.h_baUpdate(o, a.getAct());
	    // best action
	    o.aStar = expH.aStar(o);
	    // value of best heuristic in the subtree of en
	    o.hStar = o.h_ba[o.aStar] * o.children[o.aStar].hStar;
	    // update reference to best fringe node in the subtree of en
	    o.bStar = o.children[o.aStar].bStar;
	    // increase subtree size by the branching factor |A||O|
	    o.subTreeSize += problem.getnrAct() * problem.getnrObs();
	    // iterate
	    n = o;
	}
    } // updateAncestorsPath

    /// L(b,a) = R(b,a) + \gamma \sum_o P(o|b,a) L(tao(b,a,o))
    protected double ANDpropagateL(andNode a) {
	double Lba = 0;
	for(orNode o : a.children) {
	    // o.belief.getpoba() == 0 for null orNodes anyway
	    if(o != null) Lba += o.belief.getpoba() * o.l;
	}
	return a.rba + problem.getGamma() * Lba;
    }

    /// U(b,a) = R(b,a) + \gamma \sum_o P(o|b,a) U(tao(b,a,o))
    protected double ANDpropagateU(andNode a) {
	double Uba = 0;
	for(orNode o : a.children) {
	    // o.belief.getpoba() == 0 for null orNodes anyway
	    if(o != null) Uba += o.belief.getpoba() * o.u;
	}
	return a.rba + problem.getGamma() * Uba;
    }

    /// L(b) = max{max_a L(b,a),L(b)}
    protected double ORpropagateL(orNode o) {
	double maxLba = Double.NEGATIVE_INFINITY;
	for(andNode a : o.children) {
	    if(a.l > maxLba) maxLba = a.l;
	}
	// compare to current bound
	return Math.max(maxLba, o.l);
    }

    /// U(b) = min{max_a U(b,a),U(b)}
    protected double ORpropagateU(orNode o) {
	double maxUba = Double.NEGATIVE_INFINITY;
	for(andNode a : o.children) {
	    if(a.u > maxUba) maxUba = a.u;
	}
	// compare to current bound
	return Math.min(maxUba, o.u);
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
	this.root.disconnect();
    }    

    /// return best action given the current state
    /// of expansion in the whole tree
    /// this function is randomized!!!!
    public int currentBestAction() {
	// construct array with L(b,a)
	double Lba[] = new double[problem.getnrAct()];
	for(andNode a : root.children)
	    Lba[a.getAct()] = a.l;
	return Common.argmax(Lba);
    }
    
    /// check if an action is epsilon-optimal given the current
    /// state of the search tree - this assumes act was previously 
    /// obtained from currentBestAction....
    /// there' probably a better way to do this, maybe by maintaining
    /// the best action for every belief node in the tree and updating it
    public boolean actionIsEpsOptimal(int act, double OPT_EPSILON) {
	// first condition
	if (Math.abs(root.u - root.l) < OPT_EPSILON) return true;
	// second condition
	boolean opt1 = true;
	for(andNode a : root.children) {
	    if ((a.getAct() != act) && (root.l < a.u)) {
		opt1 = false;
		break;
	    }
	}
	return opt1;
    }
    
    
    // DO NOT USE
//    public boolean currentBestActionIsOptimal(double OPT_EPSILON) {
//	boolean opt1 = true;
//	boolean opt2 = false;
//	int bestA = currentBestAction();
//	for(andNode a : root.children) {
//	    if ((root.l < a.u) && (a.getAct() != bestA)) { // BUGG
//		opt1 = false;
//		break;
//	    }
//	}
//	if (Math.abs(root.u - root.l) < OPT_EPSILON)
//	    opt2 = true;
//	// either condition is enough
//	return opt1 || opt2;
//    }


    /// return best action according to the subtree
    /// rooted at on
    /// this function is randomized!!!!
//    public int currentBestActionAtNode(orNode on) {
//	// construct array with L(b,a)
//	double Lba[] = new double[problem.getnrAct()];
//	for(andNode a : on.children)
//	    Lba[a.getAct()] = a.l;
//	return Common.argmax(Lba);
//    }

    public valueFunction getLB() {
	return offlineLower;
    }

    public valueFunction getUB() {
	return offlineUpper;
    }

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
	String b = "";
	b = "b=[\\n " + 
	    DoubleArray.toString("%.2f",o.belief.getbPoint()) + 
	    "]\\n";
	out.format(o.hashCode() + "[label=\"" +
		   //b +
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
    protected void andprint(andNode a, PrintStream out) {
	// print this node
	out.format(a.hashCode() + "[label=\"" + 
		   "a=" + problem.getactStr(a.getAct()) + "\\n" + 	
		   "U(b,a)= %.2f\\n" +
		   "L(b,a)= %.2f" +
		   "\"];\n", a.u, a.l);
	// print outgoing edges for this node
	for(orNode o : a.children) {
	    if (!(o==null))		
		out.format(a.hashCode() + "->" + o.hashCode() + 
			   "[label=\"" +
			   "obs: " + problem.getobsStr(o.getobs()) + "\\n" +
			   "P(o|b,a)= %.2f\\n" + 
			   "H(b,a,o)= %.2f" +  
			   "\"];\n",
			   o.belief.getpoba(),
			   o.h_bao);
	}
	out.println();
	// recurse
	for(orNode o : a.children) if(!(o==null)) orprint(o,out);
    }

} // AndOrTree