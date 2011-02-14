package libpomdp.solve;

import java.util.ArrayList;

public abstract class Iteration {

    protected IterationStats iterationStats;
    protected ArrayList<Criteria> stopCriterias;
    protected long tempTime;

    public IterationStats getStats() {
    	return (iterationStats);
    }

    public boolean finished() {
	for (Criteria sc : stopCriterias) {
	    if (sc.check(this))
		return true;
	}
	return false;
    }

    public void addStopCriteria(Criteria sc) {
	if (sc.valid(this))
	    stopCriterias.add(sc);
    }

    protected void startTimer() {
	tempTime = System.currentTimeMillis();
    }

    protected void initIteration() {
	stopCriterias = new ArrayList<Criteria>();
	if (iterationStats==null)
		iterationStats = new IterationStats();
    }

    protected void registerInitTime() {
	iterationStats.init_time = System.currentTimeMillis() - tempTime;
    }

    protected void registerIterationTime() {
	iterationStats.register(System.currentTimeMillis() - tempTime);
    }

}