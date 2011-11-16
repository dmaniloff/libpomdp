/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: Pomdp.java
 * Description: interface to represent Pomdp problem specifications
 * Copyright (c) 2009, 2010 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

import libpomdp.common.std.BeliefStateStd;


/** Interface to represent pomdp problem specifications
 * @author Diego Maniloff 
 * @author Mauricio Araya
*/

public interface Pomdp {

    /// tao(b,a,o)
    public BeliefState nextBeliefState(BeliefState b, int a, int o);

    /// R(b,a): scalar value
    public double expectedImmediateReward(BeliefState b, int a);

    /// P(o|b,a): 1 x o in vector form for all o's
    public CustomVector observationProbabilities(BeliefState bel, int a);

    /// T(s,a,s'): s x s' matrix
    public CustomMatrix getTransitionTable(int a);

    /// O(s',a,o): s' x o matrix
    public CustomMatrix getObservationTable(int a);
    
    /// R(s,a): 1 x s vector
    public CustomVector getImmediateRewards(int a);

    /// initial belief state
    public BeliefState getInitialBeliefState();

    /** Get the number of states.
	@return the number of states */
    public int nrStates();

    /** Get the number of actions.
	@return the number of actions */
    public int nrActions();

    /** Get the number of observations.
	@return the number of observations */
    public int nrObservations();

    /** Get the Gamma value.
	@return the gamma value*/
    public double getGamma();

    /** Get the name of an action.
	@param a the action
 	@return the name of the action*/
    public String getActionString(int a);

    /** Get the name of an observation.
	@param o the action
 	@return the name of the action*/
    public String getObservationString(int o);

} // Pomdp
