/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CustomMatrix.java
 * Description: Wrapper for sparse matrix implementation.
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

import java.io.Serializable;

import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.FlexCompColMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

public class CustomMatrix implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2570778734441488861L;

	public int numColumns() {
		return m.numColumns();
	}

	public int numRows() {
		return m.numRows();
	}

	public Matrix transpose() {
		return m.transpose();
	}

	public void zero() {
		m.zero();
	}

	protected FlexCompColMatrix m; 
	
	public CustomMatrix(int rows, int cols) {
		m=new FlexCompColMatrix(rows,cols);
	}

	public CustomMatrix(CustomMatrix cm) {
		m=new FlexCompColMatrix(cm.m);
	}

	public CustomMatrix(double[][] list) {
		int cols=list.length;
		int rows=list[0].length;
		m=new FlexCompColMatrix(rows,cols);
		for (int i=0;i<cols;i++){
			CustomVector vec=CustomVector.convert(list[i]);
			m.setColumn(i,vec.v);
		}
	}

	public static CustomMatrix getUniform(int siz,int siz2){
			CustomMatrix uni=new CustomMatrix(siz, siz2);
	    	for(int i=0;i<siz;i++)
	    		for(int j=0;j<siz2;j++)
	    			uni.set(i,j,1.0/siz2);
	    	return(uni);
	    }

	public void set(int i, int j, double d) {
		m.set(i,j,d);		
	}

	public double get(int o, int s) {
		return m.get(o,s);
	}

	public CustomMatrix mult(CustomMatrix in) {
		CustomMatrix retval=new CustomMatrix(this.numRows(),in.numColumns());
		retval.m=(FlexCompColMatrix) m.mult(in.m, retval.m);
		return retval;
	}

	public CustomVector mult(CustomVector in) {
		CustomVector retval=new CustomVector(this.numRows());
		retval.v=(SparseVector) m.mult(in.v, retval.v);
		return retval;
	}

	public CustomVector transMult(CustomVector in) {
		CustomVector retval=new CustomVector(this.numRows());
		retval.v=(SparseVector) m.transMult(in.v, retval.v);
		return retval;
	}

	public CustomVector getColumn(int idx)
	{
		CustomVector retval=new CustomVector(m.numRows());
		retval.v=m.getColumn(idx).copy();
		return retval;
	}

	public CustomMatrix transBmult(CustomMatrix in) {
		CustomMatrix retval=new CustomMatrix(this.numRows(),in.numRows());
		retval.m=(FlexCompColMatrix) m.transBmult(in.m, retval.m);
		return retval;
	}

	public void setColumn(int i, CustomVector colV) {
		m.setColumn(i, colV.v);
	}

	public static CustomMatrix getIdentity(int nrSta) {
		CustomMatrix retval=new CustomMatrix(nrSta,nrSta);
		for (int i=0;i<nrSta;i++){
			retval.set(i, i, 1.0);
		}
		return retval;
	}

	public CustomVector mult(double alpha, CustomVector in) {
		CustomVector retval=new CustomVector(m.numRows());
		retval.v=(SparseVector) m.mult(alpha,in.v, retval.v);
		return retval;
	}

	public double[][] getArray() {
		return Matrices.getArray(m);
	}
	
	public String toString(){
		String ret="";
		for (int i=0;i<numRows();i++){
			for (int j=0;j<numColumns();j++){
				ret+=get(i, j)+" ";
			}
			ret+="\n";
		}
		return ret;
	}
	
	public CustomMatrix copy() {
		return new CustomMatrix(this);
	}

}
