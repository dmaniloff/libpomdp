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

package libpomdp.online.java;

import libpomdp.general.java.Common;
import libpomdp.general.java.pomdp;

import org.math.array.DoubleArray;




public class AEMS2 implements ExpandHeuristic {

    /// main property is the pomdp spec
    private pomdp problem;    

    /// set the gen only once for every instance
    //private Random gen = new Random(System.currentTimeMillis());

    /// constructor
    public AEMS2 (pomdp prob) {
	this.problem = prob;
    }
   
    /// H(b)
    public double h_b(HeuristicSearchOrNode o) {
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
    public double[] h_ba(HeuristicSearchOrNode o) {
	double UbA[] = new double[problem.getnrAct()];
	double Hba[];
	for(HeuristicSearchAndNode a : o.getChildren()) UbA[a.getAct()] = a.u;
	Hba = DoubleArray.fill(problem.getnrAct(), 0.0);
	// compute maximum upper bound
	// set the chosen action's Hba value to 1
	int aStar = Common.argmax(UbA);
	Hba[aStar] = 1.0;
	// save this value
	o.aStar = aStar;
	// return
	return Hba;
    }

    /// H(b,a) update version
    /// does this work?? needs to be tested...
//    public double[] h_baUpdate(orNode o, int a) {
//	double challenge = o.children[a].u;
//	int argmax = Common.argmax(new double[] {o.children[o.aStar].u, 
//						 challenge});
//	if(0==argmax) {
//	    return o.h_ba;
//	} else {
//	    o.h_ba[o.aStar] = 0.0;
//	    o.h_ba[a]       = 1.0;
//	    o.aStar         = a;
//	    return o.h_ba;
//	}
//    } // h_baUpdate

    /// H(b,a,o) = \gamma * P(o|b,a)
    public double h_bao(HeuristicSearchOrNode o) {	
	return problem.getGamma() * o.getBeliefState().getpoba();
    }

    /// H*(b,a) = \max_o {H(b,a,o) H*(tao(b,a,o))}
    public double hANDStar(HeuristicSearchAndNode a) {
	return a.getChild(a.oStar).h_bao * a.getChild(a.oStar).hStar;
    }

    /// H*(b) = H(b,a*) H*(b,a*)
    public double hORStar(HeuristicSearchOrNode o) {
	return o.h_ba[o.aStar] * o.getChild(o.aStar).hStar;
    }

    /// o* = argmax_o {H(b,a,o) H*(tao(b,a,o))}
    public int oStar(HeuristicSearchAndNode a) {
	double HbaoHostar[] = new double[problem.getnrObs()];	
	//for(orNode o : a.children) HbaoHostar[o.getobs()] = o.h_bao * o.hStar;
	//return Common.argmax(HbaoHostar);

	// STILL TO THINK ABOUT:
	//int nullCount=0;
	//for(orNode o : a.children) 
	for(int o=0; o<problem.getnrObs(); o++) {
	    if(a.getChild(o) != null) {
		HbaoHostar[o] = a.getChild(o).h_bao * 
			a.getChild(o).hStar;
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
	return Common.argmax(HbaoHostar);
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
    public int aStar(HeuristicSearchOrNode o) {	
	return o.aStar;
    }

} // aems2