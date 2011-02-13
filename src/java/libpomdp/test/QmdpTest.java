package libpomdp.test;

import libpomdp.common.std.PomdpStd;
import libpomdp.parser.FileParser;
import libpomdp.solve.Criteria;
import libpomdp.solve.MaxIterationsCriteria;
import libpomdp.solve.offline.ValueConvergenceCriteria;
import libpomdp.solve.offline.ValueIterationStats;
import libpomdp.solve.offline.heuristic.QmdpPolicyStd;

public class QmdpTest {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	PomdpStd pomdp = (PomdpStd) FileParser.loadPomdp(
		"/Users/diego/Documents/workspace/libpomdp/data/problems/rocksample/RockSample_7_8.POMDP",
		FileParser.PARSE_CASSANDRA_POMDP);
	QmdpPolicyStd algo = new QmdpPolicyStd(pomdp);
	double epsi = 1e-6 * (1 - pomdp.getGamma()) / (2 * pomdp.getGamma());
	algo.addStopCriteria(new MaxIterationsCriteria(100));
	algo.addStopCriteria(new ValueConvergenceCriteria(epsi,
		Criteria.CC_MAXDIST));
	algo.run();
	System.out.println(algo.getValueFunction());
	ValueIterationStats stat = (ValueIterationStats) algo.getStats();
	System.out.println(stat);
	// System.out.println(val);
    }

}
