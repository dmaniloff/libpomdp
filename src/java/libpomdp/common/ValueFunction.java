/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: valueFunction.java
 * Description: representation of a set of alpha vectors and their
 *              associated actions for direct control (if possible)
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common;


public interface ValueFunction {
    
    // list of actions associated with each alpha
    public int[] getActions();

    public int size();
    
    public CustomVector getVectorRef(int idx);
    public CustomVector getVectorCopy(int idx);
    
    // value of a belief according to this value function
    public double value(BeliefState b);

} // valueFunction
