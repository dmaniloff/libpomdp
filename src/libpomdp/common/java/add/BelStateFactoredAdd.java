/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: BelStateFactoredADD.java
 * Description: implements belState via the product of marginals with ADDs
 *              uses Popuart's implementation from Symbolic Perseus
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.add;

import libpomdp.common.java.BeliefState;
import libpomdp.common.java.CustomVector;
import libpomdp.common.java.add.symbolic.DD;
import libpomdp.common.java.add.symbolic.OP;

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

    // compute this only if we actually need it
    public CustomVector getPoint() {
	return	new CustomVector(OP.convert2array(OP.multN(marginals), staIds));
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