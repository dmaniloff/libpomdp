package libpomdp.test;

import libpomdp.common.java.std.PomdpStd;
import libpomdp.common.java.std.ValueFunctionStd;
import libpomdp.parser.java.FileParser;
import libpomdp.solve.java.MaxIterationsCriteria;
import libpomdp.solve.java.vi.ValueConvergenceCriteria;
import libpomdp.solve.java.vi.ValueIterationStats;
import libpomdp.solve.java.vi.heuristic.BpviStd;

public class BPVItest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		PomdpStd pomdp=(PomdpStd)FileParser.loadPomdp("data/problems/tiger/tiger.95.POMDP", FileParser.PARSE_CASSANDRA_POMDP);
		BpviStd algo= new BpviStd(pomdp);
		algo.addStopCriteria(new MaxIterationsCriteria(500));
		algo.addStopCriteria(new ValueConvergenceCriteria(1e-4,ValueConvergenceCriteria.CC_MAXEUCLID));
		algo.run();
		ValueIterationStats stat=algo.getStats();
		ValueFunctionStd val=algo.getValueFunction();
		System.out.println(stat);
		System.out.println(val);
	}

}
