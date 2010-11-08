package libpomdp.solve.vi.exact;

import java.util.ArrayList;

import libpomdp.common.CustomVector;
import libpomdp.common.std.BeliefMdpStd;
import libpomdp.common.std.PomdpStd;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.IterationStats;
import libpomdp.solve.Criteria;
import libpomdp.solve.vi.ValueIterationStats;
import libpomdp.solve.vi.ValueIterationStd;


public class OnePassStd extends ValueIterationStd {
	
	BeliefMdpStd bmdp;
	
	public OnePassStd(PomdpStd pomdp){
		long inTime = System.currentTimeMillis();
		this.pomdp=pomdp;
		iterationStats=new ValueIterationStats(pomdp);
		stopCriterias= new ArrayList<Criteria>();
		bmdp=new BeliefMdpStd(pomdp);
		current = new ValueFunctionStd(pomdp.nrStates());
		current.push(new CustomVector(pomdp.nrStates()), 0);
		iterationStats.init_time = System.currentTimeMillis() - inTime;
	}
	@Override
	public IterationStats iterate() {
		long inTime = System.currentTimeMillis();
		System.out.println("== Iteration "+iterationStats.iterations+" ==");
		old=current;
		current = new ValueFunctionStd(bmdp.nrStates());
		for(int a=0; a<bmdp.nrActions(); a++){
			for (int idx=0;idx<old.size();idx++){
				CustomVector alpha=old.getVectorRef(idx);
				CustomVector res=new CustomVector(bmdp.nrStates());
				for (int o=0;o<bmdp.nrObservations();o++){
					res.add(bmdp.getTau(a,o).mult(pomdp.getGamma(),alpha));
				}
				res.add(bmdp.getRewardValues(a));
				current.push(res, a);
			}
		}
		iterationStats.register(System.currentTimeMillis() - inTime, current.size());
    	return iterationStats;
	}

}
