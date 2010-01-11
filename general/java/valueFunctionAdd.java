/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: valueFunctionAdd.java
 * Description: implementation of a value function via ADDs
 *              makes use of Poupart's OP class to manipulate ADDs
 *              see README reference [5]
 *              implements Serializable so we can use the save command
 *              in Matlab
 * Copyright (c) 2010, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import org.math.array.*;
import java.io.*;

public class valueFunctionAdd implements valueFunction, Serializable {
    
    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    // represent a value function via an array of Adds
    private DD vAdd[];

    // staIds of the problem
    private int staIds[];

    // actions associated to each alpha vector
    private int a[];

    // constructor
    public valueFunctionAdd(DD vAdd[], int staIds[], int a[]) {
	this.vAdd   = vAdd; 
	this.a      = a;
	this.staIds = staIds;
    }

    // ------------------------------------------------------------------------
    // interface methods
    // ------------------------------------------------------------------------

    // list of actions associated with each alpha
    public int[] getActions() {
	return a;
    }

    // return value of a belief state
    public double V(belState bel) {
	double start;		
	DD b = ((belStateAdd)bel).bAdd;
	// the NoMem version this seems to be faster 
	double dotProds[] = OP.dotProductNoMem(b, vAdd, staIds);
	int argmax = Common.argmax(dotProds);
	// save the index of the alpha that supports this belief point
	bel.setplanid(argmax);
	double max = dotProds[argmax];
	return max;
    }

    // return flat value function
    public double[][] getvFlat() {
	return OP.convert2array(vAdd, staIds);
    }    

    // return Add representation of this value function
    public DD[] getvAdd() {
	return vAdd;
    }

} // valueFunctionAdd
