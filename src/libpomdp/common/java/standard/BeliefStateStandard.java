/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateSparseMTJ.java
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * Copyright (c) 2010 Mauricio Araya  
 --------------------------------------------------------------------------- */

package libpomdp.common.java.standard;

import libpomdp.common.java.BeliefState;
import libpomdp.common.java.CustomVector;

public class BeliefStateStandard implements BeliefState {

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
    public BeliefStateStandard(CustomVector bSparse, double poba) {
	this.bSparse = bSparse;
	this.poba    = poba;
    }

    // calling this method should be for debugging
    // purposes only, otherwise we loose the sparse rep
    @Override
    public CustomVector getPoint() {
	return bSparse;
    }

    @Override
    public double getPoba() {
	return poba;
    }

    @Override
    public void setPoba(double poba) {
	this.poba = poba;
    }

    @Override
    public int getAlpha() {
	return planid;
    }

    @Override
    public void setAlpha(int planid) {
	this.planid = planid;
    }

    @Override
    public double getEntropy() {
	// TODO Auto-generated method stub
	return 0;
    }

    public BeliefState copy() {
	return (new BeliefStateStandard(bSparse,poba));
    }

} // BeliefStateStandard
