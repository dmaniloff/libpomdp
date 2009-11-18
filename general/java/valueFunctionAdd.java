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

public class valueFunctionAdd extends valueFunction {
    
    // represent a value function via an array of Adds
    private DD vAdd[];

    // staIds of the problem
    private int staIds[];

    // constructor
    public valueFunctionAdd(DD vAdd[], int staIds[], int a[]) {
	this.vAdd   = vAdd; 
	this.vFlat  = OP.convert2array(vAdd, staIds);
	this.a      = a;
	this.staIds = staIds;
    }

    // return value of a belief state
    public double V(belState bel) {		
	DD b = ((belStateAdd)bel).ddB;
	//DD u = ((valueFunctionAdd)offlineUpper).vAdd;
	double dotProds[] = OP.dotProduct(b, vAdd, staIds);
	return DoubleArray.max(dotProds);
    }

    public DD[] getvAdd() {
	return vAdd;
    }
} // valueFunctionAdd
