/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: Pomdp.java
 * Description: interface to represent Pomdp problem specifications
 * Copyright (c) 2009, 2010 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.common;

public interface Pomdp {

    /// tao(b,a,o)
    public BeliefState nextBeliefState(BeliefState b, int a, int o);

    // / R(b,a): scalar value
    public double expectedImmediateReward(BeliefState b, int a);

    /// P(o|b,a): 1 x o in vector form for all o's
    public CustomVector observationProbabilities(BeliefState bel, int a);

    
    public TransitionModel getTransitionModel();
    
    public ObservationModel getObservationModel();
    
    public RewardFunction getRewardFunction();
    
    /// T(s,a,s'): s x s' matrix
    //public CustomMatrix getTransitionTable(int a);

    /// O(s',a,o): s' x o matrix
    //public CustomMatrix getObservationTable(int a);

    /// R(s,a): 1 x s vector
    //public CustomVector getImmediateRewards(int a);

    /// initial belief state
    public BeliefState getInitialBeliefState();

    /// nrSta: total # of states
    public int nrStates();

    /// nrAct: # of actions
    public int nrActions();

    /// nrObs: total # of observations
    public int nrObservations();

    /// \gamma
    public double getGamma();

    /// action names
    public String getActionString(int a);

    /// observation names
    public String getObservationString(int o);

    /// state names
    public String getStateString(int s) throws Exception;

} // Pomdp