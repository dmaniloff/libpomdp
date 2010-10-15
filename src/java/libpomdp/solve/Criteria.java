package libpomdp.solve;

public abstract class Criteria {
	public static final int CC_MAXEUCLID=1;
	public abstract boolean check(Iteration i);
	public abstract boolean valid(Iteration i);
}
