/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belStateFlat.java
 * Description: simple class to represent a belief state using
 *              a flat distribution over S
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */
public class belStateFlat extends belState{

    // constructor
    // in case this is the initial belief, poba = 0.0
    public belStateFlat(double[] bPoint, double poba) {
	this.bPoint = bPoint;
	this.poba   = poba;
    }

} // belStateFlat