package libpomdp.solve.java;

public abstract class StopCriteria {
	public abstract boolean check(Iteration i);
	public abstract boolean valid(Iteration i);
}
