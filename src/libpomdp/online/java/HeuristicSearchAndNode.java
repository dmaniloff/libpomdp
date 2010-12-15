/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: HeuristicSearchAndNode.java
 * Description: all of the values associated with a heuristic are simple
 *              placeholders to be filled by a method that implements
 *              the heuristic interface
 * Copyright (c) 2009, 2010 Diego Maniloff  
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.online.java;

import libpomdp.common.java.CustomVector;

public class HeuristicSearchAndNode extends AndNode {

    public double                l;
    public double                u;
    public double                rba;
    public double                hStar;
    public int                   oStar;
    public HeuristicSearchOrNode bStar;

    public void init(int action, OrNode parent, double rba) {
	super.init(action, parent);
	this.rba = rba;
    }

    @Override
    public void initChildren(int nrObs, CustomVector pOba) {
	children_ = new HeuristicSearchOrNode[nrObs];
	for(int observation = 0; observation < nrObs; observation++) {
	    if(pOba.get(observation) != 0)
		children_[observation] = new HeuristicSearchOrNode();
	} 
    }

    @Override
    public HeuristicSearchOrNode getParent() {
	return (HeuristicSearchOrNode) parent_;
    }

    @Override
    public HeuristicSearchOrNode getChild(int i) {
	return (HeuristicSearchOrNode) children_[i];
    }

    @Override
    public HeuristicSearchOrNode[] getChildren() {
	return (HeuristicSearchOrNode[]) children_;
    }

}