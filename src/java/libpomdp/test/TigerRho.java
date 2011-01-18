package libpomdp.test;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.rho.RewardFunction;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.offline.pointbased.PointSet;

public class TigerRho extends RewardFunction {

    @Override
    public ValueFunctionStd approximate(int a, PointSet bset) {
	ValueFunctionStd retval = new ValueFunctionStd(2);
	if (a == 0) {
	    for (BeliefState bel : bset) {
		CustomVector vec = bel.getPoint();
		if (vec.max() == 1.0)
		    continue;
		AlphaVector val = new AlphaVector(2, a);
		val.setValue(0, Math.log(Math.E * vec.get(0)) / Math.log(2));
		val.setValue(1, Math.log(Math.E * vec.get(1)) / Math.log(2));
		retval.push(val);
	    }
	} else if (a == 1) {
	    AlphaVector val = new AlphaVector(2, a);
	    val.setValue(0, -100);
	    val.setValue(1, 10);
	    retval.push(val);
	} else if (a == 2) {
	    AlphaVector val = new AlphaVector(2, a);
	    val.setValue(0, 10);
	    val.setValue(1, -100);
	    retval.push(val);
	}
	return (retval);
    }

    @Override
    public double max(int a) {
	if (a == 0)
	    return 0;
	else
	    return 10;
    }

    @Override
    public double min(int a) {
	if (a == 0)
	    return -1;
	else
	    return -100;
    }

    @Override
    public double sample(BeliefState b, int a) {
	double val = 0;
	if (a == 0) {
	    CustomVector vec = b.getPoint();
	    val = vec.get(0) * Math.log(vec.get(0)) / Math.log(2) + vec.get(1)
		    * Math.log(vec.get(1)) / Math.log(2);
	} else if (a == 1) {
	    AlphaVector alp = new AlphaVector(2, a);
	    alp.setValue(0, -100);
	    alp.setValue(1, 10);
	    val = alp.eval(b);
	} else if (a == 2) {
	    AlphaVector alp = new AlphaVector(2, a);
	    alp.setValue(0, 10);
	    alp.setValue(1, -100);
	    val = alp.eval(b);
	}
	return val;
    }

}
