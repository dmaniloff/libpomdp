package libpomdp.common;

import libpomdp.common.std.AlphaVectorStd;
import libpomdp.common.std.PomdpStd;
import libpomdp.common.std.ValueFunctionStd;

public class ValueFunctionFactory {

	public static ValueFunction getLowerBoundPerAction(Pomdp pomdp) {
		ValueFunction vf = null;
		if (pomdp instanceof PomdpStd){
			vf=lowerBoundPerActionStd((PomdpStd)pomdp);
			}
		else{
			//vf=lowerBoundPerActionAdd((PomdpStd)pomdp);
			// TODO Implement homogene for ADDs
		}
		return vf;
	}

	public static ValueFunction getZeroPerActions(Pomdp pomdp) {
		ValueFunction vf = null;
		if (pomdp instanceof PomdpStd){
			vf=zeroPerActionStd((PomdpStd)pomdp);
			}
		else{
			//vf=zeroPerActionAdd((PomdpStd)pomdp);
			// TODO Implement Zerp for ADDs
		}
		return vf;
	}
	


	private static ValueFunction lowerBoundPerActionStd(PomdpStd pomdp){
		ValueFunctionStd vf = null;
		vf=new ValueFunctionStd();
		for (int a = 0; a < pomdp.nrActions(); a++) {
			double factor = 1.0 / (1.0 - pomdp.getGamma());
			double val = pomdp.getRewardMin();
			val *= factor;
		    vf.newAlpha(CustomVector.getHomogene(pomdp.nrStates(), val), a);
		}
		return(vf);
	}
	
	private static ValueFunction zeroPerActionStd(PomdpStd pomdp) {
		ValueFunctionStd vf = new ValueFunctionStd();
		for (int a = 0; a < pomdp.nrActions(); a++)
		    vf.push(new AlphaVectorStd(pomdp.nrStates(), a));
		return vf;
	}

	public static ValueFunction getEmpty(Pomdp pomdp) {
		ValueFunction vf = null;
		if (pomdp instanceof PomdpStd){
			vf=new ValueFunctionStd();
			}
		else{
			//vf=new ValueFunctionAdd();
			// TODO Implement Empty for ADDs
		}
		return vf;
	}

	public static ValueFunction getRewardValueFunction(Pomdp pomdp, int a) {
		ValueFunction vf = null;
		if (pomdp instanceof PomdpStd){
			vf=((PomdpStd)pomdp).getRewardValueFunction(a);
			}
		else{
			//vf=new ValueFunctionAdd();
			// TODO Implement Empty for ADDs
		}
		return vf;
	}

}
