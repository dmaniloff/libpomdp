/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateSparseMTJ.java
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.general.java;

// imports
import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.sparse.*;

public class BelStateSparseMTJ implements BelState {

    // sparse representation of the belief
    public SparseVector bSparse;

    // flat belief point
    @SuppressWarnings("unused")
	private double bPoint[];

    // associated P(o|b,a)
    private double poba = -1.0;

    // associated alpha vector id
    private int planid = -1;

    // constructor
    // in case this is the initial belief, poba = 0.0
    public BelStateSparseMTJ(SparseVector bSparse, double poba) {
	this.bSparse = bSparse;
	this.poba    = poba;
    }

    // calling this method should be for debugging
    // purposes only, otherwise we loose the sparse rep
    public double[] getbPoint() {
	return Matrices.getArray(bSparse);
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


} // belStateSparseMTJ