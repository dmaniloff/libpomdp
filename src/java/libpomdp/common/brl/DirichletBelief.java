package libpomdp.common.brl;

import libpomdp.common.CustomVector;

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
	
	public void bayesUpdate(int x){
		params[x]++;
	}
	
	public CustomVector expectedValue(int x){
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
	
	
	
}
