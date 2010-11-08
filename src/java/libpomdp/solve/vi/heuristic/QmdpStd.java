/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: mdp.java
 * Description: offline upper and lower bounds based on the underlying
 *              fully observable MDP (Vmdp, Qmdp, and Blind)
 *              have a look at the README references [6,2]
 *              do not try on large problems as this will run out of mem
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.solve.vi.heuristic;

// imports

import java.util.ArrayList;

import libpomdp.common.CustomVector;
import libpomdp.common.std.PomdpStd;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.Criteria;
import libpomdp.solve.IterationStats;
import libpomdp.solve.vi.ValueIterationStats;
import libpomdp.solve.vi.ValueIterationStd;

public class QmdpStd extends ValueIterationStd {
    
	public CustomVector Vt;
	
	public QmdpStd(PomdpStd pomdp){
		long inTime = System.currentTimeMillis();
		this.pomdp=pomdp;
		iterationStats=new ValueIterationStats(pomdp);
		stopCriterias= new ArrayList<Criteria>();
		current=new ValueFunctionStd(pomdp.nrStates());
		CustomVector vi=new CustomVector(pomdp.nrStates());
		vi.zero();
		for(int a=0; a<pomdp.nrActions(); a++)	
		    current.push(vi,a);
		Vt=vi;
		iterationStats.init_time = System.currentTimeMillis() - inTime;
	}
	
	@Override
	public IterationStats iterate() {
		long inTime = System.currentTimeMillis();
		//System.out.println("== Iteration "+iterationStats.iterations+" ==");
		old=current.copy();
		current=new ValueFunctionStd(pomdp.nrStates());
		for(int a=0; a<pomdp.nrActions(); a++){
			CustomVector res=pomdp.getTransitionProbs(a).mult(pomdp.getGamma(),Vt);
		    res.add(pomdp.getRewardValues(a));
    	    current.push(res,a);
    	}
		for (int s=0;s<pomdp.nrStates();s++){
			double colmax=Double.NEGATIVE_INFINITY;
			for(int a=0; a<pomdp.nrActions(); a++){
				double val=current.getVectorRef(a).get(s);
				if (val > colmax)
					colmax=val;
			}
			Vt.set(s, colmax);
		}
    	iterationStats.register(System.currentTimeMillis() - inTime, current.size());
    	return iterationStats;
	}
} // qmdpFlat