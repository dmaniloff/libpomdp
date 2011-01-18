/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: backupHeuristic.java
 * Description: interface to define different online backup heuristics
 * Copyright (c) 2009, 2010 Diego Maniloff
 --------------------------------------------------------------------------- */

package libpomdp.solve.hybrid;

// imports

interface BackupHeuristic {

    public double h_b(HybridValueIterationOrNode o);

    // public orNode bakStar(andNode a);

    // public double bakHStar(andNode a, int i);

    public HybridValueIterationOrNode updateBakStar(
	    HybridValueIterationAndNode a, int o, int i);

    public HybridValueIterationOrNode updateBakStar(
	    HybridValueIterationOrNode o, int a, int i);

} // backupHeuristic
