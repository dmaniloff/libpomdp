/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: ValueFunction.java
 * Description: representation of a set of alpha vectors and their
 *              associated actions for direct control (if possible)
 * Copyright (c) 2009, 2010 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.common.java;


public interface ValueFunction {
    
    // value of a belief according to this value function
    public double V(BeliefState b);

    public CustomVector getVector(int idx);

    // list of actions associated with each alpha
    public int[] getActions();

    public int size();

} // ValueFunction
