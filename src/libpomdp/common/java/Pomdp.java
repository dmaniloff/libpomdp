/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: pomdp.java
 * Description: interface to represent pomdp problem specifications
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

public interface Pomdp {

    /// P(o|b,a) in vector form for all o's
    public Vector sampleObservationProbs(BeliefState b, int a);
    
    /// tao(b,a,o)
    public BeliefState sampleNextBelief(BeliefState b, int a, int o);

    ///R(b,a)
    public double sampleReward(BeliefState b, int a);

    /// T(s,a,s'): s x s' matrix
    /// will generally be used by mdp.java
    public Matrix getTransitionProbs(int a);

    public Matrix getObservationProbs(int a);
    
    /// R(s,a): 1 x s vector
    public Vector getRewardValues(int a);

    /// nrSta: total # of states
    public int nrStates();

    /// nrAct: # of actions
    public int nrActions();

    /// nrObs: total # of observations
    public int nrObservations();

    /// \gamma
    public double getGamma();

    /// initial belief state
    public BeliefState getInitialBelief();

    /// action names
    public String getActionString(int a);

    /// observation names
    public String getObservationString(int o);

} // pomdp