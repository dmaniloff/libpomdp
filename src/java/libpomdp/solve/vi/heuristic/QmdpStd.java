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

import libpomdp.common.AlphaVector;
import libpomdp.common.std.PomdpStd;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.IterationStats;
import libpomdp.solve.vi.ValueIterationStd;

public class QmdpStd extends ValueIterationStd {
    
	public AlphaVector Vt;
	
	public QmdpStd(PomdpStd pomdp){
		startTimer();
		initValueIteration(pomdp);
		current=new ValueFunctionStd(pomdp.nrStates());
		for(int a=0; a<pomdp.nrActions(); a++)	
		    current.push(new AlphaVector(pomdp.nrStates(),a));
		Vt=new AlphaVector(pomdp.nrStates());
		registerInitTime();
	}
	
	@Override
	public IterationStats iterate() {
		startTimer();
		old=current.copy();
		current=new ValueFunctionStd(pomdp.nrStates());
		for(int a=0; a<pomdp.nrActions(); a++){
			AlphaVector res=pomdp.mdpValueUpdate(Vt, a);
    	    current.push(res);
    	}
		for (int s=0;s<pomdp.nrStates();s++){
			double colmax=Double.NEGATIVE_INFINITY;
			for(int a=0; a<pomdp.nrActions(); a++){
				double val=current.getAlphaElement(a,s);
				if (val > colmax)
					colmax=val;
			}
			Vt.setValue(s, colmax);
		}
		registerValueIterationStats();
    	return iterationStats;
	}
} // qmdpFlat