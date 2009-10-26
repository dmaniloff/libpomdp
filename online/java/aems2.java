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
    pomdp problem;    

    /// constructor
    public aems2 (pomdp prob) {
	this.problem = prob;
    }

    /// H(b)
    public double hOR(orNode o) {
	return o.u - o.l;
    }

    /// H(b,a)
    /// given that this value depends on the
    /// other branches as well we compute it
    /// at the orNode level
    public double[] hOR_a(orNode o) {
	double UbA[] = new double[problem.getnrAct()];
	double Hba[] = new double[problem.getnrAct()];
	double maxUbA;
	ArrayList<Integer> repi = new ArrayList<Integer>();
	int a,r;
	Random g = new Random();
	// copy upper bounds to a separate array
	for(a=0; a<problem.getnrAct(); a++) 
	    UbA[a] = o.children[a].u;
	// compute maximum upper bound
	maxUbA = DoubleArray.max(UbA);
	// locate repeated values
	for(a=0; a<problem.getnrAct(); a++) 
	    if(UbA[a] == maxUbA) repi.add(new Integer(a));
	// randomize among them if necessary
	if (repi.size() > 1) System.out.println("will rand among uba, check!!");
	r = g.nextInt(repi.size());
	// set the chosen action's Hba value to 1
	for(a=0; a<problem.getnrAct(); a++) {
	    if(a==repi.get(r))
		Hba[a] = 1.0;
	    else
		Hba[a] = 0.0;
	}
	return Hba;
    }

    /// H(b,a,o)
    /// given that H(b,a,o) in AEMS2 = P(o|b,a)
    /// we will attach it to the andNode and compute
    /// the whole vector at once
    public double[] hAND_o(andNode a) {
	return LinearAlgebra.times(problem.P_Oba(a.getParent().belief, a.act), 
				   problem.getGamma());
    }

    /// argmax_o H(b,a,o) H*(tao(b,a,o))
    public int bestO(andNode a) {
	double h_ostar[] = new double[problem.getnrObs()];
	int o;
	// copy hStar values of a's children
	for(o=0; o<problem.getnrObs(); o++) 
	    h_ostar[o] = a.children[o].hStar;
	// element-wise product with H(b,a,o)
	double HbaoHstar[] = LinearAlgebra.times(a.h_o,h_ostar);
	return argmax(HbaoHstar);
    }

    /// argmax_a H(b,a) H*(b,a)
    public int bestA(orNode o) {
	double h_astar[] = new double[problem.getnrAct()];
	int a;
	for(a=0; a<problem.getnrAct(); a++)
	    h_astar[a] = o.children[a].hStar;
	// element-wise product with H(b,a)
	double HbaHbastar = LinearAlgebra.times(o.h_a, h_astar);
	return argmax(HbaHbastar);
    }

    /// H*(b,a)
    public double hANDStar(andNode a) {
	return	a.h_o[a.bestO] * a.children[a.bestO].hStar;
    }

    /// general randomized argmax of a vector of doubles
    private int argmax(double v[]) {
	// declarations
	double maxv;
	ArrayList<Integer> repi = new ArrayList<Integer>();
	int a,r;
	Random g = new Random();
	// compute maximum
	maxv = DoubleArray.max(v);
	// locate repeated values
	for(a=0; a<v.length; a++) 
	    if(v[a] == maxv) repi.add(new Integer(a));
	// randomize among them if necessary
	if (repi.size() > 1) System.out.println("will rand among uba, check!!");
	r = g.nextInt(repi.size());
	// return chosen index
	return repi.get(r);
    }

} // aems2