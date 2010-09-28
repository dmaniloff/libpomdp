/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: Common.java
 * Description: useful general routines
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

import java.util.ArrayList;
import java.util.Random;

import org.math.array.DoubleArray;

public class Utils {

    /// set the gen only once for every instance
    public static Random gen = new Random(System.currentTimeMillis());

    /// sample from a distribution - need not be fast, this is outside the planning loop
    /// so this will be O(n), instead of log(n) using binary search
    public static int sample(double d[]) {
	double[] cumSum = DoubleArray.cumSum(d);
	double   r      = gen.nextDouble();
	for(int i=0; i<cumSum.length; i++)
	    if(cumSum[i] > r) 
		return i;
	return d.length-1;
    }
		
    
    /// randomized argmax
    public static int argmax(double v[]) {
	// declarations
	double maxv = Double.NEGATIVE_INFINITY;
	int argmax  = -1;
	int c;
	for(c=0; c<v.length; c++) {
	    if (v[c] > maxv) {
		maxv   = v[c];
		argmax = c;
	    } 
	    if (v[c] == maxv) {
		// randomly decide to change index - this will no be uniform!
		if (gen.nextInt(2) == 0)
		    argmax = c;
	    }
	}
	return argmax;
		
    } // argmax

    // arternative, possibly slower, but more uniform
    public static int argmax2(double v[]) {
	// decls	
	ArrayList<Integer> repi = new ArrayList<Integer>();
	int a,r;
	double maxv;

	// compute maximum
	maxv = DoubleArray.max(v);
	// locate repeated values
	for(a=0; a<v.length; a++) 
	    if(v[a] == maxv) repi.add(new Integer(a));
	// randomize among them if necessary
	//if (repi.size() > 1) System.out.println("will rand, check!!");
	r = gen.nextInt(repi.size());
	// return chosen index
	return repi.get(r);

    } // argmax2


} // Common