package libpomdp.common;

import libpomdp.solve.offline.pointbased.PointSet;

public abstract class RhoFunction {

    abstract public double sample(BeliefState b, int a);

    abstract public ValueFunction approximate(int a, PointSet bset);

    abstract public double max(int a);

    abstract public double min(int a);

}
