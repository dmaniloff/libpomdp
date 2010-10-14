/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateSparseMTJ.java
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.std;

// imports
import java.io.Serializable;

import libpomdp.common.java.BeliefState;
import libpomdp.common.java.CustomVector;

public class BeliefStateStd implements BeliefState, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1232321752664518575L;

	// sparse representation of the belief
    public CustomVector bSparse;

    // flat belief point)
	//  double bPoint[];

    // associated P(o|b,a)
    private double poba = -1.0;

    // associated alpha vector id
    private int planid = -1;

    // constructor
    // in case this is the initial belief, poba = 0.0
    public BeliefStateStd(CustomVector bSparse, double poba) {
	this.bSparse = bSparse;
	this.poba    = poba;
    }

    // calling this method should be for debugging
    // purposes only, otherwise we loose the sparse rep
    public CustomVector getPoint() {
    	return bSparse;
    }

    public double getPoba() {
	return poba;
    }

    public void setPoba(double poba) {
	this.poba = poba;
    }

    public int getAlpha() {
	return planid;
    }

    public void setAlpha(int planid) {
	this.planid = planid;
    }

	public BeliefState copy() {
		// TODO Auto-generated method stub
		return (new BeliefStateStd(bSparse,poba));
	}


} // belStateSparseMTJ