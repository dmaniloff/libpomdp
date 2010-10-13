package libpomdp.common.java;

import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.sparse.SparseVector;

public class CustomVector {
	
	protected SparseVector v;

	public CustomVector(double[] list) {
		this(list.length);
		int idx=0;
		for (double value:list){
			if (value!=0.0)
				v.set(idx, value);
			idx++;
		}
	}

	public CustomVector(int length) {
		v=new SparseVector(length);
	}

	public CustomVector(CustomVector cv) {
		this(cv.size());
		v=cv.v.copy();
	}

	public double dot(CustomVector cv) {
		return(v.dot(cv.v));
	}

	public CustomVector copy() {
		return(new CustomVector(this));
	}
	
    public static CustomVector convert(double vec[]){
		CustomVector newVec=new CustomVector(vec.length);
		for (int i=0;i<vec.length;i++)
			newVec.set(i,vec[i]);
		return newVec;
    }
    
    public void set(int i, double d) {
    	v.set(i, d);
	}

	public static CustomVector getHomogene(int siz,double val){
		CustomVector newVec=new CustomVector(siz);
		for (int i=0;i<siz;i++)
			newVec.set(i,val);
		return newVec;
    }
    
    
    public static CustomVector getUniform(int siz){
    	double[] uni=new double[siz];
    	for(int i=0;i<siz;i++)
    		uni[i]=1.0/siz;
		return (convert(uni));
    }

	public double norm(double d) {
		if (d==Double.POSITIVE_INFINITY)
			return v.norm(Vector.Norm.Infinity);
		switch ((int)d){
			case 1:
				return v.norm(Vector.Norm.One);
			case 2:
				return v.norm(Vector.Norm.Two);
			default:
				System.out.println("Norm not supported (norm "+d+")");
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
		return(Matrices.getArray(v));
	}

	public int size() {
		return v.size();
	}

	public void add(CustomVector cv) {
		v.add(cv.v);		
	}

	public void add(double d, CustomVector cv) {
		v.add(d,cv.v);	
	}
    
	
}
