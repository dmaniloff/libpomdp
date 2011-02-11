package libpomdp.common.brl;

public class TransitionModelBelief {
	protected int states;
	protected int actions;
	protected DirichletBelief bel[][];
	
	
	
	
	public TransitionModelBelief(int n_x,int n_a){
		states=n_x;
		actions=n_a;
		bel=new DirichletBelief[states][actions];
		for (int a=0;a<actions;a++){
			for (int x=0;x<states;x++){
				bel[x][a]=new DirichletBelief(states);
			}
		}
	//	level=0;
	}
	
	public TransitionModelBelief(DirichletBelief[][] newbel) {
		setBel(newbel);
//		calculateLevel();
	}
	
	private void setBel(DirichletBelief[][] newbel){
		states=newbel.length;
		actions=newbel[0].length;
		bel=new DirichletBelief[states][actions];
		for (int a=0;a<actions;a++){
			for (int x=0;x<states;x++){
				bel[x][a]=new DirichletBelief(newbel[x][a]);
			}
		}
	}

	/*
	private void calculateLevel(){
		level=0;
		for (int a=0;a<actions;a++){
			for (int x=0;x<states;x++){
				level+=bel[x][a].getParameterNorm();
			}
		}
		level-=states;
	}*/
	
	public TransitionModelBelief(TransitionModelBelief tmod) {
		setBel(tmod.bel);
//		level=tmod.level();
	}
	
	/*public long level() {
		return level;
	}*/

	public void bayesUpdate(int x,int a,int x_next){
		bel[x][a].bayesUpdate(x_next);
//		level++;
	}
		public int getNrStates() {
		return states;
	}
	
	public int getNrActions() {
		return actions;
	}

	public TransitionModelBelief copy() {
		return new TransitionModelBelief(bel);
	}

	public double prob(int x, int a, int s) {
		return bel[x][a].expectedValue().get(s);
	}
	
	public String toString(){
		String retval="";
		for (int a=0;a<actions;a++){
			for (int x=0;x<states;x++){
				retval+="("+x+","+a+") "+bel[x][a].toString()+"\n";
			}
		}
		return retval;
	}

	public DirichletBelief getDirichlet(int s, int a) {
		return bel[s][a];
	}
	
	public boolean compare(TransitionModelBelief other){
		for (int a=0;a<actions;a++){
			for (int x=0;x<states;x++){
				if (!bel[x][a].compare(other.getDirichlet(x, a)))
					return false;
			}
		}
		return true;
	}
	
}
