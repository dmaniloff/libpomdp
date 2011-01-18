package libpomdp.solve;

import java.util.ArrayList;

import libpomdp.common.Pomdp;

public abstract class IterationStats {

    protected Pomdp pomdp;

    public int register(long iTime) {
	iteration_time.add(new Long(iTime));
	iterations++;
	return (iterations);
    }

    public long init_time;
    public ArrayList<Long> iteration_time;
    public int iterations;

    public IterationStats(Pomdp pomdp) {
	this.pomdp = pomdp;
	iteration_time = new ArrayList<Long>();
	iterations = 0;
    }

    public String toString() {
	String retval = "ITERATION STATS\n";
	retval += "-----------------\n";
	retval += "iterations = " + iterations + "\n";
	retval += "init time = " + init_time + "\n";
	retval += "iteration time  = ";
	long sum = 0;
	for (Long l : iteration_time) {
	    sum += l.longValue();
	}
	retval += sum + "\n";
	return retval;

    }

}