/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateFlat.java
 * Description: simple class to represent a belief state using
 *              a flat distribution over S
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */
package libpomdp.common.java.flat;

import libpomdp.common.java.BelState;

public class BelStateFlat implements BelState {

    // flat belief point
    private double bPoint[];

    private double poba = -1.0;

    private int planid = -1;

    // constructor
    // in case this is the initial belief, poba = 0.0
    public BelStateFlat(double[] bPoint, double poba) {
	this.bPoint = bPoint;
	this.poba   = poba;
    }

    public double[] getPoint() {
	return bPoint;
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


} // belStateFlat