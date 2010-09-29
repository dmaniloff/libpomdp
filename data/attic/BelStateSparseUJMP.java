/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File:
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.sparse;

// imports
import libpomdp.common.java.BelState;

import org.ujmp.core.doublematrix.impl.DefaultSparseDoubleMatrix;

public class BelStateSparseUJMP implements BelState {

    // main property, public for debug
    public DefaultSparseDoubleMatrix bSparse;

    // flat belief point
	//private double bPoint[];

    private double poba = -1.0;

    private int planid = -1;

    // constructor
    // in case this is the initial belief, poba = 0.0
    public BelStateSparseUJMP(DefaultSparseDoubleMatrix bSparse, double poba) {
	this.bSparse = bSparse;
	this.poba    = poba;
    }

    // calling this method should be for debugging
    // purposes only, otherwise we loose the sparse rep
    public double[] getPoint() {
	if(bSparse.getSize()[0]>1)
	    return bSparse.transpose().toDoubleArray()[0];
	else
	    return bSparse.toDoubleArray()[0];
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


} // 