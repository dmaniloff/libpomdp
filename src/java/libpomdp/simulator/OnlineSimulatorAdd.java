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
import libpomdp.solve.online.AndOrTree;
import libpomdp.solve.online.HeuristicSearchOrNode;
import libpomdp.solve.online.AEMS2;
import libpomdp.solve.offline.bounds.BpviAdd;
import libpomdp.solve.offline.bounds.QmdpAdd;
import libpomdp.parser.FileParser;
import libpomdp.problemgen.rocksample.RockSampleGraph;

class OnlineSimulatorAdd {

    public static void main(String args[]) {

        // declarations
        AndOrTree aoTree;
        HeuristicSearchOrNode rootNode;

        int expC = 0;
        int foundOptimalAction = 0;
        double cumR = 0;

        // problem name
        String probFilename = "../../../../data/problems/rocksample/RockSample_7_8.SPUDD";

        // load problem
        PomdpAdd factoredProb = (PomdpAdd)
            FileParser.loadPomdp(probFilename, FileParser.PARSE_SPUDD);

        // load bounds
        ValueFunction uBound =
            FileParser.loadUpperBound(probFilename,
                                      FileParser.PARSE_SPUDD);
        ValueFunction lBound =
            FileParser.loadLowerBound(probFilename,
                                      FileParser.PARSE_SPUDD);

        // create heuristics
        AEMS2 aems2h = new AEMS2(factoredProb);

        // figure out all possible initial states of the pomdp
        List<Integer> states = new ArrayList<Integer>();
        int factoredS[][];

        for (int r = 0; r < factoredProb.nrStates(); ++r) {
            factoredS = new int [][] {
                factoredProb.getstaIds() ,
                Utils.sdecode(r,
                              factoredProb.getnrStaV(),
                              factoredProb.getstaArity())
            };
            if (OP.eval(((BeliefStateAdd)
                         factoredProb.getInitialBeliefState()).bAdd,
                        factoredS) > 0) {
                states.add(new Integer(r));
            }
        }


        //  play the pomdp
        //logFilename = sprintf("simulation-logs/rocksample/RS78-HYVI-regions-ADD-%s.log", datestr(now, "yyyy-mmm-dd-HHMMSS"));
        //diary(logFilename);

        // rocksample parameters for the grapher
        int GRID_SIZE                = 7;
        int ROCK_POSITIONS[][]       = new int[][] {{2, 0}, {0, 1}, {3, 1}, {6, 3}, {2, 4}, {3, 4}, {5, 5}, {1, 6}};
        int SARTING_POS[]            = new int[]    {0, 3};


        int NUM_ROCKS                = ROCK_POSITIONS.length;

        // general parameters
        final double EPSILON_ACT_TH        = 1e-3;
        final int EPISODECOUNT             = 10;
        final int MAXEPISODELENGTH         = 100;
        final int TOTALRUNS                = (int) Math.pow(2, NUM_ROCKS);
        final long EXPANSIONTIME           = 1000L;
        final long TOTALPLANNINGTIME       = 1000L;
        final boolean USE_FACTORED_BELIEFS = true;

        // stats
        SimulatorStatistics stats = new SimulatorStatistics(TOTALRUNS,
                                                            EPISODECOUNT);

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
        display("EPSILON_ACT_TH       = " + EPSILON_ACT_TH);
        display("USE_FACTORED_BELIEFS = " + USE_FACTORED_BELIEFS);
        display("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        // main loop - each run represents a different starting state
        for (int runNumber = 0; runNumber < TOTALRUNS; ++runNumber) {

            display("====================== UPDATE RUN " + runNumber + " of " + TOTALRUNS + "==============================");

		    //        % stats
		    //        all.stats{run}.cumrews        = [];
		    //        all.stats{run}.backups        = [];
		    //        all.stats{run}.foundeopt      = [];
		    //        all.stats{run}.meanT          = [];
		    //        all.stats{run}.meanreusedT    = [];
		    //        all.stats{run}.meanbackuptime = [];
		    //        all.stats{run}.meanexps       = [];


		    // repeat this run's startingt state EPISODECOUNT times
		    for (int episodeNumber = 0; episodeNumber < EPISODECOUNT; ++episodeNumber) {

                display("********************** EPISODE " + episodeNumber + " of " + EPISODECOUNT + "*********************");



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
                aoTree = new AndOrTree(factoredProb,
                                       new HeuristicSearchOrNode(),
                                       lBound,
                                       uBound,
                                       aems2h);
                aoTree.init(b_init);
                rootNode = aoTree.getRoot();

                // starting state for this set of EPISODECOUNT episodes
		        factoredS = new int[][] {
                    factoredProb.getstaIds(),
                    Utils.sdecode(states.get(runNumber),
                                  factoredProb.getnrStaV(),
                                  factoredProb.getstaArity())
                };

                // stats
                cumR = 0;
                //			all.stats{run}.ep{ep}.R          = [];
                //			all.stats{run}.ep{ep}.exps       = [];
                //			all.stats{run}.ep{ep}.T          = [];
                //			all.stats{run}.ep{ep}.reusedT    = [];
                //			all.stats{run}.ep{ep}.backuptime = [];

                // action - sensing - thinking loop
                for (int iter = 0; iter < MAXEPISODELENGTH; ++iter) {

                    display("INSTANCE " + iter);
                    displaySep("*");

                    display("Current world state is:       " + factoredProb.printS(factoredS));
                    RockSampleGraph.drawState(GRID_SIZE, ROCK_POSITIONS, factoredS);

                    if (rootNode.getBeliefState() instanceof BeliefStateFactoredAdd) {
                        display("Current belief agree prob: ");
                        display( OP.evalN(( (BeliefStateFactoredAdd)
                                            rootNode.getBeliefState()).marginals,
                                          factoredS) );
                    } else {
                        display("Current belief agree prob: " +
                                OP.eval(((BeliefStateAdd)rootNode.getBeliefState()).bAdd, factoredS));
                    }

                    display("Current |T| is:                 " +
                            rootNode.getSubTreeSize());

                    // reset counters
                    expC = 0;
                    foundOptimalAction = 0;

                    // think
                    long tic = System.currentTimeMillis();
                    while (System.currentTimeMillis() - tic < EXPANSIONTIME) {
                        // expand best node
                        aoTree.expand(rootNode.bStar);
                        // update its ancestors
                        aoTree.updateAncestors(rootNode.bStar);
                        // expand counter
                        expC = expC + 1;
                    }

                    // action to execute
                    int a = aoTree.currentBestAction();

                    // check whether we found an e-optimal action
                    if (aoTree.actionIsEpsOptimal(a, EPSILON_ACT_TH)) {
                        foundOptimalAction = 1;
                    }

                    // execute best action and receive new o
                    DD restrictedT[] =
                        OP.restrictN(factoredProb.T[a], factoredS);
                    int factoredS1[][] =
                        OP.sampleMultinomial(restrictedT,
                                             factoredProb.getstaIdsPr());
                    DD restrictedO[] =
                        OP.restrictN(factoredProb.O[a],
                                     Utils.horzCat(factoredS, factoredS1));
                    int factoredO[][] =
                        OP.sampleMultinomial(restrictedO,
                                             factoredProb.getobsIdsPr());

                    int o = Utils.sencode(factoredO[1],
                                          factoredProb.getnrObsV(),
                                          factoredProb.getobsArity());


                    // save stats
                    stats.run[runNumber].episode[episodeNumber].
                        receivedReward.add( OP.eval(factoredProb.R[a],
                                                    factoredS) );
                    stats.run[runNumber].episode[episodeNumber].
                        treeSize.add( rootNode.getSubTreeSize() );
                    stats.run[runNumber].episode[episodeNumber].
                        nodeExpansions.add( expC );
                    stats.run[runNumber].episode[episodeNumber].
                        outputAction.add( a );
                    stats.run[runNumber].episode[episodeNumber].
                        foundEpsilonOptimal.add( foundOptimalAction );
                    stats.run[runNumber].episode[episodeNumber].
                        perceivedObservation.add( o );

                    // output some stats
                    display("Expansion finished, # expands:  " +
                            expC);
                    display("Expansion finished, # expands:  " +
                            stats.run[runNumber].episode[episodeNumber].
                            nodeExpansions.get( iter ));

                    display("|T|:                            " +
                            rootNode.getSubTreeSize());
                    display("|T|:                            " +
                            stats.run[runNumber].episode[episodeNumber].
                            treeSize.get( iter ));

                    display("Outputting action:              " +
                            factoredProb.getActionString( a ));
                    display("Outputting action:              " +
                            stats.run[runNumber].episode[episodeNumber].
                            outputAction.get( iter ) +
                            "( "+ factoredProb.
                            getActionString(stats.run[runNumber].episode[episodeNumber].
                                            outputAction.get( iter ) ) +")") ;

                    display("Perceived observation:          " +
                            factoredProb.printO(factoredO));
                    display("Perceived observation:          " +
                            stats.run[runNumber].episode[episodeNumber].
                            perceivedObservation.get( iter ));

                    display("Received reward:                " +
                            OP.eval(factoredProb.R[a], factoredS));
                    display("Received reward:                " +
                            stats.run[runNumber].episode[episodeNumber].
                            receivedReward.get( iter ));

                    display("Cumulative reward:              " +
                            cumR);

                    // check whether this episode ended (terminal state)
                    if(factoredS1[1][0] == GRID_SIZE+1) {
                        display("Episode ended at instance " + iter);
                        RockSampleGraph.drawState(GRID_SIZE, ROCK_POSITIONS,
                                                  factoredS1);
                        break;
                    }

                    // move the tree's root -- FIGURE OUT ZERO_BASED!!
                    aoTree.moveTree(rootNode.getChild( a ).getChild(o - 1));

                    // update reference to rootNode
                    rootNode = aoTree.getRoot();

                    // the new |T| is the reused |T|
                    stats.run[runNumber].episode[episodeNumber].
                        reusedTreeSize.add( rootNode.getSubTreeSize() );

                    display("Tree moved, reused |T|:         " +
                            rootNode.getSubTreeSize());
                    display("Tree moved, reused |T|:         " +
                            stats.run[runNumber].episode[episodeNumber].
                            reusedTreeSize.get( iter ));

                    // iterate
                    factoredS = factoredS1;
                    factoredS = Config.primeVars(factoredS,
                                                 - factoredProb.getnrTotV());

                } // time-steps loop

                // conclude episode stats
                stats.run[runNumber].summarizeEpisode( episodeNumber,
                                                       factoredProb.getGamma() );

		    } // episodes loop

            // conclude run stats
            stats.summmarizeRun( runNumber );

            // print out what we have so far
            System.out.println( stats.toString() );
        } // runs loop

        // save statistics before quitting
        //statsFilename =
        //   sprintf("simulation-logs/rocksample/RS78-ALLSTATS-HYVI-regions-ADD-%s.mat", datestr(now, "yyyy-mmm-dd-HHMMSS"));
        //save(statsFilename, "all");


    }

    static void display(String m) { System.out.println(m); }

    static void display(double m[]) {
        String pretty = "[ ";
        for( double d : m ) pretty += String.format("%.4f ", d);
        pretty += "]";
        System.out.println( pretty );
    }

    static void displaySep(String s) {
        for (int i=0; i<80; ++i) System.out.print(s);
        System.out.println();
    }

}
