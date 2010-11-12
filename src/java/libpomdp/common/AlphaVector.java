package libpomdp.common;


public class AlphaVector implements Comparable<AlphaVector> {
	
	protected CustomVector v;
	protected int a;
	
	public AlphaVector(CustomVector v,int a){
		this.v=v;
		this.a=a;
	}
	
	public AlphaVector(int dim) {
		this(new CustomVector(dim),-1);
	}

	public AlphaVector(int dim, int a) {
		this(new CustomVector(dim),a);
	}

	
	public double eval(BeliefState bel) {
		return(v.dot(bel.getPoint()));
	}

	public int getAction() {
		return a;
	}

	public AlphaVector copy() {
		return(new AlphaVector(v.copy(),a));
	}

	public CustomVector getVectorCopy() {
		return(v.copy());
	}
	
	public int size(){
		return(v.size());
	}

	public int compareTo(AlphaVector testVec, double delta) {
		v.compare(testVec.v);
		return 0;
	}

	public CustomVector getVectorRef() {
		return(v);
	}

	public int compareTo(AlphaVector arg0) {
		return v.compareTo(arg0.v);
	}

	public void setValues(CustomVector v) {
		this.v=v;
	}

	public void setAction(int a){
		this.a=a;
	}
	
	public void set(AlphaVector res) {
		setValues(res.v);
		setAction(res.a);
	}

	public void setValue(int s, double colmax) {
		v.set(s, colmax);
	}

	public void add(AlphaVector myAlpha) {
		add(myAlpha.v);	
	}

	public void add(CustomVector myVec) {
		v.add(myVec);
	}
	
}
