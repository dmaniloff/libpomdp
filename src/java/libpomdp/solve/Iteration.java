package libpomdp.solve;

import java.util.ArrayList;

import libpomdp.common.Pomdp;

public abstract class Iteration {

	protected Pomdp pomdp;
	protected IterationStats iterationStats;
	protected ArrayList<Criteria> stopCriterias; 

	public abstract IterationStats iterate();

	public IterationStats run(){
			while (!finished()){
				iterate();
			}
			return iterationStats;
	}

	public boolean finished() {
		for (Criteria sc:stopCriterias){
			if (sc.check(this))
				return true;
		}
		return false;
	}
	
	public void addStopCriteria(Criteria sc) {
		if (sc.valid(this))
			stopCriterias.add(sc);
	}
	
	public abstract IterationStats getStats();

	public abstract Pomdp getPomdp();

}