/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: aems2.java
 * Description: implementation of the heuristic interface for 
 *              AEMS2 / Hansen's policy search - please refer to
 *              the README references [2] and [4] in the root dir
 * Copyright (c) 2009, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import org.math.array.*;
import java.util.*;

public class aems2 implements heuristic {

    /// main property is the pomdp spec
    private pomdp problem;    

    /// set the gen only once for every instance
    private Random gen = new Random(System.currentTimeMillis());

    /// constructor
    public aems2 (pomdp prob) {
	this.problem = prob;
    }
   
    /// H(b)
    public double h_b(orNode o) {
	return o.u - o.l;
    }

    /// H(b,a)
    /// given that this value depends on the
    /// other branches as well we compute it
    /// at the orNode level
    public double[] h_ba(orNode o) {
	double UbA[] = new double[problem.getnrAct()];
	double Hba[];
	int a,argmax;
	// copy upper bounds to a separate array
	for(a=0; a<problem.getnrAct(); a++) {
	    UbA[a] = o.children[a].u;
	}
	// compute maximum upper bound
	argmax = argmax(UbA);
	// set the chosen action's Hba value to 1
	Hba = DoubleArray.fill(problem.getnrAct(),0.0);
	Hba[argmax] = 1.0;
	// return
	return Hba;
    }

    /// H(b,a,o) = \gamma * P(o|b,a)
    public double h_bao(orNode o) {
	return problem.getGamma() * o.belief.poba;
    }

    /// H*(b,a) = \max_o {H(b,a,o) H*(tao(b,a,o))}
    public double hANDStar(andNode a) {
    	//return	a.h_o[a.bestO] * a.children[a.bestO].hStar;
	return  a.children[a.oStar].h_bao * a.children[a.oStar].hStar;
    }

    // H*(b) = H(b,a*) H*(b,a*)
    public double hORStar(orNode o) {
	return o.h_ba[o.aStar] * o.children[o.aStar].hStar;
    }

    /// o* = argmax_o {H(b,a,o) H*(tao(b,a,o))}
    public int oStar(andNode a) {
	double HbaoHostar[] = new double[problem.getnrObs()];
	int o;
	// gather H(b,a,o) H*(tao(b,a,o))
	for(o=0; o<problem.getnrObs(); o++) {
	    //h_ostar[o]    = a.children[o].hStar;
	    HbaoHostar[o] = a.children[o].h_bao * a.children[o].hStar;
	}
	// element-wise product with H(b,a,o)
	//double HbaoHostar[] = LinearAlgebra.times(a.h_o,h_ostar);
	return argmax(HbaoHostar);
    }

    /// a* = argmax_a {H(b,a) H*(b,a)}
    public int aStar(orNode o) {
	double h_astar[] = new double[problem.getnrAct()];
	int a;
	for(a=0; a<problem.getnrAct(); a++)
	    h_astar[a] = o.children[a].hStar;
	// element-wise product with H(b,a)
	double HbaHbastar[] = LinearAlgebra.times(o.h_ba, h_astar);
	return argmax(HbaHbastar);
    }

   

    /// general randomized argmax of a vector of doubles
    /// public for debugging
    public int argmax(double v[]) {
	// declarations
	//Random gen    = new Random(System.currentTimeMillis());
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
		
// 	ArrayList<Integer> repi = new ArrayList<Integer>();
// 	int a,r;
// 	
// 	// compute maximum
// 	maxv = DoubleArray.max(v);
// 	// locate repeated values
// 	for(a=0; a<v.length; a++) 
// 	    if(v[a] == maxv) repi.add(new Integer(a));
// 	// randomize among them if necessary
// 	//if (repi.size() > 1) System.out.println("will rand, check!!");
// 	r = g.nextInt(repi.size());
// 	// return chosen index
// 	return repi.get(r);
    } // argmax

} // aems2