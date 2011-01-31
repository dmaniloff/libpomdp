/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CustomMatrix.java
 * Description: This class represents an alpha-vector based on 
 * a custom vector and an action. 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.common;

public abstract class AlphaVector implements Comparable<AlphaVector> {

	protected int a;

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
    public int getAction(){
    	return(a);
    }

    /**
     * Create a proper copy of the alpha-vector.
     * 
     * @return an alpha-vector copy
     */
    public abstract AlphaVector copy();

    /**
     * Size of the alpha-vector
     * 
     * @return size of the vector
     */
    public abstract int size();
    
    /**
     * Compare to an alpha-vector with delta tolerance.
     * 
     * @param vec
     *            the vector to compare to
     * @param delta
     *            maximum difference between them for considering them equqls.
     * @return zero if (almost) equal, positive if is higher, and negative if is
     *         lower;
     */
    public abstract int compareTo(AlphaVector vec, double delta);

    /**
     * Compare to an alpha-vector with delta tolerance.
     * 
     * @param vec
     *            the vector to compare to
     * @return zero if equal, positive if is higher, and negative if is lower.
     */
    public abstract int compareTo(AlphaVector vec);

    /**
     * Get the reference of the internal data structure. Used for optimize
     * read-only operations.
     * 
     * @return a custom vector copy
     */
    public abstract Object getInternalRef(); 

    /**
     * Get a copy of the internal data structure.
     * 
     * @return a custom vector copy
     */
    public abstract Object getInternalCopy(); 

    
    /**
     * Reset the vector reference to a new one.
     * 
     * @param v
     *            the new custom vector
     */
    public abstract void setValues(Object newref);

    /**
     * Action setter.
     * 
     * @param a
     *            a valid action
     */
    public  void setAction(int a){
    	this.a=a;
    }

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

    /**
     * Add the values of a internal reference.
     * 
     * @param vec
     *            the custom vector to sum
     */
    public abstract void add(Object iref);

	public abstract double get(int s);
	
}
