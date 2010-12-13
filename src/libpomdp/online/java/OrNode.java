/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: OrNode.java
 * Description: class for an OR node in an AND-OR tree
 * Copyright (c) 2009, 2010 Diego Maniloff  
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.online.java;

import libpomdp.common.java.BeliefState;

public abstract class OrNode {

    /// should these be private too?
    protected AndNode   parent_;
    protected AndNode   children_[];
    private   BeliefState  beliefState_;
    private   int       obs_;
    private   int       subTreeSize_;

    /// initialization
    public void init(BeliefState belief, int obs, AndNode parent) {
	beliefState_  = belief;
	obs_          = obs;
	parent_       = parent;
	children_     = null;
	subTreeSize_  = 0;
    }

    public abstract void initChildren(int nrAct);
    
    public abstract AndNode getParent();

    public abstract AndNode getChild(int i); 
    
    public abstract AndNode[] getChildren();
    
    public BeliefState getBeliefState() {
	return beliefState_;
    }

    public int getObs() {
	return obs_;
    }

    public void setSubTreeSize(int subTreeSize) {
	this.subTreeSize_ = subTreeSize;
    }

    public int getSubTreeSize() {
	return subTreeSize_;
    }
    
    public void disconnect() {
	parent_ = null;
	obs_    = -1;
    }

    

}