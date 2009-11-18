/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: orNode.java
 * Description: class for an OR node in the AND-OR tree
 *              all of the values associated with a heuristic are simple
 *              placeholders to be filled by a method that implements
 *              the heuristic interface
 * Copyright (c) 2009, Diego Maniloff  
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public class orNode {
    
    /// first property of an OR node is its belief state
    public belState belief;

    /// observation that leads to this node
    private int obs;

    /// lower bound of this node
    public double l;

    /// upper bound of this node
    public double u;

    /// H(b)
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
    public orNode bStar;

    /// size of the subtree rooted at this node, excluding itself
    public int subTreeSize;

    /// the parent of an OR node is an AND node
    private andNode parent;

    /// AND children nodes indexed by action #
    public andNode children[];

    /// initializer
    public void init(belState belief, int observation, andNode parent) {
	this.belief      = belief;
	this.obs         = observation;
	this.parent      = parent;
	this.children    = null;
	// best reference upon creation is to itself
	this.bStar       = this;
	// size of the subtree rooted here
	this.subTreeSize = 0;
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
