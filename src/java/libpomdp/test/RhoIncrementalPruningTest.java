package libpomdp.test;

import libpomdp.common.rho.RhoPomdp;
import libpomdp.common.std.BeliefStateStd;
import libpomdp.common.std.PomdpStd;
import libpomdp.parser.FileParser;
import libpomdp.solve.Criteria;
import libpomdp.solve.MaxIterationsCriteria;
import libpomdp.solve.offline.ValueConvergenceCriteria;
import libpomdp.solve.offline.ValueIterationStats;
import libpomdp.solve.offline.exact.IncrementalPruningStd;
import libpomdp.solve.offline.pointbased.PointBasedStd;
import libpomdp.solve.offline.pointbased.PointSet;

public class RhoIncrementalPruningTest {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	// tiger/tiger.95.POMDP
	PomdpStd pomdp = (PomdpStd) FileParser.loadPomdp(
		"data/problems/tiger/tiger.95.POMDP",
		FileParser.PARSE_CASSANDRA_POMDP);
	RhoPomdp rpomdp = new RhoPomdp(pomdp, new TigerRho());
	double epsi = 1e-6 * (1 - pomdp.getGamma()) / (2 * pomdp.getGamma());
	rpomdp.approxReward(randomPoints(pomdp, 10));
	IncrementalPruningStd algo = new IncrementalPruningStd(rpomdp, epsi);
	algo.addStopCriteria(new MaxIterationsCriteria(100));
	algo.addStopCriteria(new ValueConvergenceCriteria(epsi,
		Criteria.CC_MAXDIST));
	algo.run();
	System.out.println(algo.getValueFunction());
	ValueIterationStats stat = (ValueIterationStats) algo.getStats();
	System.out.println(stat);
	// System.out.println(val);
    }

    private static PointSet randomPoints(PomdpStd pomdp, int size) {
	PointSet bset = new PointSet();
	bset.add(pomdp.getInitialBeliefState());
	PointSet testBset = bset.copy();
	while (bset.size() < size) {
	    BeliefStateStd point = null;
	    point = PointBasedStd.collectRandomExplore(testBset, pomdp);
	    if (point != null)
		bset.add(point);
	    if (testBset.size() == 0)
		testBset = bset.copy();
	}
	return (bset);
    }
}
