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


public class IncrementalPruningStd extends ValueIterationStd {
	
	BeliefMdpStd bmdp;
	private double delta;
	
	public IncrementalPruningStd(PomdpStd pomdp, double delta){
		this.delta=delta;
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
			// Perform Projections
			ArrayList<ValueFunctionStd> psi=new ArrayList<ValueFunctionStd>();
			for (int o=0;o<bmdp.nrObservations();o++){
				ValueFunctionStd proj = new ValueFunctionStd(bmdp.nrStates());
				for (int idx=0;idx<old.size();idx++){
					CustomVector alpha=old.getVectorCopy(idx);
					CustomVector res=new CustomVector(bmdp.nrStates());
					res.add(bmdp.getTau(a,o).mult(pomdp.getGamma(),alpha));
					res.add(bmdp.getRewardValues(a).scale(1.0/(double)bmdp.nrObservations()));
					proj.push(res, a);
				}
				iterationStats.registerLp(proj.prune(delta));
				psi.add(proj);
			}
			//Now Cross sum...
			while (psi.size()>1){
				ValueFunctionStd vfA=psi.remove(0);
				ValueFunctionStd vfB=psi.remove(0);
				vfA.crossSum(vfB);
				iterationStats.registerLp(vfA.prune(delta));
				psi.add(vfA);
			}
			ValueFunctionStd vfA=psi.remove(0);	
			current.merge(vfA);
		
		}
		iterationStats.registerLp(current.prune(delta));
		System.out.println(current);
		iterationStats.register(System.currentTimeMillis() - inTime, current.size());
    	return iterationStats;
	}

}
