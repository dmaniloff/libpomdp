package libpomdp.solve.online;

import libpomdp.common.Pomdp;
import libpomdp.common.ValueFunction;

public abstract class AbstractAndOrTree {

    private Pomdp problem;
    private ValueFunction offlineLower;
    private ValueFunction offlineUpper;
    private OrNode root;

    public AbstractAndOrTree(Pomdp problem,
			     OrNode r,
			     ValueFunction l,
			     ValueFunction u) {
        this.problem = problem;
        this.offlineLower = l;
        this.offlineUpper = u;
        this.root = r;        
    }
    
    /**
     * expand(HeuristicSearchOrNode en): one-step expansion of |A||O|
     * HeuristicSearchOrNodes
     * @param <T>
     */
    //public abstract <T extends OrNode> void expand(T en); 

    /**
     * updateAncestors(HeuristicSearchOrNode n): update the ancestors of a given
     * HeuristicSearchOrNode
     * @param <T>
     */
    //public abstract <T extends OrNode> void updateAncestors(T n); 

    public Pomdp getProblem() {
        return problem;
    }
    
    public OrNode getRoot() {
        return root;
    }

    public void setRoot(OrNode newRoot) {
        root = newRoot;
    }
    
    public void moveTree(OrNode newroot) {
        setRoot(newroot);
        root.disconnect();
    }

    public ValueFunction getLB() {
        return offlineLower;
    }
    
    public void setLB(ValueFunction lb) {
        offlineLower = lb;
    }

    public ValueFunction getUB() {
        return offlineUpper;
    }

    public void setUB(ValueFunction ub) {
        offlineUpper = ub;
    }
}