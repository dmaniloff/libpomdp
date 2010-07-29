/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateFlat.java
 * Description: simple class to represent a belief state using
 *              a flat distribution over S
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */
public class belStateFlat implements belState {

    // flat belief point
    private double bPoint[];

    private double poba = -1.0;

    private int planid = -1;

    // constructor
    // in case this is the initial belief, poba = 0.0
    public belStateFlat(double[] bPoint, double poba) {
	this.bPoint = bPoint;
	this.poba   = poba;
    }

    public double[] getbPoint() {
	return bPoint;
    }

    public double getpoba() {
	return poba;
    }

    public void setpoba(double poba) {
	this.poba = poba;
    }

    public int getplanid() {
	return planid;
    }

    public void setplanid(int planid) {
	this.planid = planid;
    }


} // belStateFlat