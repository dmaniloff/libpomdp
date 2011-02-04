package libpomdp.common;

import libpomdp.solve.offline.pointbased.PointSet;

public abstract class RhoFunction extends RewardFunction {

    abstract public ValueFunction approximate(int a, PointSet bset);

}
