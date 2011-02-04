package libpomdp.common.std;

import libpomdp.common.AlphaVector;
import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;
import libpomdp.common.ObservationModel;

public class ObservationModelStd extends ObservationModel {

	protected int states;
	protected int actions;
	protected int observations;
	protected CustomMatrix model[];
	
	public ObservationModelStd(CustomMatrix[] o) {
		actions=o.length;
		states=o[0].numColumns();
		observations=o[0].numRows();
		model=o;
	}

	public CustomVector getRow(int o, int a) {
		return model[a].getRow(o);
	}

	public CustomVector project(CustomVector customVector,CustomMatrix matrix) {
		return matrix.mult(customVector);
	}


	public AlphaVectorStd project(AlphaVector alpha, int a) {
		AlphaVectorStd vec = (AlphaVectorStd)alpha;
		vec = AlphaVectorStd.transform(project((CustomVector)vec,model[a]));
		vec.setAction(a);
		return(vec);
	}
	
	public double getValue(int o, int s, int a) {
		return model[a].get(s, o);
	}
	
	public CustomMatrix getMatrix(int a){
		return(model[a]);
	}

}
