package libpomdp.common.std;

import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;
import libpomdp.common.TransitionModel;


public class TransitionModelStd extends TransitionModel {
	
	protected int states;
	protected int actions;
	protected CustomMatrix model[];
	

	public TransitionModelStd(CustomMatrix[] t) {
		// TODO Auto-generated constructor stub
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


	public CustomVector project(CustomVector customVector,int a) {
		return model[a].mult(customVector);
	}


	public CustomMatrix getMatrix(int a) {
		return model[a];
	}



}
