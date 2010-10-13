/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: valueFunctionSparseMTJ.java
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java.standard;

// imports
import java.io.Serializable;
import java.util.ArrayList;

import libpomdp.common.java.BeliefState;
import libpomdp.common.java.CustomVector;
import libpomdp.common.java.Utils;
import libpomdp.common.java.ValueFunction;


public class ValueFunctionStandard implements ValueFunction, Serializable {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // serial id
    static final long serialVersionUID = 4L;

    // represent a value function via a Matrix object
    private ArrayList <CustomVector> v;
    
    private int states;
    
    // actions associated to each alpha vector
    private ArrayList <Integer> a;

    // constructor
    public ValueFunctionStandard(int states){
    	a=new ArrayList<Integer>();
    	v=new ArrayList<CustomVector>();
    	this.states=states;
    }

    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------

    public ValueFunctionStandard(double[][] v, int[] a) {
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
    public double V(BeliefState bel) {
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

	public CustomVector getVector(int idx) {
		return v.get(idx);
	} 
	
	public int size() {
		return(v.size());
	}

	public ValueFunctionStandard copy() {
		ValueFunctionStandard newv=new ValueFunctionStandard(states);
		for (int i=0;i<v.size();i++)
			newv.push(v.get(i).copy(),a.get(i).intValue());
		return newv;
	}

} // valueFunctionSparseMTJ
