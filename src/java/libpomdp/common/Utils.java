/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: Utils.java
 * Description: useful general routines - everything in this class is static
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.common.java;

// imports
import java.util.ArrayList;
import java.util.Random;

import org.math.array.DoubleArray;
import org.math.array.IntegerArray;

import symPerseusJava.DD;

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
	// declarations	
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

    public static int argmin(double[] v) {
	// declarations
	double minv = Double.POSITIVE_INFINITY;
	int argmin  = -1;
	int c;
	for(c=0; c<v.length; c++) {
	    if (v[c] < minv) {
		minv   = v[c];
		argmin = c;
	    } 
	    if (v[c] == minv) {
		// randomly decide to change index - this will no be uniform!
		if (gen.nextInt(2) == 0)
		    argmin = c;
	    }
	}
	return argmin;
}
    
    /// concatenate DD arrays
    public static DD[] concat(DD[] first, DD[]... rest) {
	int totalLength = first.length;
	for (DD[] array : rest) totalLength += array.length;	
	DD[] result = new DD[totalLength];
	// copy fist array
	System.arraycopy(first, 0, result, 0, first.length);
	int offset = first.length;
	for (DD[] array : rest) {
	    System.arraycopy(array, 0, result, offset, array.length);
	    offset += array.length;
	}
	return result;
    }

    /// first arg is not an array
    public static DD[] concat(DD f, DD[]... rest) {
	DD[] first = new DD[1];
	first[0]   = f;
	return concat(first,rest);
    }

    /// append DD to a DD[]
    public static DD[] append(DD[] first, DD l) {
	DD[] last = new DD[1];
	last[0]   = l;
	return concat(first, last);
    }

    /**
     * sdecode:
     * 
     * map an assignment id from
     * [0, IntegerArray.product(sizes)-1] to an array with
     * the corresponding joint assignment of each variable
     * when all entries in sizes are the same, this becomes a
     * change of base
     */
    public static int[] sdecode(int sid, int n, int sizes[]) {
	// make sure sid is in the right range
	if (sid < 0 || sid > IntegerArray.product(sizes) - 1) {
	    System.out.println("Error calling sdecode");
	    return null;
	}
	// calculate joint assignment
	int q  = sid;
	int ja[] = IntegerArray.fill(n, 0);
	for(int i=0; i<n; i++) {
	    if (q==0) break;
	    ja[i] = q % sizes[i];
	    q = q / sizes[i];
	}
	// add 1 to each entry to comply with format
	for(int i=0; i<n; i++) ja[i]++;
	return ja;
    }

    /**
     * sencode:
     * 
     * encode state, complement of sdecode
     * receives factored state starting from 1
     * and returns factored state in the same form
     */ 
    public static int sencode(int fstate[], int n, int sizes[]) {
	// make sure fstate is in the right range
	// subtract 1 for format
	// for(int i=0; i<n; i++) fstate[i]--; // BUG HERE!
	int s = 0;
	int f = 1;
	for(int i=0;i<n;i++) {
	    s += f * (fstate[i] - 1); // remember that fstate starts from 1
	    f *= sizes[i];
	}
	// back to format from 1
	return s + 1;
    }

    public static int[] convertIntegers(ArrayList<Integer> integers) {
	int[] ret = new int[integers.size()];
	for (int i=0; i < ret.length; i++) {
	    ret[i] = integers.get(i).intValue();
	}
	return ret;
    }

} // Utils
