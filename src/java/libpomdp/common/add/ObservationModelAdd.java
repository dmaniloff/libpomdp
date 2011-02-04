package libpomdp.common.add;

import java.util.Vector;

import libpomdp.common.AlphaVector;
import libpomdp.common.CustomMatrix;
import libpomdp.common.ObservationModel;
import libpomdp.common.add.symbolic.DD;

public class ObservationModelAdd extends ObservationModel {
	
	protected DD model[][];
	protected AddConfiguration conf;
	
	public ObservationModelAdd(Vector<DD[]> actObserve,AddConfiguration conf) {
		this.conf=conf;
		model=new DD[conf.nrAct][];
		for (int a = 0; a < conf.nrAct; a++) {
		    // ^ this is cptid !!!!
		    model[a] = actObserve.get(a);
		}
	}

	@Override
	public CustomMatrix getMatrix(int a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlphaVector project(AlphaVector alpha, int a) {
		// TODO Auto-generated method stub
		return null;
	}

}
