/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: ValueConvergenceCriteria.java
 * Description: 
 * Copyright (c) 2010, 2011 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.solve.offline;

import libpomdp.common.ValueFunction;
import libpomdp.solve.Criteria;
import libpomdp.solve.Iteration;

public class ValueConvergenceCriteria extends Criteria {

    double epsilon;
    int convCriteria;

    static final int MIN_ITERATIONS = 5;

    public boolean check(Iteration i) {
	ValueIteration vi = (ValueIteration) i;
	ValueFunction newv = vi.getValueFunction();
	ValueFunction oldv = vi.getOldValueFunction();
	double conv=newv.performance(oldv,convCriteria);
	System.out.println("Eval(" + i.getStats().iterations + ") = " + conv);
	if (conv <= epsilon && i.getStats().iterations > MIN_ITERATIONS)
	    return (true);
	return false;
    }

    @Override
    public boolean valid(Iteration vi) {
	if (vi instanceof ValueIteration) {
	    return true;
	}
	return false;
    }

    public ValueConvergenceCriteria(double epsilon, int convCriteria) {
	this.epsilon = epsilon;
	this.convCriteria = convCriteria;
    }

}
