package libpomdp.common.std;

import libpomdp.common.AlphaVector;
import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;
import libpomdp.common.TransitionModel;


public class TransitionModelStd extends TransitionModel {
	
	protected int states;
	protected int actions;
	protected CustomMatrix model[];

	public TransitionModelStd(CustomMatrix[] t) {
		actions=t.length;
		states=t[0].numColumns();
		model=t;
	}


	public void TranstionModel(int n_x,int n_a){
		states=n_x;
		actions=n_a;
		model=new CustomMatrix[actions];
		for (int a=0;a<states;a++){
			for (int x=0;x<states;x++){
				model[a]=CustomMatrix.getUniform(states, states);
			}			
		}
	}


	public CustomVector project(CustomVector vec,CustomMatrix matrix) {
		return matrix.mult(vec);
	}


	public CustomMatrix getMatrix(int a) {
		return model[a];
	}

	@Override
	public AlphaVectorStd project(AlphaVector alpha, int a) {
		AlphaVectorStd vec = (AlphaVectorStd)alpha;
		vec = AlphaVectorStd.transform(project((CustomVector)vec,model[a]));
		vec.setAction(a);
		return(vec);
	}

}
