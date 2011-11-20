package libpomdp.test;

import libpomdp.common.std.PomdpStd;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.parser.FileParser;
import libpomdp.solve.offline.Criteria;
import libpomdp.solve.offline.MaxIterationsCriteria;
import libpomdp.solve.offline.vi.ValueConvergenceCriteria;
import libpomdp.solve.offline.vi.ValueIterationStats;
import libpomdp.solve.offline.bounds.BpviStd;

public class BPVItest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		PomdpStd pomdp=(PomdpStd)FileParser.loadPomdp("data/problems/tag/tagAvoid.POMDP", FileParser.PARSE_CASSANDRA_POMDP);
		BpviStd algo= new BpviStd(pomdp);
		algo.addStopCriteria(new MaxIterationsCriteria(50000));
		algo.addStopCriteria(new ValueConvergenceCriteria(1e-3,Criteria.CC_MAXEUCLID));
		algo.run();
		ValueIterationStats stat=algo.getStats();
		ValueFunctionStd val=algo.getValueFunction();
		System.out.println(stat);
		//System.out.println(val);
	}

}
