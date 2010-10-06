/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: aems2.java
 * Description: implementation of the heuristic interface for 
 *              AEMS2 / Hansen's policy search - please refer to
 *              the README references [2] and [4] in the root dir
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.solve.java.online;

// imports
import java.util.Random;

import libpomdp.common.java.Pomdp;
import libpomdp.common.java.Utils;

import org.math.array.DoubleArray;

public class aems2 implements expandHeuristic {

    /// main property is the pomdp spec
    private Pomdp problem;    

    /// set the gen only once for every instance
    private Random gen = new Random(System.currentTimeMillis());

    /// constructor
    public aems2 (Pomdp prob) {
	this.problem = prob;
    }
   
    /// H(b)
    public double h_b(orNode o) {
// 	if(o.u - o.l < 0) System.err.println("bad H(b) at ornode" + o.getobs()  + 
// 					     " parent is action " + o.getParent().getAct()
// 					     + " hb is: "+ (o.u - o.l));
// 	System.err.println("upper is" + o.u);
// 	System.err.println("lower is" + o.l);
	return o.u - o.l;
    }

    /// H(b,a)
    /// given that this value depends on the
    /// other branches as well we compute it
    /// at the orNode level
    public double[] h_ba(orNode o) {
	double UbA[] = new double[problem.nrActions()];
	double Hba[];
	for(andNode a : o.children) UbA[a.getAct()] = a.u;
	Hba = DoubleArray.fill(problem.nrActions(), 0.0);
	// compute maximum upper bound
	// set the chosen action's Hba value to 1
	int aStar = Utils.argmax(UbA);
	Hba[aStar] = 1.0;
	// save this value
	o.aStar = aStar;
	// return
	return Hba;
    }

    /// H(b,a) update version
    /// does this work?? needs to be tested...
    public double[] h_baUpdate(orNode o, int a) {
	double challenge = o.children[a].u;
	int argmax = Utils.argmax(new double[] {o.children[o.aStar].u, 
						 challenge});
	if(0==argmax) {
	    return o.h_ba;
	} else {
	    o.h_ba[o.aStar] = 0.0;
	    o.h_ba[a]       = 1.0;
	    o.aStar         = a;
	    return o.h_ba;
	}
    } // h_baUpdate

    /// H(b,a,o) = \gamma * P(o|b,a)
    /// be means of a depth value we could get rid of this
    /// product............
    public double h_bao(orNode o) {	
	return problem.getGamma() * o.belief.getPoba();
    }

    /// H*(b,a) = \max_o {H(b,a,o) H*(tao(b,a,o))}
    public double hANDStar(andNode a) {
	return  a.children[a.oStar].h_bao * a.children[a.oStar].hStar;
    }

    /// H*(b) = H(b,a*) H*(b,a*)
    public double hORStar(orNode o) {
	return o.h_ba[o.aStar] * o.children[o.aStar].hStar;
    }

    /// o* = argmax_o {H(b,a,o) H*(tao(b,a,o))}
    public int oStar(andNode a) {
	double HbaoHostar[] = new double[problem.nrObservations()];	
	//for(orNode o : a.children) HbaoHostar[o.getobs()] = o.h_bao * o.hStar;
	//return Common.argmax(HbaoHostar);

	// STILL TO THINK ABOUT:
	//int nullCount=0;
	//for(orNode o : a.children) 
	for(int o=0; o<problem.nrObservations(); o++) {
	    if(a.children[o] != null) {
		HbaoHostar[o] = a.children[o].h_bao * a.children[o].hStar;
	    } else {
		HbaoHostar[o] = -1; // do this to preserve the argmax
		//nullCount++;
	    }
	}
	// System.out.println("nullcount inside oStar is "+ nullCount);
	// 	System.out.println(DoubleArray.toString(HbaoHostar));
	// 	int argmax = Common.argmax(HbaoHostar);
	// 	if (argmax < 0 || argmax >= problem.getnrObs())
	// 	    System.err.println("armax out of bounds");
	// 	if (HbaoHostar[argmax] == -1) {
	// 	    System.err.println("Hba[argmax]=-1");
	// 	    //System.exit(0);
	// 	}	
	//return argmax;	
	return Utils.argmax(HbaoHostar);
    }

    /// update version with challenge obs
    /// INCORRECT
    // public int oStarUpdate(andNode a, int o) {
    // 	double challenge = a.children[o].h_bao * a.children[o].hStar;
    // 	int argmax = Common.argmax(new double[] {a.children[a.oStar].h_bao * a.children[a.oStar].hStar,
    // 						 challenge});
    // 	if(0==argmax)
    // 	    return a.oStar;
    // 	else
    // 	    return o;
    //     }

    /// a* = argmax_a {H(b,a) H*(b,a)}
    /// given the way H(b,a) is computed in AEMS2, we don't
    /// need to compute the actual argmax, we just return the
    /// stored value from the computation of H(b,a)
    public int aStar(orNode o) {	
	return o.aStar;
    }

} // aems2