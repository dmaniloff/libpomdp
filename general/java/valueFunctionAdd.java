/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: valueFunctionAdd.java
 * Description: implementation of a value function via ADDs
 *              makes use of Poupart's OP class to manipulate ADDs
 *              see README reference [5]
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import org.math.array.*;
import java.io.*;

public class valueFunctionAdd implements valueFunction, Serializable {
    
    // represent a value function via an array of Adds
    private DD vAdd[];

    // staIds of the problem
    private int staIds[];

    // actions associated to each alpha vector
    private int a[];

    // constructor
    public valueFunctionAdd(DD vAdd[], int staIds[], int a[]) {
	this.vAdd   = vAdd; 
	//this.vFlat  = OP.convert2array(vAdd, staIds); !!!
	this.a      = a;
	this.staIds = staIds;
    }

    // return flat value function
    public double[][] getvFlat() {
	return OP.convert2array(vAdd, staIds);
    }

    // return value of a belief state
    public double V(belState bel) {
	double start;		
	DD b = ((belStateAdd)bel).bAdd;
	//DD u = ((valueFunctionAdd)offlineUpper).vAdd;
	//start = System.currentTimeMillis();
	//double dotProds[] = OP.dotProduct(b, vAdd, staIds);
	// this seems to be faster...
	double dotProds[] = OP.dotProductNoMem(b, vAdd, staIds);
	//System.out.println("dot prod took" + (System.currentTimeMillis() - start));
	//start = System.currentTimeMillis();
	double max = DoubleArray.max(dotProds);
	//System.out.println("max took" + (System.currentTimeMillis() - start));
	return max;
    }

    public int[] getActions() {
	return a;
    }

    // return Add representation of this value function
    public DD[] getvAdd() {
	return vAdd;
    }

} // valueFunctionAdd
