/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: belState.java
 * Description: interface to implement different representations
 *              of a belief state
 *              properties are to be filled by extending classes
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

public interface BelState {

    /// flat belief point
    public double[] getbPoint();

    /// reachability prob = P(o|b,a)
    /// that is associated with the computation of tao(b,a,o)
    public double getpoba();

    /// set reachability prob
    public void setpoba(double poba);

    /// get index of the alpha vector that supports this point 
    public int getplanid();

    /// get index of the alpha vector that supports this point 
    public void setplanid(int planid);
}