/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CustomMatrix.java
 * Description: This class represents an alpha-vector based on 
 * a custom vector and an action. 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.common.std;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;



public class AlphaVectorStd extends AlphaVector {

    protected CustomVector v;

    /**
     * Constructor using an existing vector.
     * 
     * @param v
     *            the reference of the vector to use
     * @param a
     *            the action associated to the vector v
     */
    public AlphaVectorStd(CustomVector v, int a) {
    	this.v = v;
    	this.a = a;
    }

    /**
     * Constructor by vector dimension. Creates a zero-vector associated with
     * the action -1
     * 
     * @param dim
     *            the size of the zero-vector to create
     */
    public AlphaVectorStd(int dim) {
    	this(new CustomVector(dim), -1);
    }

    /**
     * Constructor by vector dimension and action.
     * 
     * @param dim
     *            the size of the zero-vector to create
     * @param a
     *            the action associated to the vector v
     */
    public AlphaVectorStd(int dim, int a) {
    	this(new CustomVector(dim), a);
    }

    /**
     * Evaluates a belief-point for this alpha.
     * 
     * @param bel
     *            the belief-state point
     * @return the dot product between both vectors.
     */
    public double eval(BeliefState bel) {
    	return (v.dot(bel.getPoint()));
    }

    /**
     * Create a proper copy of the alpha-vector.
     * 
     * @return an alpha-vector copy
     */
    public AlphaVectorStd copy() {
    	return (new AlphaVectorStd(v.copy(), a));
    }

    /**
     * Size of the alpha-vector
     * 
     * @return size of the vector
     */
    public int size() {
	return v.size();
    }

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
    public int compareTo(AlphaVector vec, double delta) {
	return (v.compareTo((CustomVector)vec.getInternalRef(),delta));
    }

    /**
     * Compare to an alpha-vector with delta tolerance.
     * 
     * @param vec
     *            the vector to compare to
     * @return zero if equal, positive if is higher, and negative if is lower.
     */
    public int compareTo(AlphaVector vec) {
	return (v.compareTo((CustomVector)vec.getInternalRef()));
    }

    /**
     * Reset the vector reference to a new one.
     * 
     * @param v
     *            the new custom vector
     */
    public void setValues(Object v) {
    	this.v = (CustomVector)v;
    }
    
    
    /**
     * Action setter.
     * 
     * @param a
     *            a valid action
     */
    public void setAction(int a) {
	this.a = a;
    }

    /**
     * New values for the alpha vector.
     * 
     * @param res
     *            the alpha vector to copy from
     */
    public void set(AlphaVector res) {
    	setValues((CustomVector)res.getInternalCopy());
    	setAction(res.getAction());
    }

    /**
     * Change one value of the alpha-vector.
     * 
     * @param idx
     *            the vector index
     * @param value
     *            the nez value
     */
    public void setValue(int idx, double value) {
    	v.set(idx, value);
    }
    
    /**
     * Add the values of other alpha-vector. This does not modify the action
     * value.
     * 
     * @param alpha
     *            the alpha-vector to add
     */
    public void add(AlphaVector alpha) {
    	add(alpha.getInternalRef());
    }

    /**
     * Add the values of a custom vector.
     * 
     * @param vec
     *            the custom vector to sum
     */
    public void add(Object vec) {
    	v.add((CustomVector)vec);
    }
    
    /**
     * Add the values of a custom vector.
     * 
     * @param vec
     *            the custom vector to sum
     */
    public void add(CustomVector vec) {
    	v.add(vec);
    }
    
    

	@Override
	public CustomVector getInternalCopy() {
		return v.copy();
	}

	@Override
	public CustomVector getInternalRef() {
		return v;
	}

	@Override
	public double get(int s) {
		return v.get(s);
	}
	
	
	
}
