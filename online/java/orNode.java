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

    /// H(b,a,o)
    public double h_o;

    /// the parent of an OR node is an AND node
    private andNode parent;

    /// AND children nodes indexed by action #
    public andNode children[];

    /// constructor
    public orNode(double belief[], int observation, andNode parent) {
	this.belief = belief;
	this.obs = observation;
	this.parent = parent;
	this.children = null;
    }

    /// constructor for root node
    public orNode(double belief[]) {
	this.belief = belief;
	this.obs = -1;
	this.parent = null;
	this.children = null;
    }
}