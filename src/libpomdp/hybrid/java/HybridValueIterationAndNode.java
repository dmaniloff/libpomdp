/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: andNode.java
 * Description: class for an AND node in the tree
 * Copyright (c) 2009, 2010 Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.hybrid.java;

import libpomdp.online.java.HeuristicSearchAndNode;
import libpomdp.online.java.OrNode;

public class HybridValueIterationAndNode extends HeuristicSearchAndNode {

    /// best node to backup
    /// this is now a list of nodes of size |V|, one per alpha-vector
    public HybridValueIterationOrNode bakCandidate[];

    /// value of the heuristic for the bakCandidate
    /// this is NOT the same as bakCandidate.bakHeuristic
    /// since there may be weighting factors along the path
    /// this is now a list of nodes of size |V|, one per alpha-vector
    public double bakHeuristicStar[];

    /// a plan id that is valid (part of the max planes representation)
    /// for one of this andNode's children (any)
    public int validPlanid;

    public void init(int action, OrNode parent, double rba) {
	super.init(action, parent, rba);
	this.bakCandidate     = null;
	this.bakHeuristicStar = null;
	this.validPlanid      = -1;
    }

    @Override
    public void initChildren(int nrObs, double pOba[]) {
	children_ = new HybridValueIterationOrNode[nrObs];
	for(int observation = 0; observation < nrObs; observation++) {
	    if(pOba[observation] != 0)
		children_[observation] = new HybridValueIterationOrNode();
	} 
    }

    @Override
    public HybridValueIterationOrNode getParent() {
	return (HybridValueIterationOrNode) parent_;
    }

    @Override
    public HybridValueIterationOrNode getChild(int i) {
	return (HybridValueIterationOrNode) children_[i];
    }

    @Override
    public HybridValueIterationOrNode[] getChildren() {
	return (HybridValueIterationOrNode[]) children_;
    }
}