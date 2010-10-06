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

import no.uib.cipr.matrix.Vector;

public interface BeliefState {

    /// flat belief point
    public Vector getPoint();

    /// reachability prob = P(o|b,a)
    /// that is associated with the computation of tao(b,a,o)
    public double getPoba();

    /// set reachability prob
    public void setPoba(double poba);

    /// get index of the alpha vector that supports this point 
    public int getAlpha();

    /// get index of the alpha vector that supports this point 
    public void setAlpha(int planid);
}