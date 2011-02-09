/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: 
 * Description: Represent a POMDP model using a flat representation and
 *              sparse matrices and vectors. This class can be constructed
 *              from a pomdpSpecSparseMTJ object after parsing a .POMDP file.
 *              Sparse matrices by matrix-toolkits-java, 
 *              every matrix will be CustomMatrix:
 *              
 * S =
 *  (3,1)        1
 *  (2,2)        2
 *  (3,2)        3
 *  (4,3)        4
 *  (1,4)        5
 * A =
 *   0     0     0     5
 *   0     2     0     0
 *   1     3     0     0
 *   0     0     4     0
 * Copyright (c) 2009, 2010 Diego Maniloff
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.common.java.standard;

// imports
import java.util.ArrayList;

import libpomdp.common.java.BeliefState;
import libpomdp.common.java.CustomMatrix;
import libpomdp.common.java.CustomVector;
import libpomdp.common.java.Pomdp;
import libpomdp.parser.java.DotPomdpParserStandard;
import libpomdp.parser.java.PomdpSpecStandard;

public class PomdpStandard implements Pomdp {

    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // number of states
    private int nrSta;

    // private nrAct
    private int nrAct;

    // private nrObs
    private int nrObs;

    // transition model: a x s x s'
    private CustomMatrix T[];

    // observation model: a x s' x o
    private CustomMatrix O[];

    // reward model: a x s'
    private CustomVector R[];

    // discount factor
    private double gamma;

    // action names
    private ArrayList<String>  actStr;

    // observation names
    private ArrayList<String> obsStr;
    
    // starting belief
    private BeliefStateStandard initBelief;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /// constructor - need to integrate file reader....doing this for now.

    public PomdpStandard(String pomdpFilename) {

	// specs of the problem
	PomdpSpecStandard prob = null;

	// instantiate parser and parse
	try {
	    prob =
		DotPomdpParserStandard.parse(pomdpFilename);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	init(prob.T,
		prob.O,
		prob.R,
		prob.nrSta,
		prob.nrAct,
		prob.nrObs,
		prob.discount,
		prob.actList,
		prob.obsList,
		prob.startState);
    }

    private void init(CustomMatrix[]  T, 
	    CustomMatrix[]  O, 
	    CustomVector[]  R,
	    int          nrSta, 
	    int          nrAct, 
	    int          nrObs, 
	    double       gamma,
	    ArrayList<String> actStr,
	    ArrayList<String> obsStr,
	    CustomVector init) {

	// allocate space for the pomdp models
	this.nrSta  = nrSta;
	this.nrAct  = nrAct;
	this.nrObs  = nrObs;
	this.T      = new CustomMatrix[nrAct];
	this.O      = new CustomMatrix[nrAct];
	this.R      = new CustomVector[nrAct];
	this.gamma  = gamma;
	this.actStr = actStr;
	this.obsStr = obsStr;

	// set initial belief state
	this.initBelief = new BeliefStateStandard(init, 0.0);

	// copy the model matrices 
	// sneaky one here, i wonder how long this's been like that
	for(int a = 0; a < nrAct; a++) {
	    this.T[a] = new CustomMatrix(T[a]);
	    this.O[a] = new CustomMatrix(O[a]);
	    this.R[a] = new CustomVector(R[a]);	    
	}
    } // constructor

    /// tao(b,a,o)
    @Override
    public BeliefState nextBeliefState(BeliefState b, int a, int o) {
	//long start = System.currentTimeMillis();
	//System.out.println("made it to tao");
	BeliefState bPrime;
	// compute T[a]' * b1
	CustomVector b1   = b.getPoint();
	CustomVector b2   = new CustomVector(nrSta);
	b2 = T[a].transMult(b1);
	//System.out.println("Elapsed in tao - T[a] * b1" + (System.currentTimeMillis() - start));

	// element-wise product with O[a](:,o)
	b2.elementMult(O[a].getColumn(o));
	//System.out.println("Elapsed in tao - O[a] .* b2" + (System.currentTimeMillis() - start));

	// compute P(o|b,a) - norm1 is the sum of the absolute values
	double poba = b2.norm(1.0);
	// make sure we can normalize
	if (poba < 0.00001) {
	    //System.err.println("Zero prob observation - resetting to init");
	    // this branch will have poba = 0.0
	    bPrime = initBelief;
	} else {
	    // safe to normalize now
	    b2 = b2.scale(1.0/poba);    
	    bPrime = new BeliefStateStandard(b2, poba);
	}
	//System.out.println("Elapsed in tao" + (System.currentTimeMillis() - start));
	// return
	return bPrime;
    }

    /// R(b,a)
    @Override
    public double expectedImmediateReward(BeliefState bel, int a) {
	CustomVector b = ((BeliefStateStandard)bel).bSparse;
	return b.dot(R[a]);
    }

    // P(o|b,a) in vector form for all o's
    @Override
    public CustomVector observationProbabilities(BeliefState b, int a) {
	CustomVector  b1  = b.getPoint();
	CustomVector  Tb  = new CustomVector(nrSta);
	Tb=T[a].mult(b1);
	CustomVector Poba = new CustomVector(nrObs);
	Poba = O[a].transMult(Tb);
	return Poba;
    }

    @Override
    public CustomMatrix getTransitionTable(int a) {
	return T[a].copy();
    }

    @Override
    public CustomMatrix getObservationTable(int a) {
	return O[a].copy();
    }

    @Override
    public CustomVector getImmediateRewards(int a) {
	return R[a].copy();
    }

    @Override
    public BeliefState getInitialBeliefState() {
	return initBelief.copy();
    }

    @Override
    public int nrStates() {
	return nrSta;
    }

    @Override
    public int nrActions() {
	return nrAct;
    }

    @Override
    public int nrObservations() {
	return nrObs;
    }

    @Override
    public double getGamma() {
	return gamma;
    }

    @Override
    public String getActionString(int a) {
	return actStr.get(a);
    }

    @Override
    public String getObservationString(int o) {	
	return obsStr.get(o);
    }

//    public String[] getStateString() {
//	return staStr.;
//    }

} // PomdpStandard

