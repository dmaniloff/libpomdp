/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belState.java
 * Description: simple class to implement different representations
 *              of a belief state
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public abstract class belState {

    /// flat belief point
    /// this should be filled by the extending classes
    public double bPoint[];

    /// reachability prob = P(o|b,a)
    /// that is associated with the computation of tao(b,a,o)
    public double poba;

    /// constructor
    // public belState(double bPoint[], double poba) {
    // 	this.bPoint = bPoint;
    // 	this.poba   = poba;
    // }

    

    /// single method is to return the belief point
    /// we define it in this way to avoid re-calculations
    //public double[] getBpoint() {
    //	return bPoint;
    //}
}