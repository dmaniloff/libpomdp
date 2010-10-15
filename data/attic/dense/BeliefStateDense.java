/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateFlat.java
 * Description: simple class to represent a belief state using
 *              a flat distribution over S
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */
package libpomdp.common.dense;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import libpomdp.common.BeliefState;

public class BeliefStateDense implements BeliefState {

    // flat belief point
    private DenseVector point;

    private double poba = -1.0;

    private int planid = -1;

    // constructor
    // in case this is the initial belief, poba = 0.0
    public BeliefStateDense(Vector point, double poba) {
    	this.point = new DenseVector(point);
    	this.poba   = poba;
    }

    public BeliefStateDense(double point[], double poba) {
    	this(new DenseVector(point),poba);
    }

	public DenseVector getPoint() {
    	return point;
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