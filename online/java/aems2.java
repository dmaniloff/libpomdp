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

} // aems2