/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: valueFunction.java
 * Description: representation of a set of alpha vectors and their
 *              associated actions for direct control (if possible)
 *              properties are to be filled by extending classes
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

interface valueFunction {
    
    // flat representation of the alpha vectors
    //protected double vFlat[][];

    
    // protected int a[];
    
    // simple list of actions associated with each alpha
    public int[] getActions();
//  {
// 	return a;
//     }

    //public double[][] getvFlat() {
    //	return vFlat;
    //}

    // value of a belief according to this value function
    public double V(belState b);

    public double[][] getvFlat();

} // valueFunction