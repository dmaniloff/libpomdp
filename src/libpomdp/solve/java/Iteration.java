package libpomdp.solve.java;

import java.util.ArrayList;

import libpomdp.common.java.Pomdp;

public abstract class Iteration {

	protected Pomdp pomdp;
	protected IterationStats iterationStats;
	protected ArrayList<StopCriteria> stopCriterias; 

	public abstract IterationStats iterate();

	public IterationStats run(){
			while (!finished()){
				iterate();
			}
			return iterationStats;
	}

	public boolean finished() {
		for (StopCriteria sc:stopCriterias){
			if (sc.check(this))
				return true;
		}
		return false;
	}
	
	public void addStopCriteria(StopCriteria sc) {
		if (sc.valid(this))
			stopCriterias.add(sc);
	}
	
	public abstract IterationStats getStats();

	public abstract Pomdp getPomdp();

}