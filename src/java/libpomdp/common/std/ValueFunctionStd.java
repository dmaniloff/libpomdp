/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: 
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 

 --------------------------------------------------------------------------- */

package libpomdp.common.std;

// imports
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.ValueFunction;
import libpomdp.solve.Criteria;

public class ValueFunctionStd implements ValueFunction, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 999938545519704337L;

    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // serial id

    // represent a value function via a Matrix object
    private ArrayList<AlphaVectorStd> set;
    long total_lp_time;

    // constructor
    public ValueFunctionStd() {
	set = new ArrayList<AlphaVectorStd>();
    }

    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------

    //public ValueFunctionStd(double[][] v, int[] a) {
	//this();
	//for (int i = 0; i < a.length; i++) {
	 //   push(v[i], a[i]);
	//}
    //}

    // list of actions associated with each alpha
    public int[] getActions() {
	int[] retval = new int[size()];
	for (int i = 0; i < size(); i++) {
	    retval[i] = getAction(i);
	}
	return (retval);
    }

    // return value of a belief state
    public double value(BeliefState bel) {
	// long start = System.currentTimeMillis();
	double valmax = Double.NEGATIVE_INFINITY;
	AlphaVectorStd sel = null;
	for (AlphaVectorStd alpha : set) {
	    double sol = alpha.eval(bel);
	    if (sol > valmax) {
		valmax = sol;
		sel = alpha;
	    }
	}
	bel.setAlpha(set.indexOf(sel));
	;
	return valmax;
    }

//    public boolean push(double list[], int a) {
//	return push(new CustomVector(list), a);
 //   }

    public boolean newAlpha(CustomVector vec, int a) {
	return (push(new AlphaVectorStd(vec, a)));
    }

    public boolean push(AlphaVectorStd ent) {
	return (set.add(ent));
    }

    public AlphaVectorStd getAlpha(int idx) {
	return set.get(idx);
    }

    public int size() {
	return (set.size());
    }

    public ValueFunctionStd copy() {
	ValueFunctionStd newv = new ValueFunctionStd();
	for (int i = 0; i < set.size(); i++)
	    newv.push(set.get(i).copy());
	return newv;
    }

    public int getAlphaAction(int idx) {
	return (set.get(idx).getAction());
    }

    public String toString() {
	String retval = "Value Function\n";
	for (int i = 0; i < size(); i++) {
	    retval += "v" + i + "\t[";
	    AlphaVectorStd v = getAlpha(i);
	    for (int j = 0; j < v.size(); j++) {
		retval += v.get(j) + " ";
	    }
	    retval += "] a=" + getAlphaAction(i) + "\n";
	}
	return retval;
    }

    public long prune() {
	return prune(1e-10);
    }

    public long prune(double delta) {
	domination_check(delta);
	return (lp_pruning(delta));
    }

    private long lp_pruning(double delta) {
	total_lp_time = 0;
	if (set.size() < 2)
	    return total_lp_time;
	ArrayList<AlphaVectorStd> newv = new ArrayList<AlphaVectorStd>();
	while (set.size() > 0) {
	    BeliefStateStd b;
	    AlphaVectorStd sel_vect = set.remove(0);
	    if (newv.size() == 0) {
	    b = BeliefStateStd.getUniformBelief(sel_vect.size());
	
	    } else {
		b = find_region(sel_vect, newv, delta);
	    }
	    if (b != null) {
		set.add(sel_vect);
		sel_vect = getBestAlpha(b, set, delta);
		int idx = set.indexOf(sel_vect);
		set.remove(idx);
		newv.add(sel_vect);
	    }
	}
	set = newv;
	return total_lp_time;
    }

    public AlphaVectorStd getBestAlpha(BeliefState b) {
	return (getBestAlpha((BeliefStateStd) b, this.set, 0.0));
    }

    private AlphaVectorStd getBestAlpha(BeliefStateStd b,
	    ArrayList<AlphaVectorStd> set2, double delta) {
	AlphaVectorStd best_vec = set2.get(0);
	double best_val = best_vec.eval(b);
	for (AlphaVectorStd test_vec : set2) {
	    double val = test_vec.eval(b);
	    if (Math.abs(val - best_val) < delta) {
		best_vec = lexicographic_max(best_vec, test_vec, delta);
	    } else if (val > best_val) {
		best_vec = test_vec;
	    }
	    best_val = best_vec.eval(b);
	}
	return best_vec;
    }

    private AlphaVectorStd lexicographic_max(AlphaVectorStd bestVec,
	    AlphaVectorStd testVec, double delta) {
	if (bestVec.compareTo(testVec, delta) > 0)
	    return (bestVec);
	else
	    return (testVec);
    }

    private BeliefStateStd find_region(AlphaVectorStd selVect,
	    ArrayList<AlphaVectorStd> newv, double delta) {
	// Can Sparsity play a role here?, nice question!
	BeliefStateStd bel = null;
	int states=selVect.size();
	glp_prob lp;
	glp_smcp parm;
	SWIGTYPE_p_int ind;
	SWIGTYPE_p_double val;
	lp = GLPK.glp_create_prob();
	GLPK.glp_set_prob_name(lp, "FindRegion");
	// Define Solution Vector
	GLPK.glp_add_cols(lp, states + 1);
	for (int i = 0; i < states; i++) {
	    GLPK.glp_set_col_kind(lp, i + 1, GLPKConstants.GLP_CV);
	    GLPK.glp_set_col_bnds(lp, i + 1, GLPKConstants.GLP_DB, 0.0, 1.0);
	}
	GLPK.glp_set_col_kind(lp, states + 1, GLPKConstants.GLP_CV);
	GLPK.glp_set_col_bnds(lp, states + 1, GLPKConstants.GLP_FR,
		Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	// Define Constraints
	GLPK.glp_add_rows(lp, newv.size() + 1);
	ind = GLPK.new_intArray(states + 2);
	for (int j = 0; j < states + 2; j++) {
	    GLPK.intArray_setitem(ind, j + 1, j + 1);
	}
	val = GLPK.new_doubleArray(states + 2);
	for (int i = 0; i < newv.size(); i++) {
	    GLPK.glp_set_row_bnds(lp, i + 1, GLPKConstants.GLP_LO, 0.0,
		    Double.POSITIVE_INFINITY);
	    AlphaVectorStd testVect = newv.get(i).copy();
	    for (int j = 0; j < states; j++) {
		GLPK.doubleArray_setitem(val, j + 1, selVect.get(j) - testVect.get(j));
	    }
	    GLPK.doubleArray_setitem(val, states + 1, -1.0);
	    GLPK.glp_set_mat_row(lp, i + 1, states + 1, ind, val);
	}
	// ind=GLPK.new_intArray(states+2);
	// for (int j=0;j<states+2;j++){
	// GLPK.intArray_setitem(ind,j+1,j+1);
	// }
	// val=GLPK.new_doubleArray(states+2);
	GLPK.glp_set_row_bnds(lp, newv.size() + 1, GLPKConstants.GLP_FX, 1.0,
		1.0);
	for (int j = 0; j < states; j++) {
	    GLPK.doubleArray_setitem(val, j + 1, 1.0);
	}
	GLPK.doubleArray_setitem(val, states + 1, 0.0);
	GLPK.glp_set_mat_row(lp, newv.size() + 1, states + 1, ind, val);
	// Define Objective
	GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);
	GLPK.glp_set_obj_coef(lp, states + 1, 1.0);
	// GLPK.glp_write_lp(lp, null, "yi.lp");
	parm = new glp_smcp();
	GLPK.glp_init_smcp(parm);
	parm.setMsg_lev(GLPKConstants.GLP_MSG_OFF);
	long inTime = System.currentTimeMillis();
	int ret = GLPK.glp_simplex(lp, parm);
	total_lp_time += System.currentTimeMillis() - inTime;
	if (ret == 0) {
	    double val1 = GLPK.glp_get_obj_val(lp);
	    double val2 = GLPK.glp_get_col_prim(lp, states + 1);
	    // System.out.println("vals ("+val1+" "+val2+")");
	    if (val1 > delta && val2 > delta) {
		BeliefStateStd thev = new BeliefStateStd(states);
		for (int i = 1; i <= states; i++)
		    thev.set(i - 1, GLPK.glp_get_col_prim(lp, i));
	    }
	}
	GLPK.glp_delete_prob(lp);
	return (bel);
    }

    private void domination_check(double delta) {
	if (set.size() < 2)
	    return;
	ArrayList<AlphaVectorStd> newv = new ArrayList<AlphaVectorStd>();
	while (set.size() > 0) {
	    AlphaVectorStd sel_vect = set.remove(set.size() - 1);
	    if (newv.size() == 0) {
		newv.add(sel_vect);
		continue;
	    }
	    ArrayList<AlphaVectorStd> tempv = new ArrayList<AlphaVectorStd>();
	    double max_dom = Double.NEGATIVE_INFINITY;
	    for (AlphaVectorStd test_vect : newv) {
		AlphaVectorStd res = test_vect.copy();
		res.add(-1.0, sel_vect);
		double min_dom = res.min();
		if (min_dom > max_dom)
		    max_dom = min_dom;
		if (max_dom > 0.0) {
		    break;
		}
		res.scale(-1.0);
		if (res.min() < 0.0) {
		    tempv.add(test_vect);
		}
	    }
	    if (max_dom < 0.0) {
		newv = tempv;
		newv.add(sel_vect);
	    }
	}
	set = newv;
    }

    public void crossSum(ValueFunctionStd vfB) {
	ArrayList<AlphaVectorStd> backup = set;
	set = new ArrayList<AlphaVectorStd>();
	for (AlphaVectorStd vecA : backup) {
		int a=vecA.getAction();
	    for (AlphaVector vecB : vfB.set) {
		AlphaVectorStd thevec = vecA.copy();
		thevec.add(vecB);
		thevec.setAction(a);
		push(thevec);
	    }
	}
    }

    public void merge(ValueFunctionStd vfA) {
	for (int i = 0; i < vfA.size(); i++) {
	    push(vfA.getAlpha(i).copy());
	}
    }

    public int getAction(int i) {
	return (set.get(i).getAction());

    }

    public void sort() {
	Collections.sort(set);
    }

    public double getAlphaElement(int i, int s) {
	return set.get(i).get(s);
    }

	public double performance(ValueFunctionStd oldv, int perfCriteria) {
		if (oldv == null || this.size() != oldv.size()) {
			return Double.POSITIVE_INFINITY;
		}
		this.sort();
		oldv.sort();
		double conv = 0;
		for (int j = 0; j < this.size(); j++) {
		    AlphaVectorStd newAlpha = this.getAlpha(j);
		    AlphaVectorStd oldAlpha = oldv.getAlpha(j);
		    if (newAlpha.getAction() != oldAlpha.getAction()) {
				return Double.POSITIVE_INFINITY;
		    }
		    AlphaVectorStd perf = newAlpha.copy();
		    perf.add(-1.0, oldAlpha);
		    double a_value = 0;
		    switch (perfCriteria) {
		    case Criteria.CC_MAXEUCLID:
			a_value = perf.norm(2.0);
			break;
		    case Criteria.CC_MAXDIST:
			a_value = perf.norm(1.0);
			break;
		    }
		    if (a_value > conv)
			conv = a_value;
		}
		return conv;
	}

	public double performance(ValueFunction oldv, int perfCriteria) {
		return performance((ValueFunctionStd)oldv,perfCriteria);
	}

	public void push(AlphaVector vec) {
		push((AlphaVectorStd)vec);
	}

	public AlphaVector getUpperBound() {
		if (this.set.size()<1){
			return null;
		}
		int states=set.get(0).size();
		AlphaVectorStd ub=new AlphaVectorStd(states);
		for (int s = 0; s < states; s++) {
		    double colmax = Double.NEGATIVE_INFINITY;
		    for (int a = 0; a < set.size(); a++) {
		    	double val = getAlphaElement(a, s);
		    	if (val > colmax)
		    		colmax = val;
		    	}
		    ub.setValue(s, colmax);
		}
		return ub;
	}

	public void crossSum(ValueFunction vf) {
		crossSum((ValueFunctionStd)vf);
	}

	public void merge(ValueFunction vfA) {
		merge((ValueFunctionStd)vfA);
	}

	public AlphaVector getFirstAlpha() {
		return getAlpha(0);
	}

} // ValueFunctionStd
