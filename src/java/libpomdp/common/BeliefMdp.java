/** BeliefMdp class. 
This interface represents a Belief MDP. 
At this point, this assumes that a belief MDP
always comes from a POMDP, but this might change in
the future if a MDP interface is defined.
@author Mauricio Araya
 */

package libpomdp.common;

public interface BeliefMdp extends Pomdp {

    /**
     * Get the tau matrix. The tau matrix is the matrix that represents the
     * projection over the next value, given an action and an observation.
     * Formally is the matrix Matrix diag(O^{o,a})*T^{o,a}.
     * 
     * @param a
     *            the action
     * @param o
     *            the observation
     */
    public CustomMatrix getTau(int a, int o);

	public AlphaVector project(AlphaVector prev, int a, int o);


	
}
