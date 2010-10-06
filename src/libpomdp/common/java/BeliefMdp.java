package libpomdp.common.java;

import no.uib.cipr.matrix.Matrix;

public interface BeliefMdp extends Pomdp {
	
	public Pomdp getPomdp();
    // Matrix Pr(s,s'|a,o) (diag(O)*T)
    public Matrix getTau(int a,int o);
}
