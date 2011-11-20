package libpomdp.test;

import libpomdp.common.std.PomdpStd;
import libpomdp.parser.FileParser;
import libpomdp.solve.offline.MaxIterationsCriteria;
import libpomdp.solve.offline.vi.ValueIterationStats;
import libpomdp.solve.offline.exact.OnePassStd;

public class OnePassTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		PomdpStd pomdp=(PomdpStd)FileParser.loadPomdp("data/problems/tiger/tiger.95.POMDP", FileParser.PARSE_CASSANDRA_POMDP);
		OnePassStd algo= new OnePassStd(pomdp);
		algo.addStopCriteria(new MaxIterationsCriteria(10));
		algo.run();
		ValueIterationStats stat=(ValueIterationStats) algo.getStats();
		System.out.println(stat);
		//System.out.println(val);
	}

}
