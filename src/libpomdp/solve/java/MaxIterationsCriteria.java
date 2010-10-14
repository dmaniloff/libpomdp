package libpomdp.solve.java;

public class MaxIterationsCriteria extends Criteria {

	int max_iter;
	
	public MaxIterationsCriteria(int maxIter){
		this.max_iter=maxIter;
	}
	
	@Override
	public boolean check(Iteration i) {
		if (i.getStats().iterations < max_iter)
			return false;
		return true;
	}

	@Override
	public boolean valid(Iteration vi) {
		return true;
	}

}
