/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: andNode.java
 * Description: class for an AND node in the tree
 * Copyright (c) 2009, Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public class andNode {
    
    /// main property is the action the node represents
    public int act;

    /// lower bound of this node L(b,a)
    public double l;

    /// U(b,a)
    public double u;

    /// the parent of an AND node is an OR node
    private orNode parent;

    /// children
    public orNode children[];

    /// constructor
    public andNode(int action, orNode parent) {
	this.act = action;
	this.parent = parent;
	this.children = null;
    }
}