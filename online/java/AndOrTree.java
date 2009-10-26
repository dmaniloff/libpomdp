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
    // prio queue or simple list
    //private PriorityQueue<orNode> fringe;
    private List<orNode> fringe;

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
    public void initialize(double belief[]) {
	this.root = new orNode();
	this.root.init(belief, -1, null);
	this.root.l = getOfflineLower(this.root);
	this.root.u = getOfflineUpper(this.root);
	//this.fringe = new PriorityQueue<orNode>();
	this.fringe = new ArrayList<orNode>();
 	// add root node to the fringe
	this.fringe.add(root);
    }

    /// expand routine - return a |A||O| list of orNode using Generics
    public List<orNode> expand(orNode en){
	// should add a check here to make sure en is in the fringe

	// allocate return list
	List<orNode> nodes = new ArrayList<orNode>();
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
	    // initialize this node
	    a.init(action,en);
	    // allocate space for the children OR nodes (do we have to do this here?)
	    a.children = new orNode[problem.getnrObs()];
	    for(observation=0; observation<problem.getnrObs(); observation++)
		a.children[observation] = new orNode();
	    // iterate through new fringe nodes
	    // start observations at zero 
	    observation = 0;
	    for (orNode o : a.children) {
		// initialize this node
		o.init(problem.tao(en.belief,action,observation), observation, a);
		o.l = getOfflineLower(o);
		o.u = getOfflineUpper(o);
		// H(b)
		o.h = H.hOR(o);			
		// H*(b)
		o.hStar = o.h;
		// add node to the fringe ??
		fringe.add(o);
		// get as bStar the index to itself
		//o.bStar = fringe.size() - 1;
		// bStar is a reference to itself since o is a fringe node
		o.bStar = o;
		// add newly created nodes to return list
		nodes.add(o);
		// iterate
		observation++;
	    }
	    // H(b,a,o)
	    a.h_o = H.hAND_o(a); 
	    // update values in a - consider using the already
	    // computed P_Oba if necessary
	    // L(b,a) = R(b,a) + \gamma \sum_o P(o|b,a)L(tao(b,a,o))
	    a.l = ANDpropagateL(a);
	    a.u = ANDpropagateU(a); 
	    // best observation
	    a.bestO = H.bestO(a);
	    // H*(b,a)
	    //a.hStar = H.hANDStar(a); 
	    a.hStar = a.h_o[a.bestO] * a.children[a.bestO].hStar;
	    // b*(b,a) - propagate ref of b*
	    a.bStar = a.children[a.bestO].bStar;
	    // iterate
	    action++;
	}  
	// update values in en
	en.l = ORpropagateL(en);
	en.u = ORpropagateU(en);
	// H(b,a)
	en.h_a = H.hOR_a(en);
	// best action
	en.bestA = H.bestA(en);
	// value of best heuristic in the subtree of en
	en.hStar = en.h_a[en.bestA] * en.children[en.bestA].hStar;
	// update reference to best fringe node in the subtree of en
	en.bStar = en.children[en.bestA].bStar;
	// return
	return nodes;
    } // expand

    
    /// update the ancestors of a given orNode
    public void updateAncestors(orNode n) {
	while(!problem.equalB(n.belief,this.root.belief)) {
	    // update the andNode that is parent of n
	    n.getParent().l = ANDpropagateL(n.getParent());
	    n.getParent().u = ANDpropagateU(n.getParent());
	    // update the orNode that is parent of the parent
	    n.getParent().getParent().l = 
		ORpropagateL(n.getParent().getParent());
	    n.getParent().getParent().u = 
		ORpropagateU(n.getParent().getParent());
	    // iterate
	    n = n.getParent().getParent();
	}
    } // updateAncestors

    /// return dot product of a belief point and a value function
    private double getOfflineLower(orNode o) {
	double maxV = Double.NEGATIVE_INFINITY;
	double dotProd = 0;
	for (double alphaV[] : offlineLower.v) {
	    // compute dot product
	    dotProd = DoubleArray.sum(LinearAlgebra.times(alphaV, o.belief));
	    // keep the max - maybe doing this outside the loop is faster
	    maxV = DoubleArray.max(dotProd,maxV);
	}
	return maxV;
    }

    /// return dot product of a belief point and a value function - can we merge these two functions?
    private double getOfflineUpper(orNode o) {
	double maxV = Double.NEGATIVE_INFINITY;
	double dotProd = 0;
	for (double alphaV[] : offlineUpper.v) {
	    // compute dot product
	    dotProd = DoubleArray.sum(LinearAlgebra.times(alphaV, o.belief));
	    // keep the max
	    maxV = DoubleArray.max(dotProd,maxV);
	}
	return maxV;
    }

    /// L(b,a) = R(b,a) + \gamma \sum_o P(o|b,a) L(tao(b,a,o))
    private double ANDpropagateL(andNode a) {
	//int o;
	double Lba = 0;
	//for(o = 0; o < problem.getnrObs(); o++) {
	for(orNode o : a.children) {
	    // how about storing P(o|b,a) at init time??
	    //Lba += problem.P_oba(o,a.getParent().belief,a.getact()) * a.children[o].l;
	    Lba += problem.P_oba(o.getobs(),a.getParent().belief,a.getact()) * o.l;
	}
	return problem.Rba(a.getParent().belief,a.getact()) + problem.getGamma() * Lba;
    }

    /// U(b,a) = R(b,a) + \gamma\sum P(o|b,a) U(tao(b,a,o))
    private double ANDpropagateU(andNode a) {
	//int o;
	double Uba = 0;
	//for(o = 0; o < problem.getnrObs(); o++) {
	for(orNode o : a.children) {
	    //Uba += problem.P_oba(o,a.getParent().belief,a.getact()) * a.children[o].u;
	    Uba += problem.P_oba(o.getobs(),a.getParent().belief,a.getact()) * o.u;
	}
	return problem.Rba(a.getParent().belief,a.getact()) + problem.getGamma() * Uba;
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

    public orNode getroot() {
	return root;
    }

    /// output a dot-formatted file to print the tree
    /// starting from a given orNode
    public void printdot(orNode root) {
	// print file headers
	System.out.println("strict digraph T {");
	// print node
	orprint(root);
	// print closing
	System.out.println("}");
    }

    /// print orNode
    private void orprint(orNode o) {
	// print this node
	System.out.println(o.hashCode() + "[label=\"" +
			   "b=[" + DoubleArray.toString(o.belief) + "]\\n" +
			   "U(b)=" + o.u + "\\n" +
			   "L(b)=" + o.l + "\\n" +
			   "H(b)=" + o.h + 
			   "\"];");
	// every or node has a reference to be best node in its subtree
	System.out.println(o.hashCode() + "->" + o.bStar.hashCode() +
			 "[label=\"b*\",weight=0,color=blue];");
	// check it's not in the fringe before calling andprint
	if (o.children == null) return;	
	// print outgoing edges from this node
	for(andNode a : o.children) {
	    System.out.print(o.hashCode() + "->" + a.hashCode() +
			     "[label=\"" + 
			     "H(b,a)=" + o.h_a[a.getact()] + 
			     "\"];");
	}
	System.out.println();
	// recurse
	for(andNode a : o.children) andprint(a);
    }

    /// print andNode
    private void andprint(andNode a) {
	// print this node
	System.out.println(a.hashCode() + "[label=\"" + 
			   "a=" + problem.getactStr()[a.getact()] + "\\n" + 	
			   "U(b,a)=" + a.u +
			   "\"];");
	// print outgoing edges for this node
	for(orNode o : a.children) {
	    System.out.print(a.hashCode() + "->" + o.hashCode() + 
			     "[label=\"" +
			     "P(o|b,a)=" + problem.P_oba(o.getobs(),
							 a.getParent().belief,
							 a.getact()) + "\\n" +
			     "H(b,a,o)=" + a.h_o[o.getobs()] + 
			     "\"];");
	}
	System.out.println();
	// recurse
	for(orNode o : a.children) orprint(o);
    }
}     // AndOrTree