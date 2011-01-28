/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateAdd.java
 * Description: implements belState via an ADD
 *              uses Popuart's implementation from Symbolic Perseus
 * Copyright (c) 2009, 2010 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.common.add;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.add.symbolic.DD;
import libpomdp.common.add.symbolic.OP;

public class BeliefStateAdd implements BeliefState {

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
    public BeliefStateAdd(DD b, int staIds[], double poba) {
	this.bAdd = b;
	this.staIds = staIds;
	this.poba = poba;
    }

    // compute this only if we actually need it
    public CustomVector getPoint() {
    	return (new CustomVector(OP.convert2array(bAdd, staIds)));
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

    
    public double getEntropy(double base) {
    	return OP.entropy(bAdd,staIds,base);
    }

    public boolean compare(BeliefState arg0) {
    	return (this.getPoint().compare(arg0.getPoint()));
    }
    
    public BeliefState copy() {
    	return new BeliefStateAdd(bAdd.copy(),staIds,poba);
    }

} // BelStateAdd