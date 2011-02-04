package libpomdp.common.add;

import libpomdp.common.Utils;
import libpomdp.common.add.symbolic.ParseSPUDD;

public class AddConfiguration {
    // number of state variables
    public int nrStaV;

    // id of state variables
    public int staIds[];

    // id of prime state variables
    public int staIdsPr[];

    // arity of state variables
    public int staArity[];

    // total number of states
    public int totnrSta;

    // number of observation variables
    public int nrObsV;

    // id of observation variables
    public int obsIds[];

    // id of prime observation variables
    public int obsIdsPr[];

    // arity of observation variables
    public int obsArity[];

    // total number of observations
    public int totnrObs;

    // total number of variables
    public int nrTotV;

    // number of actions
    public int nrAct;
    
    // action names
    public String actStr[];
    
    public AddConfiguration(ParseSPUDD problemAdd){
    	nrStaV = problemAdd.nStateVars;
    	nrObsV = problemAdd.nObsVars;
    	nrTotV = nrStaV + nrObsV;
    	nrAct = problemAdd.actTransitions.size();
    	staIds = new int[nrStaV];
    	staIdsPr = new int[nrStaV];
    	staArity = new int[nrStaV];
    	obsIds = new int[nrObsV];
    	obsIdsPr = new int[nrObsV];
    	obsArity = new int[nrObsV];
    	actStr = new String[nrAct];
    	// get variable ids, arities and prime ids
    	int c, a;
    	for (c = 0; c < nrStaV; c++) {
    	    staIds[c] = c + 1;
    	    staIdsPr[c] = c + 1 + nrTotV;
    	    staArity[c] = problemAdd.valNames.get(c).size();
    	}
    	for (c = 0; c < nrObsV; c++) {
    	    obsIds[c] = nrStaV + c + 1;
    	    obsIdsPr[c] = nrStaV + c + 1 + nrTotV;
    	    obsArity[c] = problemAdd.valNames.get(nrStaV + c).size();
    	}
    	for (a = 0; a < nrAct; a++) {
    	    actStr[a] = problemAdd.actNames.get(a);
    	}
    	// compute total nr of states and obs
    	totnrSta = Utils.product(staArity);
    	totnrObs = Utils.product(obsArity);

    }

}
