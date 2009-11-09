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
    
    /// H(b,a) - randomized approximation of pi*
    public double h_ba[];

    /// H(b,a,o)
    public double h_bao;

    /// best action 
    public int bestA;
    
    /// H*(b) 
    public double hStar;

    /// b* - best node in the fringe of this subtree
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
