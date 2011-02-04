/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: BeliefState.java
 * Description: interface to implement different representations
 *              of a belief state
 *              properties are to be filled by extending classes
 * Copyright (c) 2009, 2010 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

public interface BeliefState {

    /// flat belief point
    public CustomVector getPoint();

    /// reachability probability = P(o|b,a)
    /// that is associated with the computation of tao(b,a,o)
    public double getPoba();

    /// set reachability probability
    public void setPoba(double poba);

    /// get index of the alpha vector that supports this point 
    public int getAlpha();

    /// get index of the alpha vector that supports this point 
    public void setAlpha(int planid);

    /// returns the entropy of the belief in nats
    public double getEntropy(); 
}