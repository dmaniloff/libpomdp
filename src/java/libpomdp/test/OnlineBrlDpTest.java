package libpomdp.test;

import libpomdp.common.TransitionModel;
import libpomdp.common.brl.BrlBelief;
import libpomdp.common.brl.BrlReward;
import libpomdp.common.brl.StateDepRlReward;
import libpomdp.common.std.TransitionModelStd;
import libpomdp.simulation.SimRL;
import libpomdp.solve.online.OnlineIteration;
import libpomdp.solve.online.brl.Birdp;
import libpomdp.solve.online.brl.BrlDpExecuter;

public class OnlineBrlDpTest {

    /**
     * @param args
     * @throws Exception
     */
	public static void main(String[] args) {
		int horizon = 25;
		int states = 2;
		int actions = 2;
		double gamma=1.0;
		double fac = 0;
		//Utils.setSeed(0);
		BrlBelief.setBeliefSpace(states, actions, horizon);
		BrlBelief prior = BrlBelief.getUniform();
		System.out.println(prior);
		BrlReward reward = StateDepRlReward.getRandom(states, actions, -1, 10);
		//TransitionModel model=TransitionModelStd.getRandom(states,actions);
		//TransitionModel model=TransitionModelStd.getUniform(states,actions);
		TransitionModel model=TransitionModelStd.getDeterministicRandom(states,actions);
		//System.out.println(reward);
		//System.out.println(model);
		// prior.bayesUpdate(1, 0, 1);
		// prior.bayesUpdate(1, 0, 1);
		// prior.bayesUpdate(1, 0, 1);
		// prior.bayesUpdate(1, 0, 1);
		// prior.bayesUpdate(1, 0, 1);
		SimRL simulator=new SimRL(states, actions, model, reward);
		OnlineIteration agent; 
		agent = new BrlDpExecuter(reward, horizon, gamma, fac, prior, 0);
		System.out.println("Optimal Value = "+simulator.simulate(0, agent, horizon));
		fac=0.3;
		agent=null;
		System.gc();
		agent = new Birdp(reward, horizon, gamma, fac, prior);
		System.out.println("Bird Value = "+simulator.simulate(0, agent, horizon));
		
	}

}
