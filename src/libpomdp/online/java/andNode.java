/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: andNode.java
 * Description: class for an AND node in the tree
 * Copyright (c) 2009, 2010 Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.online.java;

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
    /// this is now a list of nodes of size |V|, one per alpha-vector
    public orNode bakCandidate[];

    /// value of the heuristic for the bakCandidate
    /// this is NOT the same as bakCandidate.bakHeuristic
    /// since there may be weighting factors along the path
    /// this is now a list of nodes of size |V|, one per alpha-vector
    public double bakHeuristicStar[];

    /// a plan id that is valid (part of the max planes representation)
    /// for one of this andNode's children (any)
    public int validPlanid;
    
    /// # of belief nodes in the subtree rooted at this node
    // public int subTreeSize;
    
    /// supportList[i] is the number of beliefs in the subtree of 
    /// this node that are supported by alpha-vector i
    //public int[] supportList;
    

    /// initializer
    public void init(int action, orNode parent, double rba) {
	this.act              = action;
	this.parent           = parent;
	this.rba              = rba;
	this.bStar            = null;
	this.bakCandidate     = null;
	this.bakHeuristicStar = null;
	this.validPlanid      = -1;
	//this.subTreeSize      = -1;
	//this.supportList      = null;
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