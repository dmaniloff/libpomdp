/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: ValueFunction.java
 * Description: representation of a set of alpha vectors and their
 *              associated actions for direct control (if possible)
 * Copyright (c) 2009, 2010, 2011 Diego Maniloff 
 * Copyright (c) 2010, 2011 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.common;

public interface ValueFunction {

    public double value(BeliefState b);

    public int[] getActions();

    public AlphaVector getAlpha(int idx);

    public CustomVector getAlphaValues(int idx);

    public int size();

    public void sort();

} // valueFunction
