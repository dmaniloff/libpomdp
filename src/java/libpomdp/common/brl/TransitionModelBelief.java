package libpomdp.common.brl;

public class TransitionModelBelief {
	protected int states;
	protected int actions;
	protected DirichletBelief bel[][];
	protected int infoCriteria;
	
	final public static int IC_ENTROPY = 1;
	final public static int IC_LOGMAX = 2;
	
	public TransitionModelBelief(int n_x,int n_a){
		infoCriteria=IC_LOGMAX;
		states=n_x;
		actions=n_a;
		bel=new DirichletBelief[states][actions];
		for (int a=0;a<states;a++){
			for (int x=0;x<states;x++){
				bel[x][a]=new DirichletBelief(states);
			}
		}
	}
	
	public void setInfoCriteria(int info){
		infoCriteria=info;
	}
	
	public void bayesUpdate(int x,int a,int x_next){
		bel[x][a].bayesUpdate(x_next);
	}
	
	
	public double getDeltaInfo(int x, int a, int x_next){
		DirichletBelief select=bel[x][a];
		double th=select.getParameter(x_next);
		double tot=select.getParameterNorm();
		double retval;
		switch (infoCriteria){
		case IC_ENTROPY:
			retval=(states-1.0)/tot - Math.log(th/tot);
			for (int i=(int)th;i<tot;i++){
				retval-=1.0/(double)i;
			}
			break;
		case IC_LOGMAX:
		default: //IC_LOGMAX
			retval=Math.log(tot) + (th-1)*Math.log(th/(th-1));
			break;
		}
		return retval;
	}
	
}
