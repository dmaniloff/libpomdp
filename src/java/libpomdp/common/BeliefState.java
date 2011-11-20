/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: BeliefState.java
 * Description: interface to implement different representations
 *              of a belief state
 *              properties are to be filled by extending classes
 * Copyright (c) 2009, 2010 Diego Maniloff, Mauricio Araya 
 --------------------------------------------------------------------------- */

package libpomdp.common;


public interface BeliefState {


    /** Get belief state point as a custom vector.
	@return belief-point */
    public CustomVector getPoint();

    // TODO: It is not better to have here the reference of the alpha vector rather
    // than the index??

    /// returns the entropy of the belief in nats
    public double getEntropy(); 


    /** Get the reachability probability. Pr(o|b,a).
	@return the reachability probability. */
    public double getPoba();

    /** Set the reachability probability. Pr(o|b,a).
	@param poba the reachability probability */ 
    public void setPoba(double poba);


    // TODO: It is not better to have here the reference of the alpha vector rather
    // than the index??

    /** Get index of the alpha vector that supports this point.
	@return the index of the alpha vector that supports this point */
    	public int getAlphaVectorIndex();

    /** Set index of the alpha vector that supports this point. 
	@param planid the index of the alpha vector that supports this point */
    public void setAlphaVectorIndex(int planid);

	
    /** Compare with other belief-state.
	@param bel a belief-state. 
	@return true if equals, false else */
    public boolean compare(BeliefState bel);


    /** Create a proper copy of the belief-state.
        @return a belief-state copy */
    public BeliefState copy();
}

