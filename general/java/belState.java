/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belState.java
 * Description: simple class to implement different representations
 *              of a belief state
 *              properties are to be filled by extending classes
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public abstract class belState {

    /// flat belief point
    /// this should be filled by the extending classes
    // public double bPoint[];
    public abstract double[] getbPoint();

    /// reachability prob = P(o|b,a)
    /// that is associated with the computation of tao(b,a,o)
    public double poba;

}