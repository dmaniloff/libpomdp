/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: Pomdp.java
 * Description: interface to represent Pomdp problem specifications
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

public interface Pomdp {

    /// P(o|b,a) in vector form for all o's
    public double[] P_Oba(BeliefState b, int a);

    /// tao(b,a,o)
    public BeliefState tao(BeliefState b, int a, int o);

    /// R(b,a)
    public double Rba(BeliefState b, int a);

    /// T(s,a,s'): s x s' matrix
    /// will generally be used by mdp.java
    public double[][] getT(int a);

    /// R(s,a): 1 x s vector
    public double[] getR(int a);

    /// nrSta: total # of states
    public int getnrSta();

    /// nrAct: # of actions
    public int getnrAct();

    /// nrObs: total # of observations
    public int getnrObs();

    /// \gamma
    public double getGamma();

    /// initial belief state
    public BeliefState getInit();

    /// action names
    public String getactStr(int a);

    /// observation names
    public String getobsStr(int o);

} // Pomdp