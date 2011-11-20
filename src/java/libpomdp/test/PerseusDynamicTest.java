package libpomdp.test;

import libpomdp.common.std.PomdpStd;
import libpomdp.parser.FileParser;
import libpomdp.solve.offline.Criteria;
import libpomdp.solve.offline.MaxIterationsCriteria;
import libpomdp.solve.offline.vi.ValueConvergenceCriteria;
import libpomdp.solve.offline.vi.ValueIterationStats;
import libpomdp.solve.offline.pointbased.PbParams;
import libpomdp.solve.offline.pointbased.PointBasedStd;

public class PerseusDynamicTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//tiger/tiger.95.POMDP
		PomdpStd pomdp=(PomdpStd)FileParser.loadPomdp("data/problems/tiger/tiger.95.POMDP", FileParser.PARSE_CASSANDRA_POMDP);
		double epsi=1e-6*(1-pomdp.getGamma())/(2*pomdp.getGamma());
		PbParams params=new PbParams(PbParams.BACKUP_ASYNC_FULL,PbParams.EXPAND_RANDOM_EXPLORE_DYNAMIC,1,100,100);
		PointBasedStd algo= new PointBasedStd(pomdp,params);
		algo.addStopCriteria(new MaxIterationsCriteria(100));
		algo.addStopCriteria(new ValueConvergenceCriteria(epsi,Criteria.CC_MAXDIST));
		algo.run();
		System.out.println(algo.getValueFunction());
		ValueIterationStats stat=(ValueIterationStats) algo.getStats();
		System.out.println(stat);
		//System.out.println(val);
	}

}
