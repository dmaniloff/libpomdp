package libpomdp.common.std;

import libpomdp.common.AlphaVector;
import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;
import libpomdp.common.TransitionModel;
import libpomdp.common.Utils;


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


	@Override
	public int sampleNextState(int state, int action) {
		CustomVector vec=model[action].getColumn(state);
		return vec.sample();
	}


	public static TransitionModel getRandom(int states, int actions) {
		CustomMatrix[] model=new CustomMatrix[actions];
		for (int a=0;a<actions;a++){
			model[a]=new CustomMatrix(states,states);
			for (int s=0;s<states;s++)
				model[a].setColumn(s, CustomVector.getRandomUnitary(states));
		}
		return new TransitionModelStd(model);
	}


	public static TransitionModel getUniform(int states, int actions) {
		CustomMatrix[] model=new CustomMatrix[actions];
		for (int a=0;a<actions;a++){
			model[a]=CustomMatrix.getUniform(states, states);
		}
		return new TransitionModelStd(model);
	}


	public static TransitionModel getDeterministicRandom(int states,
			int actions) {
		CustomMatrix[] model=new CustomMatrix[actions];
		for (int a=0;a<actions;a++){
			model[a]=new CustomMatrix(states,states);
			for (int s=0;s<states;s++){
				CustomVector vec = new CustomVector(states);
				vec.set(Utils.gen.nextInt(states),1.0);
				model[a].setColumn(s,vec);
			}
		}
		return new TransitionModelStd(model);
	}

}
