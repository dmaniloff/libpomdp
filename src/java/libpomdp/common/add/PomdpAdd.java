/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: PomdpAdd.java
 * Description: class to represent pomdp problems in factored form using
 *              ADDs - problems are specified in the subset of SPUDD
 *              defined by Poupart and parsed using his code - 
 *              several routine methods here are inspired from Poupart's
 *              matlab code for manipulating ADDs
 *              see README reference [5]
 * Copyright (c) 2009, 2010, 2011 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.common.add;

// imports
import java.util.ArrayList;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefMdp;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;
import libpomdp.common.ObservationModel;
import libpomdp.common.Pomdp;
import libpomdp.common.RewardFunction;
import libpomdp.common.TransitionModel;
import libpomdp.common.Utils;
import libpomdp.common.ValueFunction;
import libpomdp.common.add.symbolic.DD;
import libpomdp.common.add.symbolic.OP;
import libpomdp.common.add.symbolic.ParseSPUDD;

public class PomdpAdd implements Pomdp {

    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

	private AddConfiguration conf;
	
    // transition model: a-dim ADD[]
    public TransitionModelAdd T;

    // observation model: a-dim ADD[]
    public ObservationModelAdd O;

    // reward model: a-dim ADD
    public RewardFunctionAdd R;

    // discount factor
    private double gamma;

    // starting belief
    private BeliefStateAdd initBelief;

    // ParseSPUDD parser - Poupart's parsing class
    public ParseSPUDD problemAdd;

    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------

    // / constructor
    public PomdpAdd(String spuddfile) {
    	Utils.error("Adds are disable in the maray's branch for a while... I promise to fix them soon");
	// parse SPUDD file
	problemAdd = new ParseSPUDD(spuddfile);
	problemAdd.parsePOMDP(false);
	// assign values to local vars
	conf=new AddConfiguration(problemAdd);
	gamma = problemAdd.discount.getVal();
	// allocate arrays
	
	T = new TransitionModelAdd(problemAdd.actTransitions,conf);
	O = new ObservationModelAdd(problemAdd.actObserve,conf);
	R = new RewardFunctionAdd(problemAdd.reward, problemAdd.actCosts,conf);
	// get DDs for T, O, R
	
	// set initial belief state
	initBelief = new BeliefStateAdd(problemAdd.init, conf.staIds, 0.0);

    } // constructor

    /**
     * tao(b,a,o): compute new belief state from current and a,o pair
     */
    
    public BeliefState nextBeliefState(BeliefState bel, int a, int o) {
	if (bel instanceof BeliefStateAdd) {
	    return regulartao((BeliefStateAdd) bel, a, o);
	} else {
	    return factoredtao((BeliefStateFactoredAdd) bel, a, o);
	}
    }

    /**
     * regulartao(b,a,o): compute new belief state from current and a,o pair
     * uses DD representation and functions from Symbolic Perseus this function
     * re-computes poba to normalize the belief, need to think of a clever way
     * to avoid this...
     */
    public BeliefState regulartao(BeliefStateAdd bel, int a, int o) {
	// obtain subclass and the dd for this belief
	DD b1 = bel.bAdd;
	DD b2;
	DD oProb;
	BeliefState bPrime;
	DD O_o[];
	int oc[][];
	// restrict the prime observation variables to the ones that occurred
	oc = Utils.join(conf.obsIdsPr,
		Utils.sdecode(o, conf.nrObsV, conf.obsArity));
	// System.out.println(IntegerArray.toString(oc));
	O_o = OP.restrictN(O.model[a], oc);
	DD[] vars = Utils.concat(b1, T.model[a], O_o);
	// compute var elim on O * T * b
	b2 = OP.addMultVarElim(vars, conf.staIds);
	// prime the b2 DD
	b2 = OP.primeVars(b2, -conf.nrTotV);
	// compute P(o|b,a)
	oProb = OP.addMultVarElim(b2, conf.staIds);
	// make sure we can normalize
	if (oProb.getVal() < 0.00001) {
	    // this branch will have poba = 0.0 - also reset to init
	    bPrime = initBelief;
	} else {
	    // safe to normalize now
	    b2 = OP.div(b2, oProb);
	    bPrime = new BeliefStateAdd(b2, conf.staIds, oProb.getVal());
	}
	// return
	return bPrime;
    }

    /**
     * factoredtao(b,a,o): compute new belief state from current and a,o pair
     * uses DD representation and functions from Symbolic Perseus uses the
     * product of marginals to approximate a belief
     */
    public BeliefState factoredtao(BeliefStateFactoredAdd bel, int a, int o) {
	// declarations
	DD b1[] = bel.marginals;
	DD b2[];
	DD b2u[] = new DD[conf.nrStaV];
	BeliefState bPrime;
	DD O_o[];
	int oc[][];
	// restrict the prime observation variables to the ones that occurred
	oc = Utils.join(conf.obsIdsPr,
		Utils.sdecode(o, conf.nrObsV, conf.obsArity));
	O_o = OP.restrictN(O.model[a], oc);
	// gather all necessary ADDs for variable elimination
	DD[] vars = Utils.concat(b1, T.model[a], O_o);
	// compute var elim on O * T * b
	b2 = OP.marginals(vars, conf.staIdsPr, conf.staIds);
	// unprime the b2 DD
	for (int i = 0; i < conf.nrStaV; i++)
	    b2u[i] = OP.primeVars(b2[i], -conf.nrTotV);
	// no need to normalize, done inside OP.marginals()
	bPrime = new BeliefStateFactoredAdd(b2u, conf.staIds);
	// return
	return bPrime;
    }

    // / R(b,a)
    // / Poupart's Matlab code has a loop indexed over
    // / 1:length(POMDP.actions(actId).rewFn) - when would this be > 1?
    public double expectedImmediateReward(BeliefState bel, int a) {
	// obtain subclass and the dd for this belief
	DD b;
	DD m[];
	if (bel instanceof BeliefStateAdd) {
	    b = ((BeliefStateAdd) bel).bAdd;
	    return OP.dotProductNoMem(b, R.model[a], conf.staIds);
	} else {
	    m = ((BeliefStateFactoredAdd) bel).marginals;
	    return OP.factoredExpectationSparseNoMem(m, R.model[a]);
	}
    }

    /**
     * P(o|b,a) in vector form for all o's use ADDs and convert to array used to
     * quickly identify zero-prob obs and avoid building an or node for those
     * beliefs
     */
   
    public AlphaVectorAdd observationProbabilities(BeliefState bel, int a) {
	// obtain subclass and the dd for this belief
	// DD b = ((BeliefStateAdd)bel).bAdd;
	// declarations
	DD b1[];
	DD pObadd;
	//double pOba[];
	if (bel instanceof BeliefStateAdd) {
	    b1 = new DD[1];
	    b1[0] = ((BeliefStateAdd) bel).bAdd;
	} else {
	    b1 = ((BeliefStateFactoredAdd) bel).marginals;
	}
	// O_a * T_a * b1
	DD[] vars = Utils.concat(b1, T.model[a], O.model[a]);
	int[] svars = Utils.concat(conf.staIds, conf.staIdsPr);
	pObadd = OP.addMultVarElim(vars, svars);
	return(new AlphaVectorAdd(pObadd,-1));
	//pOba = OP.convert2array(pObadd, conf.obsIdsPr);
	//return new AlphaVectorAdd(new CustomVector(pOba);
    }

	public CustomVector observationProbabilitiesVector(BeliefState bel, int a) {
		return new CustomVector(OP.convert2array(observationProbabilities(bel,a).v, conf.obsIdsPr));
	}

    
    // / return s x s' matrix with T[a]
    // / to be used by mdp.java
    
    public CustomMatrix getTransitionTable(int a) {
	int vars[] = Utils.concat(conf.staIds, conf.staIdsPr);
	double T_a_v[] = OP.convert2array(OP.multN(T.model[a]), vars);
	// double T_a[][] = new double[totnrSta][totnrSta];
	CustomMatrix T_a = new CustomMatrix(conf.totnrSta, conf.totnrSta);
	int i, j;
	// convert this vector into an s x s' matrix columnwise
	for (j = 0; j < conf.totnrSta; j++) {
	    for (i = 0; i < conf.totnrSta; i++) {
		T_a.set(i,j,T_a_v[j * conf.totnrSta + i]);
	    }
	}
	// transpose so that we have s' x s and maintain Spaans convention
	// return DoubleArray.transpose(T_a);
	return new CustomMatrix(T_a);
    }

    // / return s' x o matrix with O[a]
    // / this will prob become part of the interface as well...
    
    public CustomMatrix getObservationTable(int a) {
	int vars[] = Utils.concat(conf.staIdsPr, conf.obsIdsPr);
	double O_a_v[] = OP.convert2array(OP.multN(O.model[a]), vars);
	// double O_a[][] = new double[totnrSta][totnrSta];
	CustomMatrix O_a = new CustomMatrix(conf.totnrSta, conf.totnrObs);
	int i, j;
	// convert this vector into an s' x o matrix columnwise
	for (j = 0; j < conf.totnrObs; j++) {
	    for (i = 0; i < conf.totnrSta; i++) {
		O_a.set(i, j, O_a_v[j * conf.totnrSta + i]);
	    }
	}
	// return
	return new CustomMatrix(O_a);
    }

    // / R(s,a)
    
    public CustomVector getImmediateRewards(int a) {
	DD R = OP.sub(problemAdd.reward, problemAdd.actCosts.get(a));
	return new CustomVector(OP.convert2array(R, conf.staIds));
    }

    // / get initial belief state
    
    public BeliefState getInitialBeliefState() {
	return initBelief;
    }

    // / nrSta is the product of the arity of
    // / each state variable in the DBN
    
    public int nrStates() {
	return conf.totnrSta;
    }

    // / nrAct
    
    public int nrActions() {
	return conf.nrAct;
    }

    // / nrObs is the product of the arity of
    // / each observation variable in the DBN
    
    public int nrObservations() {
	return conf.totnrObs;
    }

    // / \gamma
    
    public double getGamma() {
	return gamma;
    }

    // takes an action starting from 0
    
    public String getActionString(int a) {
	return conf.actStr[a];
    }

    // / string describing the values each obs var took
    // / the observation starts from 0
    
    public String getObservationString(int o) {
	int[] a = Utils.sdecode(o, conf.nrObsV, conf.obsArity);
	String v = "";
	int c;
	for (c = 0; c < conf.nrObsV; c++) {
	    v = v.concat(problemAdd.varNames.get(conf.nrStaV + c) + "="
		    + problemAdd.valNames.get(conf.nrStaV + c).get(a[c] - 1) + ", ");
	}
	return v;
    }

    
    public String getStateString(int s) {
		Utils.error("getStateString not implemented yet for Adds");
		return null;
    }

    // ------------------------------------------------------------------------
    // utility methods particular to this representation
    // ------------------------------------------------------------------------

    // / this one might become part of the interface in the future
    // / actions start from 0, but the state from 1
    public int[] sampleNextState(int[] state, int action) {
	// we receive the factored representation of the state
	// whereby each element of the array contains the value of each of
	// the state variables - there are no var ids here
	int factoredS[][] = Utils.join(conf.staIds, state);
	DD[] restrictedT = OP.restrictN(T.model[action], factoredS);
	int factoredS1[][] = OP.sampleMultinomial(restrictedT, conf.staIdsPr);
	//System.out.println(Utils.toString(factoredS1));
	// and we don't return any var ids either
	return factoredS1[1];
    }

    // / this one might become part of the interface in the future
    // / actions start from 0, but the states from 1 and the returned
    // / observation also starts from 1
    public int[] sampleObservation(int[] s, int[] s1, int action) {
	// we receive the factored representation of the state
	// whereby each element of the array contains the value of each of
	// the state variables - there are no var ids here
	int[] ids = Utils.concat(conf.staIds, conf.staIdsPr);
	int[] vals = Utils.concat(s, s1);
	int[][] restriction = Utils.join(ids, vals);
	DD[] restrictedO = OP.restrictN(O.model[action], restriction);
	int factoredO[][] = OP.sampleMultinomial(restrictedO, conf.obsIdsPr);
	// and we don't return any var ids either
	return factoredO[1];
    }

    // compute list of possible initial states given the
    // initial belief state specified by the POMDP
    public int[] getListofInitStates() {
	ArrayList<Integer> states = new ArrayList<Integer>();
	int factoredS[][];
	for (int r = 0; r < conf.totnrSta; r++) {
	    factoredS = Utils.join(conf.staIds,
		    Utils.sdecode(r, conf.nrStaV, conf.staArity));
	    if (OP.eval(initBelief.bAdd, factoredS) > 0)
		states.add(r);
	}
	int s[] = new int[states.size()];
	for (int i = 0; i < s.length; i++)
	    s[i] = states.get(i).intValue();
	return s;
    }

    public int getnrTotV() {
	return conf.nrTotV;
    }

    public int getnrStaV() {
	return conf.nrStaV;
    }

    public int getnrObsV() {
	return conf.nrObsV;
    }

    public int[] getobsIdsPr() {
	return conf.obsIdsPr;
    }

    public int[] getstaIds() {
	return conf.staIds;
    }

    public int[] getstaIdsPr() {
	return conf.staIdsPr;
    }

    public int[] getobsArity() {
	return conf.obsArity;
    }

    // / transform a given alpha vector with respect to an a,o pair
    // / g_{a,o}^i = \sum_{s'} O(o,s',a) T(s,a,s') \alpha^i(s')
    // / might want to move this function to valuefunctionADD?
    public DD gao(DD alpha, int a, int o) {
	DD gao;
	DD primedAlpha;
	DD O_o[];
	DD vars[];
	int oc[][];
	// alpha(s')
	primedAlpha = OP.primeVars(alpha, conf.nrTotV);
	// restrict the O model to o
	oc = Utils.join(conf.obsIdsPr,
		Utils.sdecode(o, conf.nrObsV, conf.obsArity));
	O_o = OP.restrictN(O.model[a], oc);
	vars = Utils.concat(primedAlpha, T.model[a], O_o);
	// compute var elim on O * T * \alpha(s')
	gao = OP.addMultVarElim(vars, conf.staIdsPr);
	return gao;
    }

    // / print a factored representation of a state
    public String printS(int factoredS[][]) {
	if (factoredS.length != 2 || factoredS[0].length != conf.nrStaV) {
	    System.err.println("Unexpected factored state matrix");
	    return null;
	}
	String v = "";
	int c;
	for (c = 0; c < conf.nrStaV; c++) {
	    v = v.concat(problemAdd.varNames.get(c) + "="
		    + problemAdd.valNames.get(c).get(factoredS[1][c] - 1)
		    + ", ");
	}
	return v;
    } // printS

    // / print a factored representation of an observation
    // / takes factoredO starting from 1
    public String printO(int factoredO[][]) {
	if (factoredO.length != 2 || factoredO[0].length != conf.nrObsV) {
	    System.err.println("Unexpected factored state matrix");
	    return null;
	}
	String v = "";
	int c;
	for (c = 0; c < conf.nrObsV; c++) {
	    v = v.concat(problemAdd.varNames.get(conf.nrStaV + c)
		    + "="
		    + problemAdd.valNames.get(conf.nrStaV + c).get(
			    factoredO[1][c] - 1) + ", ");
	}
	return v;
    } // printO

	public BeliefMdp getBeliefMdp() {
		try {
			throw new Exception("Belief-MDPs are not implemented yet for ADDs");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public AlphaVector getEmptyAlpha() {
		// TODO Auto-generated method stub
		return null;
	}

	public AlphaVector getEmptyAlpha(int a) {
		// TODO Auto-generated method stub
		return null;
	}

	public AlphaVector getHomogeneAlpha(double bestVal) {
		// TODO Auto-generated method stub
		return null;
	}

	public ObservationModel getObservationModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRandomAction() {
		// TODO Auto-generated method stub
		return 0;
	}

	public RewardFunction getRewardFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getRewardMax() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getRewardMaxMin() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getRewardMin() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ValueFunction getRewardValueFunction(int a) {
		// TODO Auto-generated method stub
		return null;
	}

	public TransitionModel getTransitionModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public AlphaVector mdpValueUpdate(AlphaVector vec, int a) {
		// TODO Auto-generated method stub
		return null;
	}

	public int sampleObservation(BeliefState b, int a) {
		// TODO Auto-generated method stub
		return 0;
	}

	public AddConfiguration getConf() {
		return conf;
	}

} // PomdpAdd

