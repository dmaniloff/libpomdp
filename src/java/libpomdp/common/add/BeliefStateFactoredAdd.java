/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: BeliefStateFactoredAdd.java
 * Description: implements BeliefState via the product of marginals with ADDs
 *              uses Popuart's implementation from Symbolic Perseus
 * Copyright (c) 2009, 2010, 2011 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.common.add;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.add.symbolic.DD;
import libpomdp.common.add.symbolic.OP;

public class BeliefStateFactoredAdd implements BeliefState {

    // main property is the DD array of marginals
    public DD marginals[];

    // probability of reaching this belief when computing tao
    private double poba = -1.0;

    // plain id that supports this belief point
    private int planid = -1;

    // we need the state variable ids to call convert2array
    private int staIds[];

    // constructor
    // in case this is the init belief, poba = 0.0
    public BeliefStateFactoredAdd(DD m[], int staIds[], double poba) {
	this.marginals = m;
	this.staIds = staIds;
	this.poba = poba;
    }

    // constructor without poba
    public BeliefStateFactoredAdd(DD m[], int staIds[]) {
	this.marginals = m;
	this.staIds = staIds;
    }

    // compute this only if we actually need it
    public CustomVector getPoint() {
	return new CustomVector(OP.convert2array(OP.multN(marginals), staIds));
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

    // compute entropy of this point in nats
    public double getEntropy(double base) {
    	DD m[] = new DD[marginals.length - 1];
    	System.arraycopy(marginals, 0, m, 0, marginals.length - 1);
    	//return -OP.dotProductNoMem(OP.log(OP.multN(m)), OP.multN(m), staIds);
		return OP.entropy(OP.multN(m),staIds,base);
    }

    public boolean compare(BeliefState arg0) {
    	return (this.compare(arg0));
    }

    public BeliefState copy() {
    	DD newm[]=new DD[marginals.length];
    	for (int i=0;i<marginals.length;i++){
    		newm[i]=marginals[i].copy();
    	}
    	return new BeliefStateFactoredAdd(newm,staIds,poba);
    }

} // BeliefStateFactoredAdd