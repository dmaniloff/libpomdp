package libpomdp.common.add;

import java.util.Vector;

import libpomdp.common.AlphaVector;
import libpomdp.common.CustomMatrix;
import libpomdp.common.TransitionModel;
import libpomdp.common.add.symbolic.DD;

public class TransitionModelAdd extends TransitionModel {
	
	protected AddConfiguration conf;
	protected DD model[][];
	
	public TransitionModelAdd(Vector<DD[]> actTransitions,AddConfiguration conf){
		this.conf=conf;
		model=new DD[conf.nrAct][];
		for (int a = 0; a < conf.nrAct; a++) {
		    // ^ this is cptid !!!!
		    model[a] = actTransitions.get(a);
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
