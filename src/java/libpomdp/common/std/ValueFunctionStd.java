/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: valueFunctionSparseMTJ.java
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
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
    private ArrayList <AlphaVector> set;
    private int states;
    long total_lp_time;
    
    // constructor
    public ValueFunctionStd(int states){
    	set=new ArrayList<AlphaVector>();
    	this.states=states;
    }

    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------

    public ValueFunctionStd(double[][] v, int[] a) {
    	this(v[0].length);
    	for (int i=0;i<a.length;i++){
    		push(v[i],a[i]);
    	}
	}

	// list of actions associated with each alpha
    public int[] getActions() {
    	int [] retval=new int[size()];
    	for (int i=0;i<size();i++){
    		retval[i]=getAction(i);
    	}
    	return(retval);
    }

    // return value of a belief state
    public double value(BeliefState bel) {
    	//long start = System.currentTimeMillis();
    	double valmax=Double.NEGATIVE_INFINITY;
    	AlphaVector sel = null;
    	for (AlphaVector alpha: set){
    		double sol = alpha.eval(bel);
    		if (sol > valmax)
    		{
    			valmax=sol;
    			sel=alpha;
    		}
    	}
		bel.setAlpha(set.indexOf(sel));;
		return valmax;
    }

    public boolean push(double list[],int a){
    	return push(new CustomVector(list),a);
    }
    
	public boolean push(CustomVector vec, int a) {
		return(push(new AlphaVector(vec.copy(),a)));
	}

	public boolean push(AlphaVector ent) {
		return(set.add(ent));
	}
	
	public AlphaVector getAlpha(int idx) {
		return set.get(idx);
	} 
	
	public int size() {
		return(set.size());
	}

	public ValueFunctionStd copy() {
		ValueFunctionStd newv=new ValueFunctionStd(states);
		for (int i=0;i<set.size();i++)
			newv.push(set.get(i).copy());
		return newv;
	}

	public CustomVector getAlphaValues(int idx) {
		return(getAlpha(idx).getVectorCopy());
	}
	
	public int getAlphaAction(int idx) {
		return(set.get(idx).getAction());
	}
	
    public String toString(){
    	String retval="Value Function\n";
    	for (int i=0;i<size();i++){
    		retval+="v"+i+"\t[";
    		CustomVector v=getAlphaValues(i);
    		for (int j=0;j<v.size();j++){
    			retval+=v.get(j)+" ";
    		}
    		retval+="] a="+getAlphaAction(i)+"\n";
    	}
    	return retval;
    }
    
    public long prune(){
    	return prune(1e-10);
    }
    public long prune(double delta){
    	domination_check(delta);
    	return(lp_pruning(delta));
    }

	private long lp_pruning(double delta) {
		total_lp_time=0;
		if (set.size()<2)
			return total_lp_time;
		ArrayList<AlphaVector> newv=new ArrayList<AlphaVector>();
		while(set.size()>0){
			BeliefStateStd b;
			AlphaVector sel_vect=set.remove(0);
			if (newv.size()==0){
				b = new BeliefStateStd(CustomVector.getUniform(sel_vect.size()),-1);
			}
			else{
				b=find_region(sel_vect,newv,delta);
			}
			if (b!=null) {
				set.add(sel_vect);
				sel_vect=best_vector(b,set,delta);
				int idx=set.indexOf(sel_vect);
				set.remove(idx);
				newv.add(sel_vect);
			}
		}
		set=newv;
		return total_lp_time;
	}

	private AlphaVector best_vector(BeliefStateStd b,
			ArrayList<AlphaVector> set2,double delta) {
		AlphaVector best_vec = set2.get(0);
		double best_val=best_vec.eval(b);
		for (AlphaVector test_vec:set2){
			double val=test_vec.eval(b);
			if (Math.abs(val- best_val) < delta){
				best_vec=lexicographic_max(best_vec,test_vec,delta);
			}
			else if (val > best_val){
				best_vec=test_vec;
			}
			best_val=best_vec.eval(b);
		}
		return best_vec;
	}

	private AlphaVector lexicographic_max(AlphaVector bestVec,
			AlphaVector testVec, double delta) {
		if (bestVec.compareTo(testVec,delta)>0)
			return(bestVec);
		else
			return(testVec);
	}

	private BeliefStateStd find_region(AlphaVector selVect, ArrayList<AlphaVector> newv, double delta){
		// Can Sparsity play a role here?, nice question!
		BeliefStateStd bel=null;
		glp_prob lp;
		glp_smcp parm;
		SWIGTYPE_p_int ind;
		SWIGTYPE_p_double val;
		lp = GLPK.glp_create_prob();
		GLPK.glp_set_prob_name(lp, "FindRegion");
		// Define Solution Vector
		GLPK.glp_add_cols(lp, states+1);
		for (int i=0;i<states;i++){
			GLPK.glp_set_col_kind(lp,i+1, GLPKConstants.GLP_CV);
			GLPK.glp_set_col_bnds(lp,i+1, GLPKConstants.GLP_DB, 0.0, 1.0);
		}
		GLPK.glp_set_col_kind(lp,states+1, GLPKConstants.GLP_CV);
		GLPK.glp_set_col_bnds(lp,states+1, GLPKConstants.GLP_FR, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		// Define Constraints
		GLPK.glp_add_rows(lp, newv.size()+1);
		ind=GLPK.new_intArray(states+2);
		for (int j=0;j<states+2;j++){
			GLPK.intArray_setitem(ind,j+1,j+1);
		}
		val=GLPK.new_doubleArray(states+2);
		for (int i=0;i<newv.size();i++){	
			GLPK.glp_set_row_bnds(lp, i+1, GLPKConstants.GLP_LO,0.0,Double.POSITIVE_INFINITY);
			CustomVector testVect=newv.get(i).getVectorCopy();
			for (int j=0;j<states;j++){
				GLPK.doubleArray_setitem(val,j+1,selVect.getVectorRef().get(j) - testVect.get(j));
			}
			GLPK.doubleArray_setitem(val,states+1,-1.0);
			GLPK.glp_set_mat_row(lp,i+1,states+1, ind, val);
		}
		//ind=GLPK.new_intArray(states+2);
		//for (int j=0;j<states+2;j++){
		//	GLPK.intArray_setitem(ind,j+1,j+1);
		//}
		//val=GLPK.new_doubleArray(states+2);
		GLPK.glp_set_row_bnds(lp,newv.size()+1,GLPKConstants.GLP_FX,1.0,1.0);
		for (int j=0;j<states;j++){
			GLPK.doubleArray_setitem(val,j+1,1.0);
		}
		GLPK.doubleArray_setitem(val,states+1,0.0);
		GLPK.glp_set_mat_row(lp,newv.size()+1,states+1, ind, val);
		// Define Objective
		GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);
		GLPK.glp_set_obj_coef(lp,states+1, 1.0);
		//GLPK.glp_write_lp(lp, null, "yi.lp");
		parm = new glp_smcp();
		GLPK.glp_init_smcp(parm);
		parm.setMsg_lev(GLPKConstants.GLP_MSG_OFF);
		long inTime = System.currentTimeMillis();
		int ret = GLPK.glp_simplex(lp, parm);
		total_lp_time+=System.currentTimeMillis() - inTime;
		if (ret==0) {
			double val1=GLPK.glp_get_obj_val(lp);
			double val2=GLPK.glp_get_col_prim(lp,states+1);
			//System.out.println("vals ("+val1+" "+val2+")");
			if (val1 > delta && val2 > delta){
				CustomVector thev=new CustomVector(states);
				for (int i = 1; i <= states; i++) 
					thev.set(i-1,GLPK.glp_get_col_prim(lp, i));
				bel=new BeliefStateStd(thev,-1);
			}
		}
		GLPK.glp_delete_prob(lp);
		return(bel);
	}

	private void domination_check(double delta) {
		if (set.size()<2)
			return;
		ArrayList<AlphaVector> newv=new ArrayList<AlphaVector>();
		while(set.size()>0){
			AlphaVector sel_vect=set.remove(set.size()-1);
			if (newv.size()==0){
				newv.add(sel_vect);
				continue;
			}
			ArrayList<AlphaVector> tempv=new ArrayList<AlphaVector>();
			double max_dom=Double.NEGATIVE_INFINITY;
			for (AlphaVector test_vect:newv){
				CustomVector res=test_vect.getVectorCopy();
				res.add(-1.0,sel_vect.getVectorRef());
				double min_dom=res.min();
				if (min_dom > max_dom)
					max_dom=min_dom;
				if (max_dom > 0.0){
					break;
				}
				res.scale(-1.0);
				if (res.min()<0.0){
					tempv.add(test_vect);
				}
			}
			if (max_dom<0.0){
				newv=tempv;
				newv.add(sel_vect);
			}
		}
		set=newv;
	}

	public void crossSum(ValueFunctionStd vfB) {
		int mya=set.get(0).getAction();
		ArrayList<AlphaVector> backup = set;
		set=new ArrayList<AlphaVector>();
		for (AlphaVector vecA:backup){
    		for (AlphaVector vecB:vfB.set){
        		CustomVector thevec=vecA.getVectorCopy();
        		thevec.add(vecB.getVectorRef());
        		push(thevec,mya);
        	}
    	}
	}

	public void merge(ValueFunctionStd vfA) {
		for (int i=0;i<vfA.size();i++){
    		push(vfA.getAlpha(i).copy());
    	}
	}

	public int getAction(int i) {
		return(set.get(i).getAction());
		
	}

	public void sort() {
		Collections.sort(set);
	}

	public double getAlphaElement(int i, int s) {
		return set.get(i).getVectorRef().get(s);
	}
    
} // valueFunctionSparseMTJ
