package libpomdp.simulator;

import libpomdp.common.Utils;

import java.util.ArrayList;

class SimulatorStatistics {

    // instance classes
    class EpisodeStats {
        ArrayList<Double>  receivedReward;
        ArrayList<Integer> treeSize;
        ArrayList<Integer> reusedTreeSize;
        ArrayList<Integer> nodeExpansions;
        ArrayList<Integer> outputAction;
        ArrayList<Integer> foundEpsilonOptimal;
        ArrayList<Integer> perceivedObservation;

        EpisodeStats() {
            receivedReward = new ArrayList<Double>();
            treeSize = new ArrayList<Integer>();
            reusedTreeSize = new ArrayList<Integer>();
            nodeExpansions = new ArrayList<Integer>();
            outputAction = new ArrayList<Integer>();
            foundEpsilonOptimal = new ArrayList<Integer>();
            perceivedObservation = new ArrayList<Integer>();
        }

    }

    class RunStats {
        EpisodeStats episode[];
        // stats to summarize each episode in episode
        ArrayList<Double> discountedReward;
        ArrayList<Double> meanTreeSize;
        ArrayList<Double> meanReusedTreeSize;
        ArrayList<Double> meanNodeExpansions;
        ArrayList<Double> totalEpsilonOptimal;

        RunStats (int EPISODECOUNT) {
            episode = new EpisodeStats[EPISODECOUNT];
            for (int i=0; i<EPISODECOUNT; ++i) episode[i] = new EpisodeStats();

            discountedReward = new ArrayList<Double>();
            meanTreeSize = new ArrayList<Double>();
            meanReusedTreeSize = new ArrayList<Double>();
            meanNodeExpansions = new ArrayList<Double>();
            totalEpsilonOptimal = new ArrayList<Double>();
        }

        public void summarizeEpisode(final int episodeNumber,
                                     final double gamma) {
            // assume array passed in has 5 slots
            discountedReward.
                add( Utils.discountedSum( episode[episodeNumber].
                                          receivedReward, gamma ) );
            meanTreeSize.
                add( Utils.mean( episode[episodeNumber].
                                 treeSize ) );
            meanReusedTreeSize.
                add( Utils.mean( episode[episodeNumber].
                                 reusedTreeSize ) );
            meanNodeExpansions.
                add( Utils.mean( episode[episodeNumber].
                                 nodeExpansions ) );
            totalEpsilonOptimal.
                add( Utils.sum( episode[episodeNumber].
                                foundEpsilonOptimal) );
        }

    }

    // instance variables
    RunStats run[];

    final int TOTALRUNS;
    final int EPISODECOUNT;

    // stats to summarize each run in run
    ArrayList<Double> avgCumReward;
    ArrayList<Double> avgTreeSize;
    ArrayList<Double> avgReusedTreeSize;
    ArrayList<Double> avgNodeExpansions;
    ArrayList<Double> avgFoundEpsilonOptimal;

    // constructor
    SimulatorStatistics(final int TOTALRUNS, final int EPISODECOUNT) {
        this.TOTALRUNS = TOTALRUNS;
        this.EPISODECOUNT = EPISODECOUNT;

        run = new RunStats[TOTALRUNS];
        for (int i=0; i<TOTALRUNS; ++i) run[i] = new RunStats(EPISODECOUNT);

        avgCumReward = new ArrayList<Double>();
        avgTreeSize = new ArrayList<Double>();
        avgReusedTreeSize = new ArrayList<Double>();
        avgNodeExpansions = new ArrayList<Double>();
        avgFoundEpsilonOptimal = new ArrayList<Double>();
    }

    public void summmarizeRun(int runNumber) {
        avgCumReward.add( Utils.mean( run[runNumber].discountedReward ) );
        avgTreeSize.add( Utils.mean( run[runNumber].meanTreeSize ) );
        avgReusedTreeSize.add( Utils.mean( run[runNumber].meanReusedTreeSize ) );
        avgNodeExpansions.add( Utils.mean( run[runNumber].meanNodeExpansions ) );
        avgFoundEpsilonOptimal.add( Utils.mean( run[runNumber].totalEpsilonOptimal ) );
    }

    // summarization
    @Override
    public String toString() {
        String ret = "";
        ret += String.format("Stats for %d runs and %d episodes in each run\n",
                             TOTALRUNS, EPISODECOUNT);
        ret += String.format("(Completed %d runs thus far).\n",
                             avgCumReward.size());
        ret += String.format("AVG cumulative rewards: %.6f\n",
                             Utils.mean(avgCumReward));
        ret += String.format("AVG tree size:          %.6f\n",
                             Utils.mean(avgTreeSize));
        ret += String.format("AVG reused tree size:   %.6f\n",
                             Utils.mean(avgReusedTreeSize));
        ret += String.format("AVG node expansions:    %.6f\n",
                             Utils.mean(avgNodeExpansions));
        ret += String.format("AVG found eps-opt acts: %.6f\n",
                             Utils.mean(avgFoundEpsilonOptimal));
        return ret;
    }

}