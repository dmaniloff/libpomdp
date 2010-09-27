/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: andNode.java
 * Description: class for an AND node in the tree
 * Copyright (c) 2009, 2010 Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.online.java;

// imports
import libpomdp.general.java.*;

public class andNode {
    
    /// main property is the action the node represents
    private int act;

    /// L(b,a)
    public double l;

    /// U(b,a)
    public double u;

    /// R(b,a):
    /// computed and stored at init time to avoid re-computation
    public double rba;

    /// H*(b,a):
    /// maximizing product of heuristic measures in the path to
    /// the next node to expand in the subtree of this node
    public double hStar;

    /// oStar
    /// observation in the path to b*(b,a) in the subtree of this node
    public int oStar;

    /// b*(b,a):
    /// ref to the next node to expand in the subtree of this node
    public orNode bStar;

    /// the parent of an AND node is an OR node
    private orNode parent;

    /// children
    public orNode children[];

    /// best node to backup
    public orNode bakCandidate;

    /// value of the heuristic for the bakCandidate
    /// this is NOT the same as bakCandidate.bakHeuristic
    /// since there may be weighting factors along the path
    public double bakHeuristicStar;

    /// a plan id that is valid (part of the max planes representation)
    /// for one of this andNode's children (any)
    public int validPlanid;

    /// initializer:
    /// needs a reference to problem to call Rba
    public void init(int action, orNode parent, Pomdp problem) {
	this.act              = action;
	this.parent           = parent;
	this.rba              = problem.Rba(parent.belief, action);
	this.bStar            = null;
	this.bakCandidate     = null;
	this.bakHeuristicStar = -1;
	this.validPlanid     = -1;
	// might want to include subTreeSize for andNodes too?
    }

    // get parent
    public orNode getParent() {
	return parent;
    }

    public int getAct() {
	return act;
    }
}