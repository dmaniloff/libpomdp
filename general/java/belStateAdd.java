/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateAdd.java
 * Description: implements belState via an ADD
 *              uses Popuart's implementation from Symbolic Perseus
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public class belStateAdd extends belState {
    
    // main property is the DD itself
    public DD bAdd;

    // we need the state variable ids to call convert2array
    private int staIds[];

    // constructor
    // in case this is the init belief, poba = 0.0
    public belStateAdd(DD b, int staIds[], double poba) {
	this.bAdd    = b;
	//this.bPoint = OP.convert2array(b, staIds); !!!!!!!
	this.staIds = staIds;
	this.poba   = poba;
    }

    // compute this only if we actually need it
    public double[] getbPoint() {
	return	OP.convert2array(bAdd, staIds);
    }

} // belState