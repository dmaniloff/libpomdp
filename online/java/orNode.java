/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: orNode.java
 * Description: class for an OR node in the AND-OR tree
 *              all of the values associated with a heuristic are simple
 *              placeholders to be filled by a method that implements
 *              the heuristic interface
 * Copyright (c) 2009, 2010 Diego Maniloff  
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public class orNode {
    
    /// first property of an OR node is its belief state
    public belState belief;

    /// observation that leads to this node
    private int obs;

    // ------------------------------------------------------------------------
    // expansion heuristic properties
    // ------------------------------------------------------------------------
    
    /// lower bound of this node
    public double l;

    /// upper bound of this node
    public double u;

    /// H(b)
    /// expansion heuristic
    public double h_b;
    
    /// H(b,a):
    /// randomized approximation of pi*(b,a) for each children node
    public double h_ba[];

    /// H(b,a,o):
    /// heuristic of the arc incident on this node
    public double h_bao;

    /// aStar:
    /// action in the path to b* in the subree of this node
    public int aStar;
    
    /// H*(b):
    /// maximizing product of heuristic measures in the path to
    /// the next node to expand in the subtree of this node
    /// H*(b) = \max_{a_i,o_i} H(b_F) \prod{ H(b_i,a_i) H(b_i, a_i, o_i) }
    ///       = H(b*) \prod{ H(b_i,a_i) H(b_i, a_i, o_i) }
    public double hStar; 

    /// b*:
    /// reference to the next node to expand according to H*(b)
    /// in the subtree of this node
    public orNode bStar; // might wanna change this name to expandCandidate or sth ...

    /// size of the subtree rooted at this node, excluding itself
    public int subTreeSize;

    /// the parent of an OR node is an AND node
    private andNode parent;

    /// the depth relative to orNodes only
    public int depth = 0;

    /// AND children nodes indexed by action #
    public andNode children[];

    // ------------------------------------------------------------------------
    // backup heuristic properties
    // ------------------------------------------------------------------------

    /// delta of the one-step lookahead
    public double oneStepDeltaLower;
    public double oneStepDeltaUpper;

    /// best action after one-step lookahead
    public int oneStepBestAction;

    /// backupCandidate \max_{b \in T} b_{backupHeuristic}
    /// reference to the best backup node
    /// according to a given backup heuristic
    public orNode bakCandidate;

    /// backup heuristic
    public double bakHeuristic;

    /// value of the heuristic for the bakCandidate
    /// this is NOT the same as bakCandidate.bakHeuristic
    /// since there may be weighting factors along the path
    public double bakHeuristicStar;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// initializer
    public void init(belState belief, int observation, andNode parent) {
	this.belief            = belief;
	this.obs               = observation;
	// parent and depth
	this.parent            = parent;
	if (parent != null) 
	    this.depth         = parent.getParent().depth + 1;
	this.children          = null;
	// best reference upon creation is to itself
	this.bStar             = this;
	// initialize one-step improvement 
	this.oneStepDeltaLower = -1;
	this.oneStepDeltaUpper = -1;
	// initialize one-step best action
	this.oneStepBestAction = -1;
	// backup heuristic
	this.bakCandidate      = this;
	this.bakHeuristic      = -1;
	this.bakHeuristicStar  = -1;
	// size of the subtree rooted here
	this.subTreeSize  = 0;
    }

    // getParent
    public andNode getParent() {
	return parent;
    }
    
    // disconnect - kill the parent of this node
    public void disconnect() {
	this.parent = null;
	this.obs    = -1;
    }

    public int getobs() {
	return obs;
    }

} // orNode
