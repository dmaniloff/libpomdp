/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: aems2.java
 * Description: implementation of the heuristic interface for 
 *              AEMS2 / Hansen's policy search - please refer to
 *              the README references [2] and [4] in the root dir
 * Copyright (c) 2010, Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import org.math.array.*;
import java.util.*;

public class aems2 implements expandHeuristic {

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
	for(andNode a : o.children) UbA[a.getAct()] = a.u;
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
    public double[] h_baUpdate(orNode o, int a) {
	double challenge = o.children[a].u;
	int argmax = Common.argmax(new double[] {o.children[o.aStar].u, 
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
	return problem.getGamma() * o.belief.getpoba();
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
	double HbaoHostar[] = new double[problem.getnrObs()];
	for(orNode o : a.children) HbaoHostar[o.getobs()] = o.h_bao * o.hStar;
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
    /// stored value from the computation of H(b,a) - can even DELETE this!
    public int aStar(orNode o) {
	//double h_astar[] = new double[problem.getnrAct()];
	//for(andNode a : o.children) h_astar[a.getAct()] = a.hStar;
	// element-wise product with H(b,a)
	//double HbaHbastar[] = LinearAlgebra.times(o.h_ba, h_astar);
	//return Common.argmax(HbaHbastar);
	return o.aStar;
    }

} // aems2