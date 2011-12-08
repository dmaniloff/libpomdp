package libpomdp.simulator;

import java.util.ArrayList;
import java.util.List;

import libpomdp.common.BeliefState;
import libpomdp.common.CustomVector;
import libpomdp.common.Utils;
import libpomdp.common.ValueFunction;
import libpomdp.common.add.BeliefStateAdd;
import libpomdp.common.add.BeliefStateFactoredAdd;
import libpomdp.common.add.PomdpAdd;
import libpomdp.common.add.ValueFunctionAdd;
import libpomdp.common.add.symbolic.Config;
import libpomdp.common.add.symbolic.DD;
import libpomdp.common.add.symbolic.OP;
import libpomdp.parser.FileParser;
import libpomdp.solve.hybrid.AndOrTreeUpdateAdd;
import libpomdp.solve.hybrid.DOSI;
import libpomdp.solve.hybrid.HybridValueIterationOrNode;
import libpomdp.solve.offline.bounds.BpviAdd;
import libpomdp.solve.offline.bounds.QmdpAdd;
import libpomdp.solve.online.AEMS2;

class HybridSimulatorAdd {

    public static void main(String args[]) {

	// declarations
	AndOrTreeUpdateAdd aoTree;
	HybridValueIterationOrNode rootNode;
	
	int expC = 0;
	int bakC = 0;
	int fndO = 0;
	double cumR = 0;

    // problem name
    String probFilename = "/Users/diego/Documents/workspace/libpomdp/data/problems/rocksample/RockSample_7_8.SPUDD";

	// load problem
	PomdpAdd factoredProb = (PomdpAdd)
        FileParser.loadPomdp(probFilename, FileParser.PARSE_SPUDD);

    // load bounds
	ValueFunction uBound = FileParser.loadUpperBound(probFilename,
                                                     FileParser.PARSE_SPUDD);
    ValueFunction lBound = FileParser.loadLowerBound(probFilename,
                                                     FileParser.PARSE_SPUDD);

	// create heuristics
	AEMS2 aems2h = new AEMS2(factoredProb);
	DOSI dosih   = new DOSI(factoredProb);

	// figure out all possible initial states of the pomdp
	List<Integer> states = new ArrayList<Integer>();
	int factoredS[][];
	
	for (int r = 1; r <= factoredProb.nrStates(); ++r) {
	    factoredS = new int [][] { factoredProb.getstaIds() ,
		    Utils.sdecode(r - 1, factoredProb.getnrStaV(), factoredProb.getstaArity()) };
	    if (OP.eval(((BeliefStateAdd)factoredProb.getInitialBeliefState()).bAdd,
		    factoredS) > 0) {
	        states.add(new Integer(r - 1));
	    }
	}

	
	//  play the pomdp
	//logFilename = sprintf("simulation-logs/rocksample/RS78-HYVI-regions-ADD-%s.log", datestr(now, "yyyy-mmm-dd-HHMMSS"));
	//diary(logFilename);

	// rocksample parameters for the grapher
	int GRID_SIZE                = 7;
	int ROCK_POSITIONS[][]       = new int[][] {{2, 0}, {0, 1}, {3, 1}, {6, 3}, {2, 4}, {3, 4}, {5, 5}, {1, 6}}; 
	int SARTING_POS[]            = new int[]    {0, 3};

	//drawer            = rocksampleGraph;
	int NUM_ROCKS                = ROCK_POSITIONS.length;

	// general parameters
	int EXPANSION_RATE           = 79; // calculated from the online simulator, avg 
	int AVG_EP_LIFETIME          = 26; // calculated from the online simulator, avg
	double EPSILON_ACT_TH        = 1e-3;
	int EPISODECOUNT             = 10;
	int MAXEPISODELENGTH         = 100;
	double TOTALRUNS             = Math.pow(2, NUM_ROCKS);
	long EXPANSIONTIME           = 900L;
	long BACKUPTIME              = 100L;
	long TOTALPLANNINGTIME       = 1000L;  
	boolean USE_FACTORED_BELIEFS = true;
	long P                       = EXPANSION_RATE * TOTALPLANNINGTIME;
	long K                       = EXPANSION_RATE * BACKUPTIME;
	long PMK                     = P - K;

	//    % stats
	//    cumR              = [];
	//    all.avcumrews     = [];
	//    all.avTs          = [];
	//    all.avreusedTs    = [];
	//    all.avbackuptimes = [];
	//    all.avexps        = [];
	//    all.avbackups     = [];
	//    all.avfoundeopt   = [];


	// print general config problem parameters
	display("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	display("libpomdp log - config parameters");
	display("--------------------------------");
	display("TOTALRUNS            = " + TOTALRUNS);
	display("EPISODECOUNT         = " + EPISODECOUNT);
	display("MAXEPISODELENGTH     = " + MAXEPISODELENGTH);
	display("EXPANSIONTIME        = " + EXPANSIONTIME);
	display("BACKUPTIME           = " + BACKUPTIME);
	display("EPSILON_ACT_TH       = " + EPSILON_ACT_TH);
	display("USE_FACTORED_BELIEFS = " + USE_FACTORED_BELIEFS);
	display("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

	for (int run = 1; run <= TOTALRUNS; ++run) {

	    display("====================== UPDATE RUN " + run + " of " + TOTALRUNS + "==============================");

		    //        % stats
		    //        all.stats{run}.cumrews        = [];
		    //        all.stats{run}.backups        = [];
		    //        all.stats{run}.foundeopt      = [];
		    //        all.stats{run}.meanT          = [];
		    //        all.stats{run}.meanreusedT    = [];
		    //        all.stats{run}.meanbackuptime = [];
		    //        all.stats{run}.meanexps       = [];


		    // start this run
		    for (int ep = 1; ep <= EPISODECOUNT; ++ep) {

			display("********************** EPISODE " + ep + " of " + EPISODECOUNT + "*********************");

			
			
			// are we approximating beliefs with the product of marginals?
			BeliefState b_init;
			if (USE_FACTORED_BELIEFS) {
			    DD dd_init[] = new DD[1];           
			    dd_init[0] = ((BeliefStateAdd)factoredProb.getInitialBeliefState()).bAdd;
			    b_init = new BeliefStateFactoredAdd(
				    OP.marginals(dd_init, 
					    factoredProb.getstaIds(),
					    factoredProb.getstaIdsPr()), 
					    factoredProb.getstaIds());
			} else {
			    b_init    = factoredProb.getInitialBeliefState();
			}

			// re - initialize tree at starting belief with original bounds
			aoTree = new AndOrTreeUpdateAdd(factoredProb,
                                            new HybridValueIterationOrNode(),
                                            lBound,
                                            uBound,
                                            aems2h,
                                            dosih);
			aoTree.init(b_init);
			rootNode = aoTree.getRoot();

			// starting state for this set of EPISODECOUNT episodes
		        factoredS = new int[][] { factoredProb.getstaIds(),
		        	Utils.sdecode(states.get(run), factoredProb.getnrStaV(), factoredProb.getstaArity())};

			// stats
			cumR = 0;
			bakC = 0;
			fndO = 0;
//			all.stats{run}.ep{ep}.R          = [];
//			all.stats{run}.ep{ep}.exps       = [];
//			all.stats{run}.ep{ep}.T          = [];
//			all.stats{run}.ep{ep}.reusedT    = [];
//			all.stats{run}.ep{ep}.backuptime = [];

			for (int iter = 1; iter <= MAXEPISODELENGTH; ++iter) {

			    display("******************** INSTANCE " + iter + " ********************");
			    // tc = cell(factoredProb.printS(factoredS));
			    display("Current world state is:       " + factoredProb.printS(factoredS));
			    // drawer.drawState(GRID_SIZE, ROCK_POSITIONS, factoredS);
			    if (rootNode.getBeliefState() instanceof BeliefStateFactoredAdd) {
				display("Current belief agree prob: ");                        
				display(OP.evalN(((BeliefStateFactoredAdd)rootNode.getBeliefState()).marginals, 
					factoredS).toString());
			    } else {
				display("Current belief agree prob: " +  
					OP.eval(((BeliefStateAdd)rootNode.getBeliefState()).bAdd, factoredS));
			    }

			    display("Current |T| is:                 " + rootNode.getSubTreeSize());

			    // reset expand counter
			    expC = 0;

			    //                %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			    //                % EXPANSIONS for t_exp start here:
			    //                %
			    //                % start stopwatch for expansions
			    //                %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

			    long tic = System.currentTimeMillis();
			    while (System.currentTimeMillis() - tic < EXPANSIONTIME) {
				// expand best node
				aoTree.expand(rootNode.bStar);
				// update its ancestors
				aoTree.updateAncestors(rootNode.bStar);
				// expand counter
				expC = expC + 1;
			    }

			    //                %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			    //                % BACKUP decision comes here:
			    //                %
			    //                % will go with a first approximation, and assume that ALL nodes
			    //                % supported by alpha-vec i will get improved if we have a candidate
			    //                % node supported by that alpha-vec i whose bakheuristic > 0
			    //                % we will also assume that such improvement will be forever from
			    //                % this time-step on, that is for all t > t_c
			    //                % with this, the comparison we make is 
			    //                % p n_b?c >  ?H ?(tc) k 
			    //                % of k = exp_rate * t_bak
			    //                %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

			    tic = System.currentTimeMillis();
			    boolean backedup = false;
			    long falseheurtime = 0;

			    //                % work at the root level, should not need currentBestAction anymore
			    //                % we will compute |V| of these f"s, having kept track of I^*(b) is
			    //                % enough, since |support(index(b))| / (p - k) is invariant for every
			    //                % b in the same support set:
			    //                % f(b) = ?^{d^b_T} I(b) |support(index(b))| / (p - k), b ? I(T).
			    CustomVector n_star  = aoTree.treeSupportSetSize.scale(1.0 / rootNode.getSubTreeSize()); // fraction of nodes 
			    CustomVector f       = rootNode.bakHeuristicStar.elementMult(n_star);                    // affect this fraction by (discounted I(b) * entropy)
			    int i_star           = Utils.argmax(f.getArray());                                                   // get f* and the associated index

			    // before continuing, make sure there is a feasible candidate
			    if (rootNode.bakHeuristicStar.get(i_star) > 0) {
				// get p * n_b*_c, estimate of nodes that will improve
				double imp_n = P * n_star.get(i_star) * rootNode.bakCandidate[i_star].getBeliefState().getEntropy() / Math.log(factoredProb.nrStates()); 
				// compute \gamma_H(t_c)
				double gamma_tc = (1 - factoredProb.getGamma()) / 
					(factoredProb.getGamma() - Math.pow(factoredProb.getGamma(), 
						(Math.max(AVG_EP_LIFETIME,iter) - iter + 1))); 
				// gamma_tc * K
				// if there exists a candidate, with I(b) > 0, and it fulfills the
				// decision rule, back it up
				if (imp_n > gamma_tc * K) {
				    // compute backup
				    tic = System.currentTimeMillis();
				    aoTree.backupLowerAtNode(rootNode.bakCandidate[i_star]);
				    //all.stats{run}.ep{ep}.backuptime(end+1) = toc;
				    bakC = bakC + 1;
				    // break loop
				    backedup = true;
				}
				falseheurtime = System.currentTimeMillis() - tic;
			    }

			    //                %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			    //                % EXTRA expansions may happen here for t_bak
			    //                %
			    //                % if not, continue expanding for the remaining 0.1 secs
			    //                %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  

			    if (!backedup) {
				tic = System.currentTimeMillis();
				while (System.currentTimeMillis() - tic < BACKUPTIME - falseheurtime) {
				    // expand best node
				    aoTree.expand(rootNode.bStar);
				    // update its ancestors
				    aoTree.updateAncestors(rootNode.bStar);
				    // expand counter
				    expC = expC + 1;
				}
			    }

			    //                %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			    //                % at this point, TOTALPLANNINGTIME has elapsed
			    //                %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
			    //                % get the support list for the action we are about to output
			    //                % obtain the best action for the root
			    //                % remember that a"s and o"s in matlab should start from 1
			    //                % need to do this a priori!! otherwise currentBestAction() could
			    //                % change since its randomized
			    int a = aoTree.currentBestAction();
			    a = a + 1;

			    // check whether we found an e-optimal action, either in the
			    // EXPANSIONTIME slot or in the TOTALPLANNINGTIME slot
			    if (aoTree.actionIsEpsOptimal(a-1, EPSILON_ACT_TH)) { // careful with action indexes
				display("Achieved e-optimal action!");
				fndO = fndO + 1;
			    }

			    // execute best action and receive new o
			    DD restrictedT[]   = OP.restrictN(factoredProb.T[a], factoredS);
			    int factoredS1[][] = OP.sampleMultinomial(restrictedT, factoredProb.getstaIdsPr());
			    DD restrictedO[]   = OP.restrictN(factoredProb.O[a], Utils.horzCat(factoredS, factoredS1));
			    int factoredO[][]  = OP.sampleMultinomial(restrictedO, factoredProb.getobsIdsPr());

			    // save stats
//			    all.stats{run}.ep{ep}.exps(end+1)  = expC;
//			    all.stats{run}.ep{ep}.R   (end+1)  = OP.eval(factoredProb.R(a), factoredS);
//			    all.stats{run}.ep{ep}.T   (end+1)  = rootNode.subTreeSize;
//			    cumR = cumR + factoredProb.getGamma^(iter - 1) * all.stats{run}.ep{ep}.R(end);


			    // output some stats
			    display("Expansion finished, # expands:  " + expC);
			    display("|T|:                            " + rootNode.getSubTreeSize());
			    //tc = cell(factoredProb.getactStr(a-1));
			    display("Outputting action:              " + factoredProb.getActionString(a - 1));
			    //tc = cell(factoredProb.printO(factoredO));
			    display("Perceived observation:          " + factoredProb.printO(factoredO));
			    display("Received reward:                " + OP.eval(factoredProb.R[a], factoredS));
			    display("Cumulative reward:              " +  cumR);

			    // check whether this episode ended (terminal state)
			    if(factoredS1[2][1] == GRID_SIZE+1) {
				display("==================== Episode ended at instance " + iter + "==================");
				//drawer.drawState(GRID_SIZE, ROCK_POSITIONS, factoredS1);
				break;
			    }

			    //     pause;

			    // move the tree"s root node - RE_INIT INSTEAD FOR NOW
			    int o = Utils.sencode(factoredO[2],
				    factoredProb.getnrObsV(), 
				    factoredProb.getobsArity());
			    // aoTree.moveTree(rootNode.children(a).children(o)); 
			    // create new tree, but keep new bounds
			    aoTree = new AndOrTreeUpdateAdd(factoredProb,
                                                new HybridValueIterationOrNode(),
                                                aoTree.getLB(),
                                                aoTree.getUB(),
                                                aems2h,
                                                dosih);
			    // initialize at the new belief
			    aoTree.init(rootNode.getChild(a - 1).getChild(o - 1).getBeliefState());
			    // update reference to rootNode
			    rootNode = aoTree.getRoot();

			    display("Tree moved, reused |T|:         " + rootNode.getSubTreeSize());
			    //all.stats{run}.ep{ep}.reusedT(end+1)  = rootNode.subTreeSize;

			    // iterate
			    //b = b1;
			    factoredS = factoredS1;
			    factoredS = Config.primeVars(factoredS, -factoredProb.getnrTotV());

			} // time-steps loop

//			all.stats{run}.cumrews       (end+1)   = cumR;
//			all.stats{run}.backups       (end+1)   = bakC;
//			all.stats{run}.foundeopt     (end+1)   = fndO;
//			all.stats{run}.meanT         (end+1)   = mean(all.stats{run}.ep{ep}.T);
//			all.stats{run}.meanreusedT   (end+1)   = mean(all.stats{run}.ep{ep}.reusedT);
//			all.stats{run}.meanbackuptime(end+1)   = mean(all.stats{run}.ep{ep}.backuptime);
//			all.stats{run}.meanexps      (end+1)   = mean(all.stats{run}.ep{ep}.exps);
			//pause

		    } // episodes loop

	    // average cum reward of this run of 20 episodes
//	    all.avcumrews    (end+1)   = mean(all.stats{run}.cumrews);
//	    all.avbackups    (end+1)   = mean(all.stats{run}.backups);
//	    all.avfoundeopt  (end+1)   = mean(all.stats{run}.foundeopt);
//	    all.avTs         (end+1)   = mean(all.stats{run}.meanT);
//	    all.avreusedTs   (end+1)   = mean(all.stats{run}.meanreusedT);
//	    all.avbackuptimes(end+1)   = mean(all.stats{run}.meanbackuptime);
//	    all.avexps       (end+1)   = mean(all.stats{run}.meanexps);

	} // runs loop

	// save statistics before quitting
	//statsFilename = 
	 //   sprintf("simulation-logs/rocksample/RS78-ALLSTATS-HYVI-regions-ADD-%s.mat", datestr(now, "yyyy-mmm-dd-HHMMSS"));
	//save(statsFilename, "all");


    }

    private static void display(String m) { System.out.println(m); }



}
