/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: expandHeuristic.java
 * Description: interface to define different online expansion heuristics
 *              see README reference [2] in root dir
 * Copyright (c) 2009, 2010 Diego Maniloff
 --------------------------------------------------------------------------- */

package libpomdp.online.java;

public interface ExpandHeuristic {

    /// H(b): heuristic for the orNode
    public double h_b(HeuristicSearchOrNode o);

    /// H(b,a): edge between orNode and andNode
    /// easier to compute at the orNode level
    public double[] h_ba(HeuristicSearchOrNode o);

    //public double[] h_baUpdate(HeuristicSearchOrNode o, int a);

    /// H(b,a,o): edge between andNode and orNode
    public double h_bao(HeuristicSearchOrNode o);

    /// H*(b,a)
    public double hANDStar(HeuristicSearchAndNode a);

    /// H*(b)
    public double hORStar(HeuristicSearchOrNode o);

    /// o* = argmax_o {H(b,a,o) H*(tao(b,a,o))}
    public int oStar(HeuristicSearchAndNode a);

    //public int oStarUpdate(andNode a, int o);

    /// a* = argmax_a {H(b,a) H*(b,a)}
    public int aStar(HeuristicSearchOrNode o);
    
} // expandHeuristic
