package libpomdp.test;

import libpomdp.common.std.PomdpStd;
import libpomdp.parser.FileParser;
import libpomdp.solve.Criteria;
import libpomdp.solve.MaxIterationsCriteria;
import libpomdp.solve.vi.ValueConvergenceCriteria;
import libpomdp.solve.vi.ValueIterationStats;
import libpomdp.solve.vi.exact.IncrementalPruningStd;

public class IncrementalPruningTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//tiger/tiger.95.POMDP
		PomdpStd pomdp=(PomdpStd)FileParser.loadPomdp("data/problems/tiger/tiger.95.POMDP", FileParser.PARSE_CASSANDRA_POMDP);
		double epsi=1e-7*(1-pomdp.getGamma())/(2*pomdp.getGamma());
		IncrementalPruningStd algo= new IncrementalPruningStd(pomdp,epsi);
		algo.addStopCriteria(new MaxIterationsCriteria(1000));
		algo.addStopCriteria(new ValueConvergenceCriteria(epsi,Criteria.CC_MAXEUCLID));
		algo.run();
		ValueIterationStats stat=algo.getStats();
		System.out.println(stat);
		//System.out.println(val);
	}

}
