/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: onlineRockSampleSimulatorAdd.java
 * Description: script that glues all code together to simulate
 *              an online agent in the RockSample environment
 *              ADDs and SPUDD factored descriptions are used
 *              see README reference [7] for a description of this problem
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

// imports
import java.util.*;
import org.math.array.*;

public class onlineRockSampleSimulatorAdd {

    public static void main(String[] args) {       

	// load problem
	// pomdpAdd factoredProb = new pomdpAdd('../../general/problems/tiger/tiger.95.SPUDD');
	// pomdpAdd factoredProb = new pomdpAdd('../../general/problems/coffee/coffee.90.SPUDD');
	// pomdpAdd factoredProb = new pomdpAdd('../../general/problems/rocksample/RockSample_2_1/RockSample_2_1.SPUDD');
	pomdpAdd factoredProb = new pomdpAdd  ("../../general/problems/rocksample/RockSample_7_8/RockSample_7_8.SPUDD");

	// rocksample parameters to call graphing routine
	final int GRID_SIZE           = 7;
	final int ROCK_POSITIONS[][]  = {{2, 0},{0, 1},{3, 1},{6, 3},{2, 4},{3, 4},{5, 5},{1, 6}};
	rocksampleGraph drawer        = new rocksampleGraph();

	// compute offline bounds
	long start = System.currentTimeMillis();
	blindAdd blindCalc            = new blindAdd();
	valueFunctionAdd lBound       = blindCalc.getBlindAdd(factoredProb);
	System.out.println("blindcalc took " + (System.currentTimeMillis() - start));
	// just bounds for profiling
	System.exit(0);
	qmdpAdd qmdpCalc              = new qmdpAdd();
	valueFunctionAdd uBound       = qmdpCalc.getqmdpAdd(factoredProb);

	// create heuristic search AND-OR tree
	aems2 aems2H                  = new aems2(factoredProb);
	AndOrTree aoTree              = new AndOrTree(factoredProb, aems2H, lBound, uBound);

	// initialize
	aoTree.init(factoredProb.getInit());
	orNode rootNode               = aoTree.getRoot();

	// play the pomdp
	final double MAXPLANNINGTIME  = 1000;
	final int    MAXEPISODELENGTH = 100;


	// stats counters
	ArrayList<Double> Rew         = new ArrayList<Double>();
	double cumRew                 = 0.0;
	ArrayList<Integer> nrExpands  = new ArrayList<Integer>();
	ArrayList<Double>  planTime   = new ArrayList<Double>();

	// decls
	int [][] factoredS, factoredS1, factoredO;
	DD[] restrictedT, restrictedO;
	int iter, expc, a, o;
	double tic, avgPlanTime = 0.0;

	// initial belief and state
	factoredS = OP.sampleMultinomial(((belStateAdd)factoredProb.getInit()).bAdd, factoredProb.staIds);

	// episode loop
	for(iter=1; iter<MAXEPISODELENGTH; iter++) {
    
	    System.out.println("******************** INSTANCE: " + iter + " ********************");
	    System.out.println("Current world state is:         " + factoredProb.printS(factoredS));
	    // draw the current state
	    drawer.drawState(GRID_SIZE, ROCK_POSITIONS, factoredS);
	    System.out.println("Current belief agree prob:      " + OP.eval(((belStateAdd)rootNode.belief).bAdd, factoredS));
	    System.out.println("Current |T| is:                 " + rootNode.subTreeSize);
    
	    // reset expand counter
	    expc = 0;

	    // start stopwatch
	    tic = System.currentTimeMillis();

	    // planning loop
	    while (System.currentTimeMillis() - tic < MAXPLANNINGTIME) {
		// expand best node
		aoTree.expand(rootNode.bStar);
		// update its ancestors
		aoTree.updateAncestors(rootNode.bStar);
		// expand counter
		expc++;
		// check whether we found an e-optimal action - there is another criteria
		// here too!!
		if (Math.abs(rootNode.u - rootNode.l) < 1e-3) {
		    System.out.println("Found e-optimal action, aborting expansions");
		    break;
		}
	    } // planning loop

	    // save plannig time
	    planTime.add(System.currentTimeMillis() - tic);
    
	    // obtain the best action for the root
	    // remember that a's and o's in matlab should start from 1
	    // and so the OP operations expect this
	    a = aoTree.currentBestAction();
	    //a = a + 1;
    
	    // execute it and receive new o
	    restrictedT = OP.restrictN(factoredProb.T[a], factoredS); 
	    factoredS1  = OP.sampleMultinomial(restrictedT, factoredProb.staIdsPr);
	    //System.arraycopy(factoredProb.T[a], 0, adds, 1, factoredProb.T[a].length);
	    restrictedO = OP.restrictN(factoredProb.O[a], IntegerArray.mergeRows(IntegerArray.merge(factoredS[0], factoredS1[0]),
										 IntegerArray.merge(factoredS[1], factoredS1[1])));
	    factoredO   = OP.sampleMultinomial(restrictedO, factoredProb.obsIdsPr);

    
	    // save stats
	    Rew.add(OP.eval(factoredProb.R[a], factoredS));
	    cumRew += Math.pow(factoredProb.getGamma(),iter - 1) * Rew.get(Rew.size()-1);
	    nrExpands.add(expc);
    
	    // output some stats
	    System.out.println("Expansion finished, # expands:" + expc);
	    // this will count an extra |A||O| nodes given the expansion of the root
	    System.out.println("|T|:                          " + rootNode.subTreeSize);
	    System.out.println("Outputting action:            " + factoredProb.getactStr(a));
	    System.out.println("Perceived observation:        " + factoredProb.printO(factoredO));
	    System.out.println("Received reward:              " + Rew.get(Rew.size()-1));
	    System.out.println("Cumulative reward:            " + cumRew);
    
	    // check whether this episode ended (terminal state)
	    if(factoredS1[1][0] == GRID_SIZE + 1) {
		System.out.println("***************** Episode ended at instance: " + iter);
		System.out.println("Final stats");
		for(Double pt : planTime) avgPlanTime += pt;
		System.out.println("Average planning time:    " + avgPlanTime / iter);
		break;
	    }
    
	    // move the tree's root node
	    o = IntegerArray.product(factoredO[1]);
	    aoTree.moveTree(rootNode.children[a].children[o-1]); // check this!
	    // update reference to rootNode
	    rootNode = aoTree.getRoot();
    
	    System.out.println("Tree moved, reused |T|:         " + rootNode.subTreeSize);
    
	    // iterate	    
	    // factoredS = factoredS1;
	    System.arraycopy(factoredS1, 0, factoredS, 0, factoredS1.length);
	    // roll back var ids on factoredS
	    factoredS = Config.primeVars(factoredS, -factoredProb.getnrTotV());

	} // episode loop

    } // main

} // onlineRockSampleSimulatorAdd