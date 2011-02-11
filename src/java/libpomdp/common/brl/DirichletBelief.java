package libpomdp.common.brl;

import libpomdp.common.CustomVector;
import libpomdp.common.Utils;

public class DirichletBelief {
	protected long[] params;
	public DirichletBelief(int states){
		params=new long[states];
		for (int i=0;i<states;i++)
			params[i]=1;
	}
	
	public DirichletBelief(long params[]){
		this.params=params.clone();
	}
	
	public DirichletBelief(DirichletBelief dirichletBelief) {
		this(dirichletBelief.params);
	}

	public void bayesUpdate(int x){
		params[x]++;
	}
	
	public CustomVector expectedValue(){
		CustomVector retval=new CustomVector(params);
		retval.normalize();
		return retval;
	}
	
	public long getParameterNorm(){
		long norm=0;
		for (int i=0;i<params.length;i++)
			norm+=params[i];
		return norm;
	}
	
	public long getParameter(int x){
		return(params[x]);
	}

	public long max() {
		return(Utils.max(params));
	}
	
	public String toString(){
		String retval = "";
		int states = params.length;
		for (int i=0;i<states;i++)
			retval+=params[i]+" ";
		return retval;
	}

	public boolean compare(DirichletBelief dirichlet) {
		for (int i=0;i<params.length;i++)
			if (params[i]!=dirichlet.params[i])
				return false;
		return true;
	}
	
	
}
