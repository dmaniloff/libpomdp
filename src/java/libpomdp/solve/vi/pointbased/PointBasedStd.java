package libpomdp.solve.vi.pointbased;

import libpomdp.common.AlphaVector;
import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.std.BeliefMdpStd;
import libpomdp.common.std.BeliefStateStd;
import libpomdp.common.std.PomdpStd;
import libpomdp.common.std.ValueFunctionStd;
import libpomdp.solve.IterationStats;
import libpomdp.solve.vi.ValueIterationStd;

public class PointBasedStd extends ValueIterationStd {
	
	BeliefMdpStd bmdp;
	PointSet fullBset;
	PointSet newBset;
	PbParams params;
	
	public AlphaVector getLowestAlpha(){
		double best_val=Double.NEGATIVE_INFINITY;
		for (int a=0;a<bmdp.nrActions();a++){
			CustomVector rvect=bmdp.getRewardValues(a);
			double val=rvect.min();
			if (val > best_val)
				best_val=val;
		}
		best_val=best_val/(1-bmdp.getGamma());
		return(new AlphaVector(CustomVector.getHomogene(bmdp.nrStates(), best_val),-1));
	}
	
	public PointBasedStd(PomdpStd pomdp,PbParams params){
		startTimer();
		initValueIteration(pomdp);
		this.params=params;
		bmdp=new BeliefMdpStd(pomdp);
		current = new ValueFunctionStd(pomdp.nrStates());
		current.push(getLowestAlpha());
		registerInitTime();
	}
	
	public IterationStats iterate() {
		startTimer();
		old=current;
		expand();
		//System.out.println("size(B)="+fullBset.size());
		
		for (int i=0;i<params.backupHorizon;i++){
			backup();
		}
		current.prune();
		if (params.isNewPointsOnly())
			fullBset=newBset;
		//System.out.println(current);
		registerValueIterationStats();
    	return iterationStats;
	}

	protected void backup(){
		switch(params.getBackupMethod()){
		case PbParams.BACKUP_SYNC_FULL:
			current=syncBackup(fullBset);
			break;
		case PbParams.BACKUP_SYNC_NEWPOINTS:
			current=syncBackup(newBset);
			break;
		case PbParams.BACKUP_ASYNC_FULL:
			current=asyncBackup(fullBset);
			break;
		case PbParams.BACKUP_ASYNC_NEWPOINTS:
			current=asyncBackup(fullBset);
			break;
		}
	}

	
	
	private ValueFunctionStd asyncBackup(PointSet newBset) {
		// TODO Auto-generated method stub
		return null;
	}

	protected ValueFunctionStd syncBackup(PointSet bset){
		ValueFunctionStd newv=new ValueFunctionStd(bmdp.nrStates());
		for (BeliefState bel:bset){
			AlphaVector alpha_max=null;
			double alpha_max_val=Double.NEGATIVE_INFINITY;
			for (int a=0;a<bmdp.nrActions();a++){
				AlphaVector alpha_sum=new AlphaVector(bmdp.nrStates(),a);
				for (int o=0;o<bmdp.nrObservations();o++){
					double max_val=Double.NEGATIVE_INFINITY;
					AlphaVector max_vect=null;
					for (int idx=0;idx<old.size();idx++){
						AlphaVector prev= old.getAlpha(idx);
						AlphaVector vect=bmdp.projection(prev, a, o);
						double val=vect.eval(bel);
						if (val>max_val){
							max_val=val;
							max_vect=vect;
						}
					}
					alpha_sum.add(max_vect);
				}
				alpha_sum.add(bmdp.getRewardValues(a));
				double alpha_val=alpha_sum.eval(bel);
				if (alpha_val>alpha_max_val){
					alpha_max_val=alpha_val;
					alpha_max=alpha_sum;
				}
			}
			newv.push(alpha_max);
		}
		return newv;
	}
	
	protected void expand() {
		newBset = new PointSet();
		if (fullBset == null) {
			fullBset = new PointSet();
			fullBset.add(bmdp.getInitialBelief());
			newBset.add(bmdp.getInitialBelief());
		}
		if (fullBset.size() >= params.getMaxTotalPoints())
			return;
		
		PointSet testBset = fullBset.copy();
		while (fullBset.size() < params.getMaxTotalPoints()
				|| newBset.size() < params.getMaxNewPoints()) {
			BeliefStateStd point = null;
			switch (params.getExpandMethod()) {
			case PbParams.EXPAND_GREEDY_ERROR_REDUCTION:
				point = collectGreedyErrorReduction((BeliefStateStd) testBset.remove(0));
				break;
			case PbParams.EXPAND_EXPLORATORY_ACTION:
				point = collectExploratoryAction((BeliefStateStd) testBset.remove(0));
				break;
			case PbParams.EXPAND_RANDOM_EXPLORE_STATIC:
			case PbParams.EXPAND_RANDOM_EXPLORE_DYNAMIC:
				point = collectRandomExplore((BeliefStateStd) testBset.remove(0));
				break;
			}
			if (point!=null){
				fullBset.add(point);
				newBset.add(point.copy());
			}
			if(testBset.size() == 0){
				if (params.getExpandMethod()==PbParams.EXPAND_RANDOM_EXPLORE_STATIC )
					testBset=fullBset.copy();
				else
					break;
			}
		}
	}

	private BeliefStateStd collectExploratoryAction(BeliefStateStd b) {
		double max_dist=Double.NEGATIVE_INFINITY;
		BeliefStateStd bnew=null;
		for (int a=0;a<bmdp.nrActions();a++){		
			int o = bmdp.getRandomObservation(b, a);
			BeliefStateStd ba = (BeliefStateStd) bmdp.sampleNextBelief(b, a, o);
			double dist=distance(ba,fullBset);
			if (dist > max_dist){
				max_dist=dist;
				bnew=ba;
			}
		}
		if (max_dist==0.0) 
			bnew=null;
		return(bnew);
	}

	private BeliefStateStd collectGreedyErrorReduction(BeliefStateStd remove) {
		// TODO Auto-generated method stub
		return null;
	}

	private BeliefStateStd collectRandomExplore(BeliefStateStd b) {
		BeliefStateStd bprime;
		int a = bmdp.getRandomAction();
		int o = bmdp.getRandomObservation(b, a);
		bprime = (BeliefStateStd) bmdp.sampleNextBelief(b, a, o);
		return bprime;
	}

	private double distance(BeliefStateStd ba,PointSet newBset) {
		double min_val=Double.POSITIVE_INFINITY;
	    for (BeliefState bprime:newBset){
	    	CustomVector vect=bprime.getPoint().copy();
	    	vect.add(-1.0,ba.getPoint());
	    	double val=vect.norm(1.0);
	    	if (val < min_val)
	    		min_val=val;
	    }
		return min_val;
	}
	
	
}
