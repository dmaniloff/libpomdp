/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: HybridValueIterationOrNode.java
 * Description: all of the values associated with a heuristic are simple
 *              placeholders to be filled by a method that implements
 *              the heuristic interface
 * Copyright (c) 2009, 2010 Diego Maniloff  
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.online.java;

import libpomdp.general.java.belState;

public class HybridValueIterationOrNode extends HeuristicSearchOrNode {

    /// can have this since we are re-starting the tree every time with
    /// no reused nodes...need to get rid of it for generality!
    private int depth_;

    /// best action after one-step lookahead
    public int oneStepBestAction;

    /// backupCandidate \max_{b \in T} b_{backupHeuristic}
    /// reference to the best backup node
    /// according to a given backup heuristic
    /// this is now a list of nodes of size |V|, one per alpha-vector
    public HybridValueIterationOrNode bakCandidate[];

    /// backup heuristic for this node
    public double bakHeuristic;

    /// value of the heuristic for the bakCandidate
    /// this is NOT the same as bakCandidate.bakHeuristic
    /// since there may be weighting factors along the path
    /// this is now a list of nodes of size |V|, one per alpha-vector
    public double bakHeuristicStar[];

    @Override
    public void init(belState belief, int obs, AndNode parent) {
	super.init(belief, obs, parent);
	// initialize one-step best action
	this.oneStepBestAction = -1;
	// backup heuristic
	this.bakCandidate      = null;
	this.bakHeuristic      = -1;
	this.bakHeuristicStar  = null;
	if (parent != null) 
	    depth_ =  ((HybridValueIterationOrNode)parent.getParent()).getDepth() + 1;
	else
	    depth_ = 0;
    }

    @Override
    public void initChildren(int nrAct) {
	// allocate space for the children AND nodes
	children_ = new HybridValueIterationAndNode[nrAct];
	for (int action=0; action<nrAct; action++) 
	    children_[action] = new HybridValueIterationAndNode();
    }

    @Override
    public HybridValueIterationAndNode getParent() {
	return (HybridValueIterationAndNode) parent_;
    }

    @Override
    public HybridValueIterationAndNode getChild(int i) {
	return (HybridValueIterationAndNode) children_[i];
    }

    @Override
    public HybridValueIterationAndNode[] getChildren() {
	return (HybridValueIterationAndNode[]) children_;
    }

    public int getDepth() {
	return depth_;
    }

} // orNode
