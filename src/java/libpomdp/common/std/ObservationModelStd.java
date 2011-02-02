package libpomdp.common.std;

import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;
import libpomdp.common.ObservationModel;

public class ObservationModelStd extends ObservationModel {

	protected int states;
	protected int actions;
	protected int observations;
	protected CustomMatrix model[];
	
	public ObservationModelStd(CustomMatrix[] o) {
		// TODO Auto-generated constructor stub
	}

	public CustomVector getRow(int o, int a) {
		return model[a].getRow(o);
	}

	public CustomVector project(CustomVector customVector,int a) {
		return model[a].mult(customVector);
	}

	public double getValue(int o, int s, int a) {
		return model[a].get(s, o);
	}

}
