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

public abstract class valueFunction {
    
    // flat representation of the alpha vectors
    protected double vFlat[][];

    // simple list of actions associated with each alpha
    protected int a[];
    
    // public methods
    public int[] getActions() {
	return a;
    }

    public double[][] getvFlat() {
	return vFlat;
    }

    // abstract methods
    // value of a belief according to this value function
    public abstract double V(belState b);

} // valueFunction