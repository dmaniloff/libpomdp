package libpomdp.solve.vi.exact;

import java.util.ArrayList;

import libpomdp.common.AlphaVector;
import libpomdp.common.std.BeliefMdpStd;
import libpomdp.common.std.PomdpStd;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.IterationStats;
import libpomdp.solve.vi.ValueIterationStats;
import libpomdp.solve.vi.ValueIterationStd;


public class IncrementalPruningStd extends ValueIterationStd {
	
	BeliefMdpStd bmdp;
	private double delta;
	
	public IncrementalPruningStd(PomdpStd pomdp, double delta){
		startTimer();
		initValueIteration(pomdp);
		this.delta=delta;
		bmdp=new BeliefMdpStd(pomdp);
		current = new ValueFunctionStd(pomdp.nrStates());
		current.push(new AlphaVector(bmdp.nrStates()));
		registerInitTime();
	}
	
	public IterationStats iterate() {
		startTimer();
		old=current;
		ValueIterationStats iterationStats=(ValueIterationStats) this.iterationStats;
		current = new ValueFunctionStd(bmdp.nrStates());
		for(int a=0; a<bmdp.nrActions(); a++){
			// Perform Projections
			ArrayList<ValueFunctionStd> psi=new ArrayList<ValueFunctionStd>();
			for (int o=0;o<bmdp.nrObservations();o++){
				ValueFunctionStd proj = new ValueFunctionStd(bmdp.nrStates());
				for (int idx=0;idx<old.size();idx++){
					AlphaVector alpha=old.getAlpha(idx);
					AlphaVector res=bmdp.projection(alpha, a, o);
					res.add(bmdp.getRewardValues(a).scale(1.0/(double)bmdp.nrObservations()));
					proj.push(res);
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
		registerValueIterationStats();
    	return iterationStats;
	}

}
