/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateAdd.java
 * Description: implements belState via an ADD
 *              uses Popuart's implementation from Symbolic Perseus
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.add;

// imports
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import libpomdp.common.java.BeliefState;
import libpomdp.common.java.symbolic.DD;
import libpomdp.common.java.symbolic.OP;
 
public class BelStateAdd implements BeliefState {
    
    // main property is the DD itself
    public DD bAdd;

    // probability of reaching this belief when computing tao
    private double poba = -1.0;

    // plain id that supports this belief point
    private int planid = -1;

    // we need the state variable ids to call convert2array
    private int staIds[];

    // constructor
    // in case this is the init belief, poba = 0.0
    public BelStateAdd(DD b, int staIds[], double poba) {
	this.bAdd   = b;
	this.staIds = staIds;
	this.poba   = poba;
    }

    // compute this only if we actually need it
    public Vector getPoint() {
    	return(new DenseVector(OP.convert2array(bAdd, staIds)));
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

} // belState