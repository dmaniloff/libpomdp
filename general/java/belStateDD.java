/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateDD.java
 * Description: implements belState via an ADD
 *              uses Popuart's implementation from Symbolic Perseus
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public class belStateDD extends belState {
    
    // main property is the DD itself
    public DD ddB;

    // we need the state variable ids to call convert2array
    //private int staIds[];

    // constructor
    public belStateDD(DD b, int staIds[], double poba) {
	this.ddB = b;
	//this.staIds = staIds;
	this.bPoint = OP.convert2array(b, staIds);
	this.poba = poba;
    }

} // belState