/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: BeliefStateAdd.java
 * Description: implements BeliefState via an ADD
 *              uses Popuart's implementation from Symbolic Perseus
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

// imports
import symPerseusJava.DD;
import symPerseusJava.OP;

public class BeliefStateAdd implements BeliefState {
    
    // main property is the DD itself
    public DD bAdd;

    // probability of reaching this belief when computing tao
    private double poba = -1.0;

    // plain id that suuports this belief point
    private int planid = -1;

    // we need the state variable ids to call convert2array
    private int staIds[];

    // constructor
    // in case this is the init belief, poba = 0.0
    public BeliefStateAdd(DD b, int staIds[], double poba) {
	this.bAdd   = b;
	this.staIds = staIds;
	this.poba   = poba;
    }

    // compute this only if we actually need it
    public double[] getbPoint() {
	return	OP.convert2array(bAdd, staIds);
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

    public double getEntropy() {
	// TODO Auto-generated method stub
	return -1;
    }

} // BeliefState