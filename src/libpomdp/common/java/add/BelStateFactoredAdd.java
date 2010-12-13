/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: BelStateFactoredAdd.java
 * Description: implements BeliefState via the product of marginals with ADDs
 *              uses Popuart's implementation from Symbolic Perseus
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.add;

// imports
import libpomdp.common.java.BeliefState;
import symPerseusJava.DD;
import symPerseusJava.OP;

public class BelStateFactoredAdd implements BeliefState {
    
    // main property is the DD array of marginals
    public DD marginals[];

    // probability of reaching this belief when computing tao
    private double poba = -1.0;

    // plain id that suuports this belief point
    private int planid = -1;

    // we need the state variable ids to call convert2array
    private int staIds[];

    // constructor
    // in case this is the init belief, poba = 0.0
    public BelStateFactoredAdd(DD m[], int staIds[], double poba) {
	this.marginals = m;
	this.staIds    = staIds;
	this.poba      = poba;
    }

    // constructor without poba
    public BelStateFactoredAdd(DD m[], int staIds[]) {
	this.marginals = m;
	this.staIds    = staIds;	
    }

    // compute entropy of this point in nats
    public double getEntropy() {
	DD m[] = new DD[marginals.length-1];
	System.arraycopy(marginals, 0, m, 0, marginals.length-1);
	return -OP.dotProductNoMem(
                	OP.log(OP.multN(m)),
                        OP.multN(m),
                        staIds);
    }
    
    // compute this only if we actually need it
    public double[] getbPoint() {
	return	OP.convert2array(OP.multN(marginals), staIds); // bug here, should be marginals(1:10)
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

} // BeliefState