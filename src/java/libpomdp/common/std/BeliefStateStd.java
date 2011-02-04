/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateSparseMTJ.java
 * Description: 
 * Copyright (c) 2009, 2010, 2011 Diego Maniloff 
 * Copyright (c) 2010, 2011 Mauricio Araya  
 --------------------------------------------------------------------------- */

package libpomdp.common.std;

// imports
import java.io.Serializable;

import no.uib.cipr.matrix.sparse.SparseVector;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;

public class BeliefStateStd extends CustomVector implements BeliefState, Serializable  {

    /**
     * 
     */
    private static final long serialVersionUID = 1232321752664518575L;

	public static BeliefStateStd transform(CustomVector vec){
		return new BeliefStateStd(vec.getInternal());
	}
	
    
    // associated P(o|b,a)
    private double poba = -1.0;

    // associated alpha vector id
    private int planid = -1;

    // constructor
    // in case this is the initial belief, poba = 0.0
    public BeliefStateStd(CustomVector bSparse, double poba) {
    this(bSparse);
	this.poba = poba;
    }

    public BeliefStateStd(CustomVector bSparse) {
	super(bSparse);
    }

    public BeliefStateStd(int length) {
    	super(length);
     }
    
    public BeliefStateStd(SparseVector internal) {
		super(internal);
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

    
    public BeliefStateStd copy() {
    	BeliefStateStd bel = new BeliefStateStd(this, poba);
    	bel.setAlpha(this.planid);
		return bel;
    }

	public boolean compare(BeliefState bel) {
		return(compare((CustomVector)bel));
	}

	public CustomVector getPoint() {
		return this;
	}

	public static BeliefStateStd getUniformBelief(int size) {
		return BeliefStateStd.transform(CustomVector.getUniform(size));
	}

} // BeliefStateStandard
