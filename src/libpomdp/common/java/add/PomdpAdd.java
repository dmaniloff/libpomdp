/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: pomdpAdd.java
 * Description: class to represent pomdp problems in factored form using
 *              ADDs - problems are specified in the subset of SPUDD
 *              defined by Poupart and parsed using his code - 
 *              several routine methods here are inspired from Poupart's
 *              matlab code for manipulating ADDs
 *              see README reference [5]
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.add;

// imports
import java.util.ArrayList;

import libpomdp.common.java.BelState;
import libpomdp.common.java.Pomdp;
import libpomdp.common.java.symbolic.DD;
import libpomdp.common.java.symbolic.OP;
import libpomdp.common.java.symbolic.ParseSPUDD;

import org.math.array.DoubleArray;
import org.math.array.IntegerArray;

public class PomdpAdd implements Pomdp {
    	
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------
	
    // number of state variables
    private int nrStaV;

    // id of state variables
    public int staIds[];

    // id of prime state variables
    public int staIdsPr[];
    
    // arity of state variables
    public int staArity[];

    // total number of states
    private int totnrSta;

    // number of observation variables
    private int nrObsV;

    // id of observation variables
    public int obsIds[];

    // of of prime observation variables
    public int obsIdsPr[];

    // arity of observation variables
    private int obsArity[];

    // total number of observations
    private int totnrObs;
	
    // total number of variables
    private int nrTotV;

    // number of actions
    private int nrAct;        
	
    // transition model: a-dim ADD[] 
    public DD T[][];

    // observation model: a-dim ADD[] 
    public DD O[][];
	
    // reward model: a-dim ADD
    public DD R[];
	
    // discount factor
    private double gamma;
	
    // action names
    private String actStr[];
	
    // starting belief
    private BelStateAdd initBelief;

    // ParseSPUDD parser - Poupart's parsing class
    public ParseSPUDD problemAdd;
    
    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------

    /// constructor
    public PomdpAdd(String spuddfile) {
	// parse SPUDD file
	problemAdd = new ParseSPUDD(spuddfile);
	problemAdd.parsePOMDP(false);
	// assign values to local vars
	nrStaV = problemAdd.nStateVars;
	nrObsV = problemAdd.nObsVars;
	nrTotV = nrStaV + nrObsV;
	nrAct  = problemAdd.actTransitions.size();
	gamma  = problemAdd.discount.getVal();
	// allocate arrays
	staIds   = new int[nrStaV];
	staIdsPr = new int[nrStaV];
	staArity = new int[nrStaV];
	obsIds   = new int[nrObsV];
	obsIdsPr = new int[nrObsV];
	obsArity = new int[nrObsV];
	T        = new DD [nrAct][];
	O        = new DD [nrAct][];
	R        = new DD [nrAct];
	actStr   = new String[nrAct];
	// get variable ids, arities and prime ids
	int c,a;
	for(c=0; c<nrStaV; c++) {
	    staIds[c]   = c + 1;
	    staIdsPr[c] = c + 1 + nrTotV;
	    staArity[c] = problemAdd.valNames.get(c).size();
	}	
	for(c=0; c<nrObsV; c++) {
	    obsIds[c]   = nrStaV + c + 1;
	    obsIdsPr[c] = nrStaV + c + 1 + nrTotV;
	    obsArity[c] = problemAdd.valNames.get(nrStaV+c).size();
	}
	// get DDs for T, O, R
	for(a=0; a<nrAct;  a++) {
	    //                                   ^ this is cptid !!!!
 	    T[a] = problemAdd.actTransitions.get(a);
	    O[a] = problemAdd.actObserve.get(a);
	    // reward for a is reward for the state - the cost of a
	    R[a] = OP.sub(problemAdd.reward, problemAdd.actCosts.get(a));
	    // if we wanted to have a model for R(s,a,s'), then we need this:
	    // actStruct.rewFn = 
	    // 		OP.addMultVarElim([actStruct.rewFn,actStruct.transFn], 
	    // 				  ddPOMDP.nVars+1:ddPOMDP.nVars+ddPOMDP.nStateVars);
	    actStr[a] = problemAdd.actNames.get(a);
	}
	// set initial belief state
	initBelief = new BelStateAdd(problemAdd.init, staIds, 0.0);
	// compute total nr of states and obs
	totnrSta = IntegerArray.product(staArity);
        totnrObs = IntegerArray.product(obsArity);
    } // constructor

    /**
     * P(o|b,a) in vector form for all o's
     * use ADDs and convert to array
     * used to quicky identify zero-prob obs and
     * avoid bulding an or node for those beliefs
     */
    public double[] P_Oba(BelState bel, int a) {
	// obtain subclass and the dd for this belief
	//DD b = ((belStateAdd)bel).bAdd;
	// declarations
	DD     b1[];
	DD     pObadd;
	double pOba[];
	if (bel instanceof BelStateAdd) {
	    b1 = new DD[1];
	    b1[0] = ((BelStateAdd)bel).bAdd;
	} else {
	    b1 = ((BelStateFactoredAdd)bel).marginals;
	}
	// O_a * T_a * b1
	DD[]  vars  = concat(b1, T[a], O[a]);
	int[] svars = IntegerArray.merge(staIds, staIdsPr);
	pObadd      = OP.addMultVarElim(vars, svars);
	pOba        = OP.convert2array(pObadd, obsIdsPr);
	return pOba;
    }

    /**
     *  tao(b,a,o):
     *  compute new belief state from current and a,o pair
     */
    public BelState tao(BelState bel, int a, int o) {
	if (bel instanceof BelStateAdd) {
	    return regulartao ((BelStateAdd)bel, a, o);
	} else {
	    return factoredtao((BelStateFactoredAdd)bel, a, o);
	}
    }

    /**
     * regulartao(b,a,o):
     * compute new belief state from current and a,o pair
     * uses DD representation and functions from Symbolic Perseus
     * this function re-computes poba to normalize the belief,
     * need to think of a clever way to avoid this...
     */
    public BelState regulartao(BelStateAdd bel, int a, int o) {	    
	// obtain subclass and the dd for this belief 
	DD b1 = bel.bAdd;
	DD b2;
	DD oProb;
	BelState bPrime;
	DD O_o[];
	int oc[][];
	// restrict the prime observation variables to the ones that occurred
	oc  = IntegerArray.mergeRows(obsIdsPr, sdecode(o, nrObsV, obsArity));
	//System.out.println(IntegerArray.toString(oc));
	O_o = OP.restrictN(O[a], oc); 
	DD[] vars = concat(b1, T[a], O_o);
    	// compute var elim on O * T * b
	b2 = OP.addMultVarElim(vars, staIds);
	// prime the b2 DD 
	b2 = OP.primeVars(b2, -nrTotV);
	// compute P(o|b,a)
	oProb  = OP.addMultVarElim(b2, staIds);
	// make sure we can normalize
	if (oProb.getVal() < 0.00001) {
	    // this branch will have poba = 0.0 - also reset to init
	    bPrime = initBelief;
	} else {
	    // safe to normalize now
	    b2 = OP.div(b2, oProb);	    
	    bPrime = new BelStateAdd(b2, staIds, oProb.getVal());
	}
	// return
	return bPrime;
    }

    /**
     *  factoredtao(b,a,o):
     *  compute new belief state from current and a,o pair
     *  uses DD representation and functions from Symbolic Perseus
     *  uses the product of marginals to approximate a belief
     */
    public BelState factoredtao(BelStateFactoredAdd bel, int a, int o) {	    
	// declarations
	DD       b1[] = bel.marginals;	
	DD       b2[];
	DD       b2u[] = new DD[nrStaV];
	BelState bPrime;
	DD       O_o[];
	int      oc[][];	
	// restrict the prime observation variables to the ones that occurred
	oc  = IntegerArray.mergeRows(obsIdsPr, sdecode(o, nrObsV, obsArity));
	O_o = OP.restrictN(O[a], oc); 
	// gather all necessary ADDs for variable elimination
	DD[] vars = concat(b1, T[a], O_o);
    	// compute var elim on O * T * b
	b2 = OP.marginals(vars, staIdsPr, staIds);
	// unprime the b2 DD 
	for(int i=0; i<nrStaV; i++) b2u[i] = OP.primeVars(b2[i], -nrTotV);
	// no need to normalize, done inside OP.marginals()	    
	bPrime = new BelStateFactoredAdd(b2u, staIds);
	// return
	return bPrime;
    }
    
    /// R(b,a)
    /// Poupart's matlab code has a loop indexed over
    /// 1:length(POMDP.actions(actId).rewFn) - when would this be > 1?
    public double Rba(BelState bel, int a) {
	// obtain subclass and the dd for this belief
	DD b;
	DD m[];
	if (bel instanceof BelStateAdd) {
	    b = ((BelStateAdd)bel).bAdd;
	    return OP.dotProductNoMem(b, R[a], staIds);
	} else {
	    m = ((BelStateFactoredAdd)bel).marginals;
	    return OP.factoredExpectationSparseNoMem(m, R[a]);
	}
    }

    /// return s x s' matrix with T[a]
    /// to be used by mdp.java
    public double[][] getT(int a) {
	int vars[]     = IntegerArray.merge(staIds, staIdsPr);	
	double T_a_v[] = OP.convert2array(OP.multN(T[a]),vars);
	//	double T_a[][] = new double[totnrSta][totnrSta];
	double T_a[][] = DoubleArray.fill(totnrSta, totnrSta, 0.0);
	int i,j;
	// convert this vector into an s x s' matrix columnwise
	for(j=0; j<totnrSta; j++) {
	    for(i=0; i<totnrSta; i++) {
		T_a[i][j] = T_a_v[j*totnrSta+i];
	    }
	}
	// transpose so that we have s' x s and maintain Spaans convention
	//return DoubleArray.transpose(T_a);
	return T_a;
    }

    /// return s' x o matrix with O[a]
    /// this will prob become part of the interface as well...
    public double[][] getO(int a) {
	int vars[]     = IntegerArray.merge(staIdsPr, obsIdsPr);	
	double O_a_v[] = OP.convert2array(OP.multN(O[a]),vars);
	//	double O_a[][] = new double[totnrSta][totnrSta];
	double O_a[][] = DoubleArray.fill(totnrSta, totnrObs, 0.0);
	int i,j;
	// convert this vector into an s' x o matrix columnwise
	for(j=0; j<totnrObs; j++) {
	    for(i=0; i<totnrSta; i++) {
		O_a[i][j] = O_a_v[j*totnrSta+i];
	    }
	}
	// return
	return O_a;
    }
    
    /// R(s,a)
    public double[] getR(int a) {
	DD R = 	OP.sub(problemAdd.reward, problemAdd.actCosts.get(a));
	return OP.convert2array(R, staIds);
    }

    /// R(s,a), s given in factored form
    public double getReward(int[] factoredS, int a) {
	return OP.eval(R[a], IntegerArray.mergeRows(staIds, factoredS));
    }

    /// nrSta is the product of the arity of
    /// each state variable in the DBN
    public int getnrSta() {
        return totnrSta;
    }

    /// nrAct
    public int getnrAct() {
        return nrAct;
    }

    /// nrObs is the product of the arity of
    /// each observation variable in the DBN
    public int getnrObs() {
        return totnrObs;
    }

    /// \gamma
    public double getGamma() {
        return gamma;
    }

    /// get initial belief state
    public BelState getInit() {
	return initBelief;
    }

    // takes an action starting from 0
    public String getactStr(int a) {
        return actStr[a];
    }
    
    /// string describing the values each obs var took
    /// the observation starts from 0
    public String getobsStr(int o) {
        int[] a = sdecode(o, nrObsV, obsArity);
	String v="";
	int c;
	for(c=0; c<nrObsV; c++) {
	    v=v.concat(problemAdd.varNames.get(nrStaV+c)+"="+
		       problemAdd.valNames.get(nrStaV+c).get(a[c]-1)+", ");
	}
	return v;
    }

    // ------------------------------------------------------------------------
    // utility methods particular to this representation
    // ------------------------------------------------------------------------

    /// this one might become part of the interface in the future
    /// actions start from 0, but the state from 1
    public int[] sampleNextState(int[] state, int action) {
	// we receive the factored representation of the state
	// whereby each element of the array contains the value of each of
	// the state variables - there are no var ids here
	int factoredS[][]  = IntegerArray.mergeRows(staIds, state); 
	DD[]  restrictedT  = OP.restrictN(T[action], factoredS);
	int factoredS1[][] = OP.sampleMultinomial(restrictedT, staIdsPr);
	System.out.println(IntegerArray.toString(factoredS1));
	// and we don't return any var ids either
	return factoredS1[1];
    }

    /// this one might become part of the interface in the future
    /// actions start from 0, but the states from 1 and the returned
    /// observation also starts from 1
    public int[] sampleObservation(int[] s, int[] s1, int action) {
	// we receive the factored representation of the state
	// whereby each element of the array contains the value of each of
	// the state variables - there are no var ids here
	int[] ids  = IntegerArray.merge(staIds, staIdsPr);
	int[] vals = IntegerArray.merge(s, s1);
	int[][] restriction = IntegerArray.mergeRows(ids, vals);
	DD[] restrictedO = OP.restrictN(O[action], restriction);
	int factoredO[][]   = OP.sampleMultinomial(restrictedO, obsIdsPr);
	// and we don't return any var ids either
	return factoredO[1];
    }

    // compute list of possible initial states given the
    // initial belief state specified by the POMDP
    public int [] getListofInitStates() {
	ArrayList<Integer> states = new ArrayList<Integer>();
	int factoredS[][];
	for (int r=0; r<totnrSta; r++) {
	    factoredS = IntegerArray.mergeRows(staIds, 
					       sdecode(r,
						       nrStaV,
						       staArity));
	    if (OP.eval(initBelief.bAdd, factoredS) > 0)
		states.add(r);
	}
	int s[] = new int[states.size()];
	for(int i=0; i<s.length; i++) s[i] = states.get(i).intValue();
	return s;
    }

    public int getnrTotV() {
	return nrTotV;
    }

    public int getnrStaV() {
	return nrStaV;
    }

    public int getnrObsV() {
	return nrObsV;
    }

    public int[] getobsArity() {
	return obsArity;
    }

    /// transform a given alpha vector with respect to an a,o pair
    /// g_{a,o}^i = \sum_{s'} O(o,s',a) T(s,a,s') \alpha^i(s')
    public DD gao(DD alpha, int a, int o) {
	DD gao;
	DD primedAlpha;
	DD O_o[];
	DD vars[];
	int oc[][];
	// alpha(s')
	primedAlpha = OP.primeVars(alpha, nrTotV);
	// restrict the O model to o
	oc = IntegerArray.mergeRows(obsIdsPr, sdecode(o, nrObsV, obsArity));
	O_o = OP.restrictN(O[a], oc); 
	vars = concat(primedAlpha, T[a], O_o);
    	// compute var elim on O * T * \alpha(s')
	gao = OP.addMultVarElim(vars, staIdsPr);
	return gao;
    }

    /// print a factored representation of a state
    public String printS(int factoredS[][]) {
	if(factoredS.length != 2 || factoredS[0].length != nrStaV) {
	    System.err.println("Unexpected factored state matrix");
	    return null;
	}
	String v="";
	int c;
	for(c=0; c<nrStaV; c++) {
	    v=v.concat(problemAdd.varNames.get(c)+"="+
		       problemAdd.valNames.get(c).get(factoredS[1][c]-1)+", ");
	}
	return v;
    } // printS

    /// print a factored representation of an observation
    /// takes factoredO starting from 1
    public String printO(int factoredO[][]) {
	if(factoredO.length != 2 || factoredO[0].length != nrObsV) {
	    System.err.println("Unexpected factored state matrix");
	    return null;
	}
	String v="";
	int c;
	for(c=0; c<nrObsV; c++) {
	    v=v.concat(problemAdd.varNames.get(nrStaV+c)+"="+
		       problemAdd.valNames.get(nrStaV+c).get(factoredO[1][c]-1)+", ");
	}
	return v;
    } // printO

    /**
     * sdecode
     * map an assignment id from
     * [0, IntegerArray.product(sizes)-1] to an array with
     * the corresponding joint assignment of each variable
     * when all entries in sizes are the same, this becomes a
     * change of base
     */
    public int[] sdecode(int sid, int n, int sizes[]) {
	// make sure sid is in the right range
	if (sid < 0 || sid > IntegerArray.product(sizes) - 1) {
	    System.out.println("Error calling sdecode");
	    return null;
	}
	// calculate joint assignment
	int q  = sid;
	int ja[] = IntegerArray.fill(n, 0);
	for(int i=0; i<n; i++) {
	    if (q==0) break;
	    ja[i] = q % sizes[i];
	    q = q / sizes[i];
	}
	// add 1 to each entry to comply with format
	for(int i=0; i<n; i++) ja[i]++;
	return ja;
    }

    /// encode state, complement of sdecode
    /// receives factored state starting from 1
    /// and returns factored state in the same form
    public int sencode(int fstate[], int n, int sizes[]) {
	// make sure fstate is in the right range
	// subtract 1 for format
	// for(int i=0; i<n; i++) fstate[i]--; // BUG HERE!
	int s = 0;
	int f = 1;
	for(int i=0;i<n;i++) {
	    s += f * (fstate[i] - 1); // remember that fstate starts from 1
	    f *= sizes[i];
	}
	// back to format from 1
	return s + 1;
    }

    // concatenate DD arrays - need to replace this with the
    // routine in Common.java
    private DD[] concat(DD[] first, DD[]... rest) {
	int totalLength = first.length;
	for (DD[] array : rest) totalLength += array.length;	
	DD[] result = new DD[totalLength];
	// copy fist array
	System.arraycopy(first, 0, result, 0, first.length);
	int offset = first.length;
	for (DD[] array : rest) {
	    System.arraycopy(array, 0, result, offset, array.length);
	    offset += array.length;
	}
	return result;
    }

    // first arg is not an array - IDEM as before
    private DD[] concat(DD f, DD[]... rest) {
	DD[] first = new DD[1];
	first[0]   = f;
	return concat(first,rest);
    }

} // addpomdp