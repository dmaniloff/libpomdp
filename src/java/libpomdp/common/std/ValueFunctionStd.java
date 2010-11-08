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

import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.Utils;
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
    private ArrayList <CustomVector> v;
    
    private int states;
    
    long total_lp_time;
    
    // actions associated to each alpha vector
    private ArrayList <Integer> a;

    // constructor
    public ValueFunctionStd(int states){
    	a=new ArrayList<Integer>();
    	v=new ArrayList<CustomVector>();
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
    	return(Utils.convertIntegers(a));
    }

    // return value of a belief state
    public double value(BeliefState bel) {
    	//long start = System.currentTimeMillis();
    	CustomVector b = (CustomVector)bel.getPoint();
    	double valmax=Double.MIN_VALUE;
    	int index=0;
    	for (CustomVector vect: v){
    		double sol = vect.dot(b);
    		if (sol > valmax)
    		{
    			valmax=sol;
    			index=v.indexOf(vect);
    		}
    	}
		bel.setAlpha(index);
		//System.out.println("elapsed in V: " + (System.currentTimeMillis() - start));
		return valmax;
    }

    public int push(double list[],int a){
    	return push(new CustomVector(list),a);
    }
    
	public int push(CustomVector vec, int a) {
		v.add(vec.copy());
		this.a.add(new Integer(a));
		return(v.size()-1);
	}

	public CustomVector getVectorRef(int idx) {
		return v.get(idx);
	} 
	
	public int size() {
		return(v.size());
	}

	public ValueFunctionStd copy() {
		ValueFunctionStd newv=new ValueFunctionStd(states);
		for (int i=0;i<v.size();i++)
			newv.push(v.get(i).copy(),a.get(i).intValue());
		return newv;
	}

	public CustomVector getVectorCopy(int idx) {
		return(getVectorRef(idx).copy());
	}
	
    public String toString(){
    	String retval="Value Function\n";
    	for (int i=0;i<size();i++){
    		retval+="v"+i+"\t[";
    		CustomVector v=getVectorRef(i);
    		for (int j=0;j<v.size();j++){
    			retval+=v.get(j)+" ";
    		}
    		retval+="] a="+a.get(i)+"\n";
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
		if (v.size()<2)
			return total_lp_time;
		ArrayList<CustomVector> newv=new ArrayList<CustomVector>();
		ArrayList<Integer> newa=new ArrayList<Integer>();
		while(v.size()>0){
			BeliefStateStd b;
			CustomVector sel_vect=v.remove(0);
			Integer sel_a=a.remove(0);
			if (newv.size()==0){
				b = new BeliefStateStd(CustomVector.getUniform(sel_vect.size()),-1);
			}
			else{
				b=find_region(sel_vect,newv,delta);
			}
			if (b!=null) {
				v.add(sel_vect);
				a.add(sel_a);
				sel_vect=best_vector(b,v,delta);
				int idx=v.indexOf(sel_vect);
				v.remove(idx);
				sel_a=a.remove(idx);
				newv.add(sel_vect);
				newa.add(sel_a);
			}
		}
		v=newv;
		a=newa;
		return total_lp_time;
	}

	private CustomVector best_vector(BeliefStateStd b,
			ArrayList<CustomVector> v2,double delta) {
		CustomVector best_vec = v2.get(0);
		double best_val=best_vec.dot(b.getPoint());;
		for (CustomVector test_vec:v2){
			double val=test_vec.dot(b.getPoint());
			if (Math.abs(val- best_val) < delta){
				best_vec=lexicographic_max(best_vec,test_vec,delta);
			}
			else if (val > best_val){
				best_vec=test_vec;
			}
			best_val=best_vec.dot(b.getPoint());
		}
		return best_vec;
	}

	private CustomVector lexicographic_max(CustomVector bestVec,
			CustomVector testVec, double delta) {
		for (int i=0;i<bestVec.size();i++){
			if (bestVec.get(i) > testVec.get(i) + delta)
				return bestVec;
			if (bestVec.get(i) < testVec.get(i) - delta)
				return testVec;
		}
		return bestVec;
	}

	private BeliefStateStd find_region(CustomVector selVect, ArrayList<CustomVector> v2, double delta){
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
		GLPK.glp_add_rows(lp, v2.size()+1);
		ind=GLPK.new_intArray(states+2);
		for (int j=0;j<states+2;j++){
			GLPK.intArray_setitem(ind,j+1,j+1);
		}
		val=GLPK.new_doubleArray(states+2);
		for (int i=0;i<v2.size();i++){	
			GLPK.glp_set_row_bnds(lp, i+1, GLPKConstants.GLP_LO,0.0,Double.POSITIVE_INFINITY);
			CustomVector testVect=v2.get(i);
			for (int j=0;j<states;j++){
				GLPK.doubleArray_setitem(val,j+1,selVect.get(j) - testVect.get(j));
			}
			GLPK.doubleArray_setitem(val,states+1,-1.0);
			GLPK.glp_set_mat_row(lp,i+1,states+1, ind, val);
		}
		//ind=GLPK.new_intArray(states+2);
		//for (int j=0;j<states+2;j++){
		//	GLPK.intArray_setitem(ind,j+1,j+1);
		//}
		//val=GLPK.new_doubleArray(states+2);
		GLPK.glp_set_row_bnds(lp,v2.size()+1,GLPKConstants.GLP_FX,1.0,1.0);
		for (int j=0;j<states;j++){
			GLPK.doubleArray_setitem(val,j+1,1.0);
		}
		GLPK.doubleArray_setitem(val,states+1,0.0);
		GLPK.glp_set_mat_row(lp,v2.size()+1,states+1, ind, val);
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
		if (v.size()<2)
			return;
		ArrayList<CustomVector> newv=new ArrayList<CustomVector>();
		ArrayList<Integer> newa=new ArrayList<Integer>();
		while(v.size()>0){
			CustomVector sel_vect=v.remove(v.size()-1);
			Integer sel_a=a.remove(v.size());
			if (newv.size()==0){
				newv.add(sel_vect);
				newa.add(sel_a);
				continue;
			}
			ArrayList<CustomVector> tempv=new ArrayList<CustomVector>();
			ArrayList<Integer> tempa=new ArrayList<Integer>();
			double max_dom=Double.NEGATIVE_INFINITY;
			for (CustomVector test_vect:newv){
				CustomVector res=test_vect.copy();
				res.add(-1.0,sel_vect);
				double min_dom=res.min();
				if (min_dom > max_dom)
					max_dom=min_dom;
				if (max_dom > 0.0){
					break;
				}
				res.scale(-1.0);
				if (res.min()<0.0){
					tempv.add(test_vect);
					tempa.add(newa.get(newv.indexOf(test_vect)));
				}
			}
			if (max_dom<0.0){
				newv=tempv;
				newa=tempa;
				newv.add(sel_vect);
				newa.add(sel_a);
			}
		}
		v=newv;
		a=newa;
	}

	public void crossSum(ValueFunctionStd vfB) {
		int mya=a.get(0).intValue();
		ArrayList<CustomVector> backup = v;
		v=new ArrayList<CustomVector>();
		for (CustomVector vecA:backup){
    		for (CustomVector vecB:vfB.v){
        		CustomVector thevec=vecA.copy();
        		thevec.add(vecB);
        		push(thevec,mya);
        	}
    	}
		
	}

	public void merge(ValueFunctionStd vfA) {
		for (int i=0;i<vfA.size();i++){
    		push(vfA.getVectorRef(i),vfA.getAction(i));
    	}
	}

	public int getAction(int i) {
		return(a.get(i).intValue());
		
	}
    
} // valueFunctionSparseMTJ
