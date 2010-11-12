package libpomdp.solve;

import java.util.ArrayList;

import libpomdp.common.Pomdp;

public abstract class Iteration {

	protected IterationStats iterationStats;
	protected ArrayList<Criteria> stopCriterias; 
	protected long tempTime;
	
	public abstract IterationStats iterate();

	public IterationStats run(){
			while (!finished()){
				System.out.println("== Iteration "+iterationStats.iterations+" ==");
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
	
	protected void startTimer(){
		tempTime = System.currentTimeMillis();
	}
	
	protected void initIteration(){
		stopCriterias= new ArrayList<Criteria>();
	}
	
	protected void registerInitTime(){
		iterationStats.init_time = System.currentTimeMillis() - tempTime;
	}
	
	protected void registerIterationTime(){
		iterationStats.register(System.currentTimeMillis() - tempTime); 
	}

	
	public abstract IterationStats getStats();

	public abstract Pomdp getPomdp();

}