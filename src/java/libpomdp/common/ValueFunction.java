
package libpomdp.common;


/** Representation of a set of alpha vectors and their associated actions for direct control (if possible)
    @author Diego Maniloff 
    @author Mauricio Araya*/
public interface ValueFunction {
    
    public int[] getActions();

    public int size();
    
    public AlphaVector getAlpha(int idx);
    public CustomVector getAlphaValues(int idx);
    
    public double value(BeliefState b);

    public void sort();

} // valueFunction
