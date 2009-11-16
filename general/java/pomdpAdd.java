/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: pomdpAdd.java
 * Description: class to represent pomdp problems in factored form using
 *              ADDs - problems are specified in the subset of SPUDD
 *              defined by Poupart and parsed using his code - 
 *              several routine methods here are inspired from Poupart's
 *              matlab code for manipulating ADDs
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import java.util.*;
import org.math.array.*;

public class pomdpAdd implements pomdp {
    	
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
    public int obsArity[];

    // total number of observations
    private int totnrObs;
	
    // total number of variables
    public int nrTotV;

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
	
    // observation names
    //    public ArrayList<String>[] obsStr;

    // starting belief
    private belStateAdd initBelief;

    // ParseSPUDD parser - Poupart's parsing class
    public ParseSPUDD problemAdd;
    
    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------

    /// constructor
    public pomdpAdd(String spuddfile) {
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
	//obsStr   = new ArrayList<String>[nrObsV];
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
	    //obsStr[c].add(problemAdd.varNames.get(nrStaV+c));
	}
	// get DDs for T, O, R
	for(a=0; a<nrAct;  a++) {
	    //                                   ^ this is cptid !!!!
	    //T[a] = problemAdd.actTransitions.get(a)[0];
	    //O[a] = problemAdd.actObserve.get(a)[0];
 	    T[a] = problemAdd.actTransitions.get(a);
	    O[a] = problemAdd.actObserve.get(a);
	    // reward for a is reward for the state - the cost of a
	    R[a] = OP.sub(problemAdd.reward, problemAdd.actCosts.get(a));
	    actStr[a] = problemAdd.actNames.get(a);
	}
	// set initial belief state
	initBelief = new belStateAdd(problemAdd.init, staIds, 0.0);
	// compute total nr of states and obs
	totnrSta = IntegerArray.product(staArity);
        totnrObs = IntegerArray.product(obsArity);
    } // constructor

    /**
     * P(o|b,a) in vector form for all o's
     * use ADDs and convert to array
     */
    public double[] P_Oba(belState bel, int a) {
	// obtain subclass and the dd for this belief
	DD b = ((belStateAdd)bel).ddB;	
	DD pObadd;
	double[]pOba;
	DD[] vars   = concat(b, T[a], O[a]);
	int[] svars = IntegerArray.merge(staIds, staIdsPr);
	pObadd      = OP.addMultVarElim(vars,svars);
	pOba        = OP.convert2array(pObadd,obsIdsPr);
	return pOba;
    }

    /**
     *  tao(b,a,o):
     *  compute new belief state from current and a,o pair
     *  uses DD representation and functions from Symbolic Perseus
     *  stores poba in the orNode o to avoid re-computation
     */
    public belState tao(belState bel, int a, int o) {	    
	// obtain subclass and the dd for this belief 
	DD b1 = ((belStateAdd)bel).ddB;
	DD b2;
	belState bPrime;
	DD O_o[];
	int oc[][];
	double oProb = 0.0;
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
	oProb  = OP.addMultVarElim(b2, staIds).getVal();
	// make sure we can normalize
	if (oProb < 0.00001) {
	    // System.err.println("Zero prob observation - resetting to init"); -!!!!!!!!!!!!!
	    // this branch will have poba = 0.0
	    bPrime = initBelief;
	} else {
	    // safe to normalize now
	    b2 = OP.div(b2, OP.addMultVarElim(b2, staIds));
	    bPrime = new belStateAdd(b2, staIds, oProb);
	}
	// return
	return bPrime;
    }
    
    /// R(b,a)
    // Poupart's matlab code has a loop indexed over
    // 1:length(POMDP.actions(actId).rewFn) - when would this be > 1?
    public double Rba(belState bel, int a) {
	// obtain subclass and the dd for this belief
	DD b = ((belStateAdd)bel).ddB;
        return OP.dotProduct(b, R[a], staIds);
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
    public belState getInit() {
	return initBelief;
    }

    public String getactStr(int a) {
        return actStr[a];
    }
    
    // string describing the values each obs var took
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
    // utility methods
    // ------------------------------------------------------------------------

    // public simulate(int s, int a) {
// 	restrictedTransFn = OP.restrictN(ddPOMDP.actions(actId).transFn, stateConfig); 
//     nextStateConfig = OP.sampleMultinomial(restrictedTransFn,stateVarsPrime);
//     restrictedObsFn = OP.restrictN(ddPOMDP.actions(actId).obsFn, [stateConfig, nextStateConfig]);
//     obsConfig = OP.sampleMultinomial(restrictedObsFn, obsVarsPrime);
//     }

    // print a factored representation of a state
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

    // print a factored representation of an observation
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
     * sdecode:
     * map an assignment id from
     * [0, IntegerArray.product(sizes)-1] to an array with
     * the corresponding joint assignment of each variable
     */
    public int[] sdecode(int sid, int n, int sizes[]) {
	// make sure sid is in the right range
	if (sid < 0 || sid > IntegerArray.product(sizes) - 1) {
	    System.out.println("Error calling sdecode");
	    return null;
	}
	// calculate joint assignment
	int i,q  = sid;
	int ja[] = IntegerArray.fill(n, 0);
	for(i=0; i<n; i++) {
	    if (q==0) break;
	    ja[i] = q % sizes[i];
	    q = q / sizes[i];
	}
	// add 1 to each entry to comply with format
	for(i=0; i<n; i++) ja[i]++;
	return ja;
    }

    /// concatenate DD arrays
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

    /// first arg is not an array
    private DD[] concat(DD f, DD[]... rest) {
	DD[] first = new DD[1];
	first[0]   = f;
	return concat(first,rest);
    }


    /// compare two belief vectors to a given accuracy - used??
    /* 
     * public boolean equalB(double[] b1, double[] b2) {
     * 	double acc = 0.00001;
     * 	double diff[];
     * 	diff = LinearAlgebra.minus(b1,b2);
     * 	return (DoubleArray.max(DoubleArray.f(diff,abs)) <= acc);
     * }
     * 
     * // define absolute value function to apply to vectors
     * private Function abs = new Function() { 
     * 	    public double f(double x) { return Math.abs(x); }};
     */

    // public double[][][] getT() {
    //     return T;
    // }

    // public double[][] getR() {
    //     return R;
    // }    
    

} // addpomdp