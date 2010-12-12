
package libpomdp.common;

import libpomdp.common.std.BeliefStateStd;


/** Interface to represent pomdp problem specifications
 * @author Diego Maniloff 
 * @author Mauricio Araya
*/

public interface Pomdp {

    /** Sample observation probabilities. Specifically get the probabilities
	Pr(o|a,b) in vector form for all o's 
	@param b the belief state 
        @param a the action 
	@return the observations probabilities */
    public CustomVector sampleObservationProbs(BeliefState b, int a);
    
    /** Sample the next belief given the current belief, action and observation tuple.
	In other words, calculate the bayesian update for b. 
	@param b the belief state
        @param a the action
        @param o the observation
	@return the next belief state*/
    public BeliefState sampleNextBelief(BeliefState b, int a, int o);

    /** Sample a reward given a belief state and an action.
	For the vanilla POMDP this correspond to the expectation over the given belif state.
	@param b the belief state
        @param a the action
        @return a scalar reward*/
    public double sampleReward(BeliefState b, int a);

    /** Get the transition matrix for a given action.
    	Mathematically, corresponds to T(s,a,s')=Pr(s'|a,s), which is a |S| x |S| matrix,
    	where the distributions are the column?? vectors.
        @param a the action
	@return the transition matrix*/
    public CustomMatrix getTransitionProbs(int a);

    /** Get the observation matrix for a given action.
    	Mathematically, corresponds to O(s',a,o)=Pr(o|a,s'), which is a |S| x |O| matrix,
    	where the distributions are the column?? vectors.
        @param a the action
	@return the observation matrix*/
    public CustomMatrix getObservationProbs(int a);

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

    /** Get the initial belief state.
	@return the intial belief state */
    public BeliefState getInitialBelief();

    /** Get the name of an action.
	@param a the action
 	@return the name of the action*/
    public String getActionString(int a);

    /** Get the name of an observation.
	@param o the action
 	@return the name of the action*/
    public String getObservationString(int o);

    /** Get a random action from a uniform distribution.
	@return a random action*/
    public int getRandomAction();

    /** Get a random observation from the obsevation probabilities.
	@param b the belief state
	@param a tha action
	@return a random observation*/
    public int getRandomObservation(BeliefStateStd b, int a);
    
} // pomdp
