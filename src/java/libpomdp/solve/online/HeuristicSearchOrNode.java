/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: HeuristicSearchOrNode.java
 * Description: all of the values associated with a heuristic are simple
 *              placeholders to be filled by a method that implements
 *              the heuristic interface
 * Copyright (c) 2009, 2010 Diego Maniloff  
 --------------------------------------------------------------------------- */

package libpomdp.solve.online;

import libpomdp.common.BeliefState;


public class HeuristicSearchOrNode extends OrNode {

    public double                l;
    public double                u;
    public double                h_b;
    public double                h_ba[];
    public double                h_bao;
    public int                   aStar;
    public double                hStar;
    public HeuristicSearchOrNode bStar;
    public double                oneStepDeltaLower;
    public double                oneStepDeltaUpper;

    @Override
    public void init(BeliefState belief, int obs, AndNode parent) {
	super.init(belief, obs, parent);
	// initialize one-step improvement 
        this.oneStepDeltaLower = -1;
        this.oneStepDeltaUpper = -1;
	// best reference upon creation is to itself
	this.bStar = this;
    }

    @Override
    public void initChildren(int nrAct) {
	// allocate space for the children AND nodes
	children_ = new HeuristicSearchAndNode[nrAct];
	for (int action=0; action<nrAct; action++) 
	    children_[action] = new HeuristicSearchAndNode();
    }

    @Override
    public HeuristicSearchAndNode getParent() {
	return (HeuristicSearchAndNode) parent_;
    }

    @Override
    public HeuristicSearchAndNode getChild(int i) {
	return (HeuristicSearchAndNode) children_[i];
    }

    @Override
    public HeuristicSearchAndNode[] getChildren() {
	return (HeuristicSearchAndNode[]) children_;
    }

}