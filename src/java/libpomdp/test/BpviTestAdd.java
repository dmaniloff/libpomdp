package libpomdp.test;

import libpomdp.common.add.PomdpAdd;
import libpomdp.parser.FileParser;
import libpomdp.solve.offline.Criteria;
import libpomdp.solve.offline.MaxIterationsCriteria;
import libpomdp.solve.offline.vi.ValueConvergenceCriteria;
import libpomdp.solve.offline.vi.ValueIterationStats;
import libpomdp.solve.offline.bounds.BpviAdd;

public class BpviTestAdd {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		PomdpAdd pomdp=(PomdpAdd)FileParser.loadPomdp("data/problems/tiger/tiger.95.SPUDD", FileParser.PARSE_SPUDD);
		BpviAdd algo= new BpviAdd(pomdp);
        algo.getBlindAdd();
		// algo.addStopCriteria(new MaxIterationsCriteria(50000));
		// algo.addStopCriteria(new ValueConvergenceCriteria(1e-3,Criteria.CC_MAXEUCLID));
		// algo.run();
		// ValueIterationStats stat=(ValueIterationStats) algo.getStats();
		// System.out.println(stat);
		//System.out.println(val);
	}

}
