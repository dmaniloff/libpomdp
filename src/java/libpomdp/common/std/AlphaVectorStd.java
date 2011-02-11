/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CustomMatrix.java
 * Description: This class represents an alpha-vector based on 
 * a custom vector and an action. 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.common.std;

import no.uib.cipr.matrix.sparse.SparseVector;
import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;



public class AlphaVectorStd  extends CustomVector implements AlphaVector {

    /**
	 * 
	 */
	private static final long serialVersionUID = -9053379653819618307L;
	protected int a;

	public static AlphaVectorStd transform(CustomVector vec){
		return new AlphaVectorStd(vec.getInternal());
	}
	
	public static AlphaVectorStd transform(CustomVector vec,int a){
		AlphaVectorStd ne=new AlphaVectorStd(vec.getInternal());
		ne.setAction(a);
		return(ne);
	}
	
    /**
     * Constructor using an existing vector.
     * 
     * @param v
     *            the reference of the vector to use
     * @param a
     *            the action associated to the vector v
     */
    public AlphaVectorStd(CustomVector v, int a) {
    	super(v);
    	this.a = a;
    }

    public AlphaVectorStd(AlphaVectorStd vec) {
    	super(vec);
    	this.a = vec.a;
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

    public AlphaVectorStd(SparseVector internal) {
		super(internal);
	}

	/**
     * Evaluates a belief-point for this alpha.
     * 
     * @param bel
     *            the belief-state point
     * @return the dot product between both vectors.
     */
    public double eval(BeliefState bel) {
    	return (dot((BeliefStateStd)bel));
    }

    /**
     * Create a proper copy of the alpha-vector.
     * 
     * @return an alpha-vector copy
     */
    public AlphaVectorStd copy() {
    	return new AlphaVectorStd(this);
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
    //public int compareTo(AlphaVector vec, double delta) {
    //	AlphaVectorStd v=(AlphaVectorStd) vec;
    //	return (compareTo((CustomVector)v,delta));
    //}

    /**
     * Compare to an alpha-vector with delta tolerance.
     * 
     * @param vec
     *            the vector to compare to
     * @return zero if equal, positive if is higher, and negative if is lower.
     */
    public int compareTo(AlphaVector vec) {
     	AlphaVectorStd v=(AlphaVectorStd) vec;
    	return (compareTo((CustomVector)v));
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
    	AlphaVectorStd vec=(AlphaVectorStd) res;
    	set((CustomVector)vec);
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
    	set(idx, value);
    }
    
    /**
     * Add the values of other alpha-vector. This does not modify the action
     * value.
     * 
     * @param alpha
     *            the alpha-vector to add
     */
    public void add(AlphaVector alpha) {
    	AlphaVectorStd stdAlpha=(AlphaVectorStd) alpha;
    	add((CustomVector)stdAlpha);
    }
    
	public int getAction() {
		return a;
	}
	
	
}
