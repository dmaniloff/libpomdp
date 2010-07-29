/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: AndOrTreeUpdateAdd.java
 * Description: extension of the AndOrTree class to implement
 *              online updates of the offline bounds using ADD's
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.hybrid.java;

// imports
import java.io.*;
import org.math.array.*;

public class AndOrTreeUpdateAdd extends AndOrTree {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    /// work with ADD representation of the pomdp
    pomdpAdd problem;

    /// backup heuristic
    backupHeuristic bakH;

    /// same constructor with backup heuristic
    public AndOrTreeUpdateAdd(pomdp prob, 
			      expandHeuristic h, 
			      backupHeuristic bakh, 
			      valueFunction L, 
			      valueFunction U) {
	super(prob, h, L, U);
	this.problem = (pomdpAdd) super.problem;
	this.bakH    =  bakh;
    }

    /**
     * expand(orNode en):
     * one-step expansion of |A||O| orNodes
     * fully overriden here to access internals and have
     * more control - allows for greater speed
     */
    public void expand(orNode en){
	// make sure this node hasn't been expanded before
	if (en.children != null) { 
	    System.err.println("node cannot be expanded");
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
	    a.init(action, en, problem);
	    // allocate space for the children OR nodes (do we have to do this here?)
	    a.children = new orNode[problem.getnrObs()];
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
		// initialize this node with factored belief, set its poba		
		o.init(problem.tao(en.belief,action,observation), observation, a);
		o.belief.setpoba(pOba[observation]);		
		// compute upper and lower bounds for this node
		o.u = offlineUpper.V(o.belief);		
		o.l = offlineLower.V(o.belief);
		// save one valid plan id for this andNode
		// may be saved multiple times, but it's ok
		a.validPlanid = o.belief.getplanid();
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

	    // L(b,a) = R(b,a) + \gamma \sum_o P(o|b,a)L(tao(b,a,o))
	    a.l = ANDpropagateL(a);
	    a.u = ANDpropagateU(a); 
	    // observation in the path to the next node to expand
	    a.oStar = expH.oStar(a);
	    // H*(b,a)
	    a.hStar = expH.hANDStar(a); 
	    // b*(b,a) - propagate ref of b*
	    a.bStar = a.children[a.oStar].bStar;
	    
	    // the backup candidate and bakheursitic stay as in the
	    // initialization of the andNode since none of its children
	    // can be backed-up given that they are all fringe nodes	    
	    
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
	// one-step best action
	en.oneStepBestAction = currentBestActionAtNode(en);	
	// compute backup heuristic for this newly expanded node
	en.bakHeuristic = bakH.h_b(en); 
	// the backup candidate is still itself and it has its own value as best
	en.bakHeuristicStar = en.bakHeuristic;
	en.bakCandidate     = en;
    } // (overriden) expand


    /**
     * updateAncestors now keeps track of the best
     * candidate node to backup
     */
    public void updateAncestors(orNode n) {
	andNode a;
	orNode  o;
	while(n.hashCode() != this.root.hashCode()) {  
	    // get the AND parent node
	    a = n.getParent();
	    // update the andNode that is parent of n
	    a.l = ANDpropagateL(a);
	    a.u = ANDpropagateU(a);
	    // best obs for expansion
	    a.oStar = expH.oStar(a);
	    // H*(b,a)
	    a.hStar = expH.hANDStar(a);
	    // b*(b,a) - propagate ref of b*
	    a.bStar = a.children[a.oStar].bStar;
	    // propagate reference of backup candidate and its H value
	    a.bakCandidate = bakH.updateBakStar(a, n.getobs());

	    // get the OR parent of the parent
	    o = a.getParent();
	    // update the orNode that is parent of the parent
	    o.l = ORpropagateL(o);
	    o.u = ORpropagateU(o);
	    // H(b,a)
	    o.h_ba = expH.h_ba(o);
	    // best action for expansion
	    o.aStar = expH.aStar(o);
	    // value of best heuristic in the subtree of en
	    o.hStar = o.h_ba[o.aStar] * o.children[o.aStar].hStar;
	    // update reference to best fringe node in the subtree of en
	    o.bStar = o.children[o.aStar].bStar;
	    // update reference of backup candidate and its H value
	    o.bakCandidate = bakH.updateBakStar(o, a.getAct());
	    // increase subtree size by the branching factor |A||O|
	    o.subTreeSize += problem.getnrAct() * problem.getnrObs();
	    // iterate
	    n = n.getParent().getParent();
	}
    } // (overriden) updateAncestors


    /**
     * backup the lower bound at the root node - not used for now
     */
    public double[] backupLowerAtRoot() {
	// decls
	DD gamma  = DDleaf.myNew(problem.getGamma());
	DD gab    = DD.zero;		
	int bestA = currentBestAction(); // consider caching this value maybe
	DD lowerBound [] = ((valueFunctionAdd)offlineLower).getvAdd();
	// \sum_o g_{a,o}^i
	for(orNode o : root.children[bestA].children) {
	    //if(o==null) continue;
	    // compute g_{a,o}^{planid}
	    // problem.gao(lowerBound[o.belief.getplanid()], bestA, o.getobs()).display();
	    gab = OP.add(gab, problem.gao(lowerBound[o.belief.getplanid()], bestA, o.getobs()));
	}    
	// multiply result by discount factor and add it to r_a
	gab = OP.mult(gamma, gab);
	gab = OP.add(problem.R[bestA], gab);
	return OP.convert2array(gab, problem.staIds);

    } // backupLowerAtRoot


    /**
     * backupLowerAtNode:
     * backup the lower bound at the given orNode
     * and update the offline lower bound by adding the
     * new alpha vector to the value function representation
     * Using the current info from the tree, a backup operation is
     * reduced to computing a particular gab vector
     */
    public valueFunction backupLowerAtNode(orNode on) {
	// make sure this node is not in the fringe
	if(null == on.children) {
	    System.err.println("Attempted to backup a fringe node");
	    return null;
	}
	// decls
	DD gamma  = DDleaf.myNew(problem.getGamma());
	DD gab    = DD.zero;		
	//int bestA = currentBestActionAtNode(on); // consider caching this value maybe
	int obs   = 0;
	DD lowerBound [] = ((valueFunctionAdd)offlineLower).getvAdd();
	// \sum_o g_{a,o}^i
	for(orNode o : on.children[on.oneStepBestAction].children) {
	    if(o==null) {
		// in this case we use any valid supporting alpha to compute g_{a,o}^{i}
		gab = OP.add(gab, problem.
			     gao(lowerBound[on.children[on.oneStepBestAction].validPlanid], 
				 on.oneStepBestAction, 
				 obs));
	    } else {
		// compute g_{a,o}^{planid}
		gab = OP.add(gab, problem.
			     gao(lowerBound[o.belief.getplanid()], 
				 on.oneStepBestAction, 
				 obs));
	    }
	    // iterate counter
	    obs++;
	}    
	// multiply result by discount factor and add it to r_a
	gab = OP.mult(gamma, gab);
	gab = OP.add(problem.R[on.oneStepBestAction], gab);
	// add newly computed vector to the tree's offline lower bound - NO PRUNING FOR NOW
	valueFunctionAdd newLB = new valueFunctionAdd(Common.append(lowerBound, gab), 
				    problem.staIds,
				    IntegerArray.merge(offlineLower.getActions(), 
						       new int[] {on.oneStepBestAction}));
	offlineLower = newLB;
	// return 
	return newLB;
	// how about coding a union operation in valueFunction?
	// this function does not watch for repeated vectors yet
    } // backupLowerAtNode


    /**
     * expectedReuse:
     * calculate expected # of belief nodes
     * to reuse given current expanded tree and
     * best action to execute
     */
    public double expectedReuse() {
	int bestA   = currentBestAction();
	double expR = 0;
	for(orNode o : root.children[bestA].children) {
	    // null nodes do not contribute to this sum
	    if(o!=null) expR += o.belief.getpoba() * o.subTreeSize;
	}
	return expR;
    } // expectedReuse

    /// reuse ratio
    /// correct the extra |A||O| in subTreeSize of root node
    public double expectedReuseRatio() {
	return expectedReuse() / 
	    (root.subTreeSize - problem.getnrObs() * problem.getnrAct());
    } // expectedReuseRatio
	
    /// overriden here to print the backupHeuristic of each node
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
	out.println("digraph T {");
	// print node
	orprint(root,out);
	// print closing
	out.println("}");
    }

    /// print orNode
    private void orprint(orNode o, PrintStream out) {
	// print this node
	String b = "";
	if (o.belief.getbPoint().length < 4)
	    b = "b=[" + DoubleArray.toString("%.2f",o.belief.getbPoint()) + "]\\n";
	out.format(o.hashCode() + "[label=\"" +
		   //b +
		   "U(b)= %.2f\\n" +
		   "L(b)= %.2f\\n" + 
		   "expH(b)= %.2f\\n" +
		   "expH*(b)= %.2f\\n" +
		   "bakH(b)= %.2f" +
		   "\"];\n", o.u, o.l, o.h_b, o.hStar, o.bakHeuristic);
	// every or node has a reference to the best node to expand in its subtree
	out.println(o.hashCode() + "->" + o.bStar.hashCode() +
		    "[label=\"b*\",weight=0,color=blue];");
	// every or node has a reference to the best node to backup in its subtree
	//out.println(o.hashCode() + "->" + o.bakCandidate.hashCode() +
	//	    "[label=\"bakCandidate\",weight=0,color=orange];");
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
			   "\"];",
			   o.belief.getpoba(),
			   o.h_bao);
	}
	out.println();

	// every or node has a reference to the best node to backup in its subtree
	out.println(a.hashCode() + "->" + a.bakCandidate.hashCode() +
		    "[label=\"bakCandidate\",weight=0,color=orange];");

	// recurse
	for(orNode o : a.children) if(!(o==null)) orprint(o,out);
    }


} // AndOrTreeUpdateAdd
