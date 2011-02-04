/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CustomMatrix.java
 * Description: This class represents an alpha-vector based on 
 * a custom vector and an action. 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.common;

public interface AlphaVector {


    /**
     * Evaluates a belief-point for this alpha.
     * 
     * @param bel the belief-state point
     * @return the evaluation on that point.
     */
    public abstract double eval(BeliefState bel);

    /**
     * Get the associated action
     * 
     * @return the associated action
     */
    public int getAction();

    /**
     * Create a proper copy of the alpha-vector.
     * 
     * @return an alpha-vector copy
     */
    public abstract AlphaVector copy();

    /**
     * Compare to an alpha-vector with delta tolerance.
     * 
     * @param vec
     *            the vector to compare to
     * @return zero if equal, positive if is higher, and negative if is lower.
     */
    public abstract int compareTo(AlphaVector vec);
    
    /**
     * Action setter.
     * 
     * @param a
     *            a valid action
     */
    public void setAction(int a);

    /**
     * New values for the alpha vector.
     * 
     * @param res
     *            the alpha vector to copy from
     */
    public abstract void set(AlphaVector res);

   
    /**
     * Add the values of other alpha-vector. This does not modify the action
     * value.
     * 
     * @param alpha
     *            the alpha-vector to add
     */
    public abstract void add(AlphaVector alpha);

    public abstract double get(int s);

	
}
