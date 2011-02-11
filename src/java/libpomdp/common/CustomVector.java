/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CustomMatrix.java
 * Description: Wrapper for sparse vector implementation.
 * Copyright (c) 2010, 2011 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.common;

import java.io.Serializable;
import java.util.Iterator;

import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.sparse.SparseVector;

public class CustomVector implements Serializable, Comparable<CustomVector> {


    protected SparseVector v;
    
    /**
     * 
     */
    private static final long serialVersionUID = 8494095501573100178L;

    public double[] getData() {
	return v.getData();
    }

    public int[] getIndex() {
	return v.getIndex();
    }

    public int getUsed() {
	return v.getUsed();
    }

    public String toString() {
	return v.toString();
    }

    public void zero() {
	v.zero();
    }

    public CustomVector(double[] list) {
	this(list.length);
	int idx = 0;
	for (double value : list) {
	    if (value != 0.0)
		v.set(idx, value);
	    idx++;
	}
    }

    public CustomVector(int length) {
	v = new SparseVector(length);
    }

    public CustomVector(SparseVector v){
    	v=this.v;
    }
    
    public CustomVector(CustomVector cv) {
	this(cv.size());
	v = cv.v.copy();
    }

    public CustomVector(long[] val) {
    	this(val.length);
    	int idx = 0;
    	for (long value : val) {
    	    if (value != 0.0)
    		v.set(idx, value);
    	    idx++;
    	}	
	}

	public double dot(CustomVector cv) {
	return (v.dot(cv.v));
    }

    public CustomVector copy() {
	return (new CustomVector(this));
    }

    public static CustomVector convert(double vec[]) {
	CustomVector newVec = new CustomVector(vec.length);
	for (int i = 0; i < vec.length; i++)
	    newVec.set(i, vec[i]);
	return newVec;
    }

    public void set(int i, double d) {
	v.set(i, d);
    }

    public static CustomVector getHomogene(int siz, double val) {
	CustomVector newVec = new CustomVector(siz);
	for (int i = 0; i < siz; i++)
	    newVec.set(i, val);
	return newVec;
    }

    public static CustomVector getUniform(int siz) {
	double[] uni = new double[siz];
	for (int i = 0; i < siz; i++)
	    uni[i] = 1.0 / siz;
	return (convert(uni));
    }

    public double norm(double d) {
	if (d == Double.POSITIVE_INFINITY)
	    return v.norm(Vector.Norm.Infinity);
	switch ((int) d) {
	case 1:
	    return v.norm(Vector.Norm.One);
	case 2:
	    return v.norm(Vector.Norm.Two);
	default:
	    System.out.println("Norm not supported (norm " + d + ")");
	    return 0;
	}
    }

    public CustomVector scale(double d) {
	v.scale(d);
	return this;
    }

    public CustomVector elementMult(CustomVector cv) {
	for (VectorEntry e : v)
	    v.set(e.index(), e.get() * cv.get(e.index()));
	return this;
    }

    public double get(int s) {
	return v.get(s);
    }

    public double[] getArray() {
	return (Matrices.getArray(v));
    }

    public int numColumns() {
	return 1;
    }

    public int numRows() {
	return size();
    }

    public int size() {
	return v.size();
    }

    public void add(CustomVector cv) {
	v.add(cv.v);
    }

    public void add(double d, CustomVector cv) {
	v.add(d, cv.v);
    }

    public void set(CustomVector res) {
	v.set(res.v);
    }

    public double min() {
	double minv = Double.POSITIVE_INFINITY;
	for (double val : Matrices.getArray(v)) {
	    if (val < minv)
		minv = val;
	}
	return minv;
    }

    public boolean compare(CustomVector point) {
	if (point.v.toString().compareTo(v.toString()) == 0)
	    return true;
	return false;

    }

    public static CustomVector getRandomUnitary(int dim) {
	CustomVector retval = new CustomVector(dim);
	for (int i = 0; i < dim; i++) {
	    retval.set(i, Utils.gen.nextDouble());
	}
	retval.normalize();
	return retval;
    }

    public int compareTo(CustomVector vprime, double delta) {
	for (int i = 0; i < v.size(); i++) {
	    if (v.get(i) > vprime.get(i) + delta)
		return 1;
	    if (v.get(i) < vprime.get(i) - delta)
		return -1;
	}
	return 0;
    }

    public int compareTo(CustomVector arg0) {
	return compareTo(arg0, 0.0);
    }

    public double max() {
	double maxv = Double.NEGATIVE_INFINITY;
	for (double val : Matrices.getArray(v)) {
	    if (val > maxv)
		maxv = val;
	}
	return maxv;
    }

	public double cumulate() {
		double cum=0;
		for (int i=0;i<v.size();i++){
			cum+=v.get(i);
			v.set(i,cum);			
		}
		return cum;
	}

	public int sample() {
		double cum=0;
		double prob=Utils.gen.nextDouble();
		for (int i=0;i<v.size();i++){
			cum+=v.get(i);
			if (cum>=prob)
				return i;
		}
		return -1;
	}
	
	public double getEntropy(double base) {
		SparseVector logv=v.copy();
		Iterator<VectorEntry> it = logv.iterator();
		do{
			VectorEntry val=it.next();
			val.set(Math.log(val.get()));
		} while (it.hasNext());
		if 	(base!=Math.E)
			logv.scale(1.0/Math.log(base));
		return(-logv.dot(v));
	}

	public void normalize() {
		double norm=v.norm(Vector.Norm.One);
		v.scale(1.0/norm);
	}

	public SparseVector getInternal() {
		return v;
	}

}
