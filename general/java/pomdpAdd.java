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
import org.math.array.*;

public class pomdpAdd implements pomdp {
    	
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------
	
    // number of state variables
    public int nrStaV;

    // id of state variables
    public int staIds[];

    // id of prime state variables
    public int staIdsPr[];
    
    // arity of state variables
    public int staArity[];

    // number of observation variables
    public int nrObsV;

    // id of observation variables
    public int obsIds[];

    // of of prime observation variables
    public int obsIdsPr[];

    // arity of observation variables
    public int obsArity[];
	
    // total number of variables
    public int nrTotV;

    // number of actions
    public int nrAct;
    
    // observation model: a-dim ADD[] 
    private DD O[][];
	
    // transition model: a-dim ADD[] 
    private DD T[][];
	
    // reward model: a-dim ADD
    public DD R[];
	
    // discount factor
    public double gamma;
	
    // action names
    //private String actStr[];
	
    // observation names
    //private String obsStr[];

    // starting belief
    public DD init;

    // ParseSPUDD parser - Poupart's parsing class
    public ParseSPUDD problem;
    
    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------

    /// constructor
    public pomdpAdd(String spuddfile) {
	// parse SPUDD file
	problem = new ParseSPUDD(spuddfile);
	problem.parsePOMDP(false);
	// assign values to local vars
	nrStaV = problem.nStateVars;
	nrObsV = problem.nObsVars;
	nrTotV = nrStaV + nrObsV;
	nrAct  = problem.actTransitions.size();
	gamma  = problem.discount.getVal();
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
	// get variable ids, arities and prime ids
	int c,a;
	for(c=0; c<nrStaV; c++) {
	    staIds[c]   = c + 1;
	    staIdsPr[c] = c + 1 + nrTotV;
	    staArity[c] = problem.valNames.get(c).size();
	}	
	for(c=0; c<nrObsV; c++) {
	    obsIds[c]   = nrStaV + c + 1;
	    obsIdsPr[c] = nrStaV + c + 1 + nrTotV;
	    obsArity[c] = problem.valNames.get(nrStaV+c).size();
	}
	// get DDs for transitions
	for(a=0; a<nrAct;  a++) {
	    //                                   ^ this is cptid !!!!
	    //T[a] = problem.actTransitions.get(a)[0];
	    //O[a] = problem.actObserve.get(a)[0];
 	    T[a] = problem.actTransitions.get(a);
	    O[a] = problem.actObserve.get(a);
	    // reward for a is reward for the state - the cost of a
	    R[a] = OP.sub(problem.reward, problem.actCosts.get(a));
	}
    }

    /**
     * P(o|b,a) in vector form for all o's
     * use ADDs and convert to array
     */
    public double[] P_Oba(belState bel, int a) {
	// obtain subclass and the dd for this belief
	DD b = ((belStateDD)bel).ddB;	
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
	DD b1 = ((belStateDD)bel).ddB;
	DD b2;
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
	    System.err.println("Zero prob observation - resetting to init");
	    b2 = init;
	    // make this branch not selectable by the heuristic
	    oProb = 0.0;
	}
	// store P(o|b,a)
	//on.poba = oProb;

	// safe to normalize now
	b2 = OP.div(b2, OP.addMultVarElim(b2, staIds));
	// return
	return new belStateDD(b2, staIds, oProb); 
    }
    
    /// R(b,a)
    // Poupart's matlab code has a loop indexed over
    // 1:length(POMDP.actions(actId).rewFn) - when would this be > 1?
    public double Rba(belState bel, int a) {
	// obtain subclass and the dd for this belief
	DD b = ((belStateDD)bel).ddB;
        return OP.dotProduct(b, R[a], staIds);
    }

    /// nrSta is the product of the arity of
    /// each state variable in the DBN
    public int getnrSta() {
        return IntegerArray.product(staArity);
    }

    /// nrAct
    public int getnrAct() {
        return nrAct;
    }

    /// nrObs is the product of the arity of
    /// each observation variable in the DBN
    public int getnrObs() {
        return IntegerArray.product(obsArity);
    }

    /// \gamma
    public double getGamma() {
        return gamma;
    }

    public String[] getactStr() {
        return null;
    }

    public String[] getobsStr() {
        return null;
    }

    // ------------------------------------------------------------------------
    // utility methods
    // ------------------------------------------------------------------------

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