package libpomdp.common;


public interface BeliefMdp extends Pomdp {
	
	public Pomdp getPomdp();
    // Matrix Pr(s,s'|a,o) (diag(O)*T)
    public CustomMatrix getTau(int a,int o);
}
