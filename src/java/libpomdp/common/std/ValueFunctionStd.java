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
		// TODO Auto-generated constructor stub
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
    		retval+="]\n";
    	}
    	return retval;
    }
    
    
} // valueFunctionSparseMTJ
