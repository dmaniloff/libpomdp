package libpomdp.test;

import libpomdp.common.std.PomdpStd;
import libpomdp.parser.FileParser;
import libpomdp.solve.Criteria;
import libpomdp.solve.MaxIterationsCriteria;
import libpomdp.solve.vi.ValueConvergenceCriteria;
import libpomdp.solve.vi.ValueIterationStats;
import libpomdp.solve.vi.heuristic.QmdpStd;

public class QmdpTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		PomdpStd pomdp=(PomdpStd)FileParser.loadPomdp("data/problems/tiger/tiger.95.POMDP", FileParser.PARSE_CASSANDRA_POMDP);
		QmdpStd algo= new QmdpStd(pomdp);
		algo.addStopCriteria(new MaxIterationsCriteria(50000));
		algo.addStopCriteria(new ValueConvergenceCriteria(1e-3,Criteria.CC_MAXEUCLID));
		algo.run();
		ValueIterationStats stat=algo.getStats();
		System.out.println(stat);
		//System.out.println(val);
	}

}
