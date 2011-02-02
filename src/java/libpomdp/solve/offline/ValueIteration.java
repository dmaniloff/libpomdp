/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: ValueIteration.java
 * Description: 
 * Copyright (c) 2010, 2011 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.solve.offline;

import libpomdp.common.Pomdp;
import libpomdp.common.ValueFunction;
import libpomdp.solve.Iteration;
import libpomdp.solve.IterationStats;

public abstract class ValueIteration extends Iteration {

    protected Pomdp pomdp;
    protected ValueFunction current;
    protected ValueFunction old;

    public IterationStats getStats() {
    	return (iterationStats);
    }
    
    protected void initValueIteration(Pomdp pomdp) {
	this.pomdp = pomdp;
	initIteration();
	iterationStats = new ValueIterationStats(pomdp);
    }

    public Pomdp getPomdp() {
	return pomdp;
    }

    public ValueFunction getValueFunction() {
	return current;
    }

    public ValueFunction getOldValueFunction() {
	return old;
    }

    public abstract IterationStats iterate();

    public void registerValueIterationStats() {
	if (current != null) {
	    ((ValueIterationStats) iterationStats).iteration_vector_count
		    .add(new Integer(current.size()));
	}
	registerIterationTime();
    }
}
