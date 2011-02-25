package libpomdp.common.brl;

import libpomdp.common.CustomVector;
import libpomdp.common.IntegerVector;
import libpomdp.common.Utils;


public class BrlBelief {
	IntegerVector vector;
	IntegerVector marginals;
	
	// Static Block
	protected static long fig_table[][];
	protected static int states = -1;
	protected static int actions = -1;
	protected static int horizon = -1;
	private static int vsize;
	public static void checkSpace() {
		if (horizon == -1 || states == -1 || actions == -1)
			throw new IllegalArgumentException("You must setup the belief space before creating beliefs");
	}
	public static void setBeliefSpace(int sta,int act,int hor){
		states=sta;
		actions=act;
		horizon=hor;
		System.out.print("Figurate Table...");
		System.out.flush();
		vsize=sta*sta*act;
		fig_table=new long[vsize][horizon+1];
		for (int h=1;h<horizon+1;h++){
			fig_table[0][h]=1;
			for (int v=1;v<vsize;v++){
				fig_table[v][h]=(fig_table[v-1][h]*(h+v-1))/v;
			}
		}
		System.out.println("[Done]");
	}
	
	public BrlBelief(){
		checkSpace();	
		vector = new IntegerVector(states*actions*states);
		marginals = new IntegerVector(states*actions);
	}
	
	// Constructors
	public BrlBelief(BrlBelief parent){
		checkSpace();
		vector= parent.vector.copy();
		marginals= parent.marginals.copy();
	}
	
	private int encode(int x,int a, int xp){
		return x*actions*states + a*states + xp;
	}
	
	private int encode(int x, int a) {
		return x*actions + a;
	}
	
	public int get(int x,int a, int xp){
		return vector.get(encode(x,a,xp));
	}
	
	public void set(int x,int a, int xp,int value){
		int idx=encode(x,a,xp);
		int save=vector.get(idx);
		vector.set(idx,value);
		marginals.add(encode(x,a),value-save);
	}
	
	public int getMarginal(int x,int a){
		return marginals.get(x*actions + a);
	}
	
	public void bayesUpdate(int x,int a,int xp){
		vector.add(encode(x,a,xp),1);
		marginals.add(encode(x,a),1);
	}
	
	public static int states() {
		return states;
	}
	
	public static int actions() {
		return actions;
	}

	public BrlBelief copy() {
		return new BrlBelief(this);
	}

	public double prob(int x, int a, int xp) {
		double val=vector.get(encode(x,a,xp));
		double tot=marginals.get(encode(x,a));
		if (tot==0 || val==0){
			Utils.error(toString());
		}
		return val/tot;
	}
	
	public String toString(){
		String retval="(";
		retval+=vector;
		/*for (int a=0;a<actions;a++){
			for (int x=0;x<states;x++){
				retval+=vector.toString(encode(x,a,0),encode(x,a,states-1))+" ";
			}
		}
		*/
		retval+=")";
		return retval;
	}

	@Override
	public int hashCode() {
		long result = 0;
		int index[]=vector.getIndex();
		int data[]=vector.getData();
		int level=marginals.norm1(); // Maybe save and update a level variable?
		for (int i=0;i<vector.getUsed();i++){
			for (int j=1;j<=data[i];j++){
				int r=vsize - index[i] - 2;
				int n=level + 2 - j;
				if (r<0) continue;
				result+=fig_table[r][n];
				//System.out.print(" +f("+r+","+n+")="+fig_table[r][n]);
			}
			level-=data[i];
		}
		//System.out.print(" = "+result+"\n");
		return (int) (result + Integer.MIN_VALUE);
	}

	/*private long getHashBase(int i) {
		return i*horizon;	
	}*/
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BrlBelief other = (BrlBelief) obj;
		if (other.marginals.norm1() != other.marginals.norm1())
			return false;
		if (hashCode() == other.hashCode())
				return true;
		return false;
	}
	public void add(BrlBelief other) {
		vector.add(other.vector);
		//System.out.println(marginals);
		//System.out.println(other.marginals);
		marginals.add(other.marginals);
		
	}
	
	//TODO: can this be optimized?
	public CustomVector expected(int x, int a) {
		CustomVector retval=new CustomVector(states);
		for (int xp=0;xp<states;xp++){
			retval.set(xp,this.prob(x, a, xp));
		}
		retval.compact();
		return retval;
	}
	
	public static BrlBelief getUniform() {
		BrlBelief retval = new BrlBelief();
		for (int x=0;x<states;x++){
			for (int a=0;a<actions;a++){
				for (int xp=0;xp<states;xp++){
					//System.out.println(x+" "+a+" "+xp);
					retval.set(x, a, xp,1);
				}
			}
		}
		return retval;
	}
	
	/*public IntegerVector getSubVector(int x, int a) {
		return vector.getSubVector(encode(x,a,0),encode(x,a,states-1));
		
	}*/
	
}
