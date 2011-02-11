package libpomdp.test;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.RhoFunction;
import libpomdp.common.ValueFunction;
import libpomdp.common.std.AlphaVectorStd;
import libpomdp.common.std.BeliefStateStd;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.offline.pointbased.PointSet;

public class TigerRho implements RhoFunction {

    
    public ValueFunctionStd approximate(int a, PointSet bset) {
	ValueFunctionStd retval = new ValueFunctionStd();
	if (a == 0) {
	    for (BeliefState bel : bset) {
		CustomVector vec = bel.getPoint();
		if (vec.max() == 1.0)
		    continue;
		AlphaVectorStd val = new AlphaVectorStd(2, a);
		val.setValue(0, Math.log(Math.E * vec.get(0)) / Math.log(2));
		val.setValue(1, Math.log(Math.E * vec.get(1)) / Math.log(2));
		retval.push(val);
	    }
	} else if (a == 1) {
	    AlphaVectorStd val = new AlphaVectorStd(2, a);
	    val.setValue(0, -100);
	    val.setValue(1, 10);
	    retval.push(val);
	} else if (a == 2) {
	    AlphaVectorStd val = new AlphaVectorStd(2, a);
	    val.setValue(0, 10);
	    val.setValue(1, -100);
	    retval.push(val);
	}
	return (retval);
    }

    
    public double max(int a) {
	if (a == 0)
	    return 0;
	else
	    return 10;
    }

    
    public double min(int a) {
	if (a == 0)
	    return -1;
	else
	    return -100;
    }

    
    public double sample(BeliefState b, int a) {
	double val = 0;
	if (a == 0) {
	    CustomVector vec = b.getPoint();
	    val = vec.get(0) * Math.log(vec.get(0)) / Math.log(2) + vec.get(1)
		    * Math.log(vec.get(1)) / Math.log(2);
	} else if (a == 1) {
	    AlphaVectorStd alp = new AlphaVectorStd(2, a);
	    alp.setValue(0, -100);
	    alp.setValue(1, 10);
	    val = alp.eval(b);
	} else if (a == 2) {
	    AlphaVectorStd alp = new AlphaVectorStd(2, a);
	    alp.setValue(0, 10);
	    alp.setValue(1, -100);
	    val = alp.eval(b);
	}
	return val;
    }

	
	public ValueFunction getValueFunction(int a) {
		BeliefStateStd b = BeliefStateStd.getUniformBelief(2);
		PointSet bset=new PointSet();
		bset.add(b);
		return approximate(a,bset);
	}

	
	public double max() {
		return 10;
	}

	
	public double min() {
		return -100;
	}

	
	public double get(int state, int action) {
		switch (action){
			case 0:
				return(-1);
			case 1:
				if (state==1)
					return(10);
				else 
					return(-100);
			case 2:
				if (state==1)
					return(-100);
				else 
					return(10);
			}
		return Double.NEGATIVE_INFINITY;
	}


}
