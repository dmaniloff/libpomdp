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
    private int act;

    /// L(b,a)
    public double l;

    /// U(b,a)
    public double u;

    /// P(o|b,a) in vector form for all its children
    public double poba[];

    /// H*(b,a)
    public double hStar;

    /// H(b,a,o)
    public double h_o[];

    /// bestO
    public int bestO;

    /// b*(b,a) - ref to best node in this subtree
    public orNode bStar;

    /// the parent of an AND node is an OR node
    private orNode parent;

    /// children
    public orNode children[];

    /// initializer
    public void init(int action, orNode parent) {
	this.act = action;
	this.parent = parent;
    }

    // get parent
    public orNode getParent() {
	return parent;
    }

    public int getact() {
	return act;
    }
}