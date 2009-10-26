/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: orNode.java
 * Description: class for an OR node in the tree
 * Copyright (c) 2009, Diego Maniloff  
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public class orNode {
    
    /// first property of an OR node is its belief state
    public double belief[];

    /// observation that leads to this node
    private int obs;

    /// lower bound of this node
    public double l;

    /// upper bound of this node
    public double u;

    /// H(b)
    public double h;
    
    /// H(b,a) - randomized approximation of pi*
    public double h_a[];

    /// best action 
    public int bestA;
    
    /// H*(b) 
    public double hStar;

    /// b* - best node in the fringe of this subtree
    public orNode bStar;

    /// the parent of an OR node is an AND node
    private andNode parent;

    /// AND children nodes indexed by action #
    public andNode children[];

    /// initializer
    public void init(double belief[], int observation, andNode parent) {
	this.belief = belief;
	this.obs = observation;
	this.parent = parent;
	this.children = null;
    }

    // getParent
    public andNode getParent() {
	return parent;
    }
    
    public int getobs() {
	return obs;
    }

    /// constructor for root node
    /* 
     * public orNode(double belief[]) {
     * 	this.belief = belief;
     * 	this.obs = -1;
     * 	this.parent = null;
     * 	this.children = null;
     * }
     */
}