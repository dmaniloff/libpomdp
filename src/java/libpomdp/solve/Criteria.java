package libpomdp.solve;

public abstract class Criteria {
    public static final int CC_MAXEUCLID = 1;
    public static final int CC_MAXDIST = 2;

    public abstract boolean check(Iteration i);

    public abstract boolean valid(Iteration i);
}
