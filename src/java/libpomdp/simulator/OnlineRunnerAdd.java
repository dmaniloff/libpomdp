package libpomdp.simulator;

import libpomdp.problemgen.StateDrawer;
import libpomdp.problemgen.rocksample.RockSampleGraph;

class OnlineRunnerAdd {

    public static void main(String args[]) {

        String pomdpFilename = null;
        String statsFilename = null;
        OnlineSimulatorAdd.TerminalStateEvaluator ev = null;
        StateDrawer drawer = null;

        System.out.println("Running simulator for config " + args[0] + "...");

        if(args[0].equals("RS78")){
            // config
            pomdpFilename = "data/problems/rocksample/RockSample_7_8.SPUDD";
            statsFilename = pomdpFilename + ".ONLINE.STATS.ser";

            // drawer
            final int GRID_SIZE = 7;
            final int ROCK_POSITIONS[][] = new int[][] {{2, 0},
                                                        {0, 1},
                                                        {3, 1},
                                                        {6, 3},
                                                        {2, 4},
                                                        {3, 4},
                                                        {5, 5},
                                                        {1, 6}};

            // terminal state ev
            ev = new OnlineSimulatorAdd.TerminalStateEvaluator() {
                    @Override
                    boolean isTerminalState(int factoredS[][]) {
                        return factoredS[1][0] == GRID_SIZE+1;
                    }
                };

            // state drawer
            drawer = new RockSampleGraph(GRID_SIZE,
                                         ROCK_POSITIONS);
        } else if(args[0].equals("RS710")) {
            // config
            pomdpFilename = "data/problems/rocksample/RockSample_7_10.SPUDD";
            statsFilename = pomdpFilename + ".ONLINE.STATS.ser";

            // drawer
            final int GRID_SIZE = 7;
            final int ROCK_POSITIONS[][] = new int[][] {{2, 0},
                                                        {0, 1},
                                                        {3, 1},
                                                        {6, 3},
                                                        {2, 4},
                                                        {3, 4},
                                                        {5, 5},
                                                        {1, 6},
                                                        {6, 0},
                                                        {6, 6}};

            // terminal state ev
            ev = new OnlineSimulatorAdd.TerminalStateEvaluator() {
                    @Override
                    boolean isTerminalState(int factoredS[][]) {
                        return factoredS[1][0] == GRID_SIZE+1;
                    }
                };

            // state drawer
            drawer = new RockSampleGraph(GRID_SIZE,
                                         ROCK_POSITIONS);
        } else if (args[0].equals("RS1010")) {
            // config
            pomdpFilename = "data/problems/rocksample/RockSample_10_10.SPUDD";
            statsFilename = pomdpFilename + ".ONLINE.STATS.ser";

            // drawer
            final int GRID_SIZE = 10;
            final int ROCK_POSITIONS[][] = new int[][] {{0, 3},
                                                        {0, 7},
                                                        {1, 8},
                                                        {3, 3},
                                                        {3, 8},
                                                        {4, 3},
                                                        {5, 8},
                                                        {6, 1},
                                                        {9, 3},
                                                        {9, 9}};

            // terminal state ev
            ev = new OnlineSimulatorAdd.TerminalStateEvaluator() {
                    @Override
                    boolean isTerminalState(int factoredS[][]) {
                        return factoredS[1][0] == GRID_SIZE+1;
                    }
                };

            // state drawer
            drawer = new RockSampleGraph(GRID_SIZE,
                                         ROCK_POSITIONS);
        } else if (args[0] == "RS1011") {
            // config
            pomdpFilename = "data/problems/rocksample/RockSample_10_11.SPUDD";
            statsFilename = pomdpFilename + ".ONLINE.STATS.ser";

            // drawer
            final int GRID_SIZE = 11;
            final int ROCK_POSITIONS[][] = new int[][] {{0, 3},
                                                        {0, 7},
                                                        {1, 8},
                                                        {3, 3},
                                                        {3, 8},
                                                        {4, 3},
                                                        {5, 8},
                                                        {6, 1},
                                                        {9, 3},
                                                        {9, 9},
                                                        {9, 0}};

            // terminal state ev
            ev = new OnlineSimulatorAdd.TerminalStateEvaluator() {
                    @Override
                    boolean isTerminalState(int factoredS[][]) {
                        return factoredS[1][0] == GRID_SIZE+1;
                    }
                };

            // state drawer
            drawer = new RockSampleGraph(GRID_SIZE,
                                         ROCK_POSITIONS);
        } else if (args[0].equals("RS1015")) {
            // config
            pomdpFilename = "data/problems/rocksample/RockSample_10_15.SPUDD";
            statsFilename = pomdpFilename + ".ONLINE.STATS.ser";

            // drawer
            final int GRID_SIZE = 11;
            final int ROCK_POSITIONS[][] = new int[][] {{0, 3},
                                                        {0, 7},
                                                        {1, 8},
                                                        {3, 3},
                                                        {3, 8},
                                                        {4, 3},
                                                        {5, 8},
                                                        {6, 1},
                                                        {9, 3},
                                                        {9, 9},
                                                        {0, 0},
                                                        {0, 9},
                                                        {3, 0},
                                                        {3, 9},
                                                        {9, 0}};

            // terminal state ev
            ev = new OnlineSimulatorAdd.TerminalStateEvaluator() {
                    @Override
                    boolean isTerminalState(int factoredS[][]) {
                        return factoredS[1][0] == GRID_SIZE+1;
                    }
                };

            // state drawer
            drawer = new RockSampleGraph(GRID_SIZE,
                                         ROCK_POSITIONS);
        } else if (args[0].equals("RS1512")) {
            // config
            pomdpFilename = "data/problems/rocksample/RockSample_15_12.SPUDD";
            statsFilename = pomdpFilename + ".ONLINE.STATS.ser";

            // drawer
            final int GRID_SIZE = 15;
            final int ROCK_POSITIONS[][] = new int[][] {{0, 9},
                                                        {0, 13},
                                                        {1, 14},
                                                        {3, 9},
                                                        {3, 14},
                                                        {4, 9},
                                                        {5, 14},
                                                        {6, 9},
                                                        {9, 14},
                                                        {9, 9},
                                                        {4, 0},
                                                        {14, 0}};

            // terminal state ev
            ev = new OnlineSimulatorAdd.TerminalStateEvaluator() {
                    @Override
                    boolean isTerminalState(int factoredS[][]) {
                        return factoredS[1][0] == GRID_SIZE+1;
                    }
                };

            // state drawer
            drawer = new RockSampleGraph(GRID_SIZE,
                                         ROCK_POSITIONS);
        }

        // instantiate simulator
        OnlineSimulatorAdd simulator = new OnlineSimulatorAdd(pomdpFilename,
                                                              statsFilename,
                                                              System.out);

        // set its terminal state conditions (varies according to problem)
        simulator.setTerminalEvaluator( ev );

        // set its state drawer
        simulator.setDrawer( drawer );

        // simulate
        simulator.run();

    }

}