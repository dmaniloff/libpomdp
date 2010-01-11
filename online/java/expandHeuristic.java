/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: expandHeuristic.java
 * Description: interface to define different online expansion heuristics
 *              see README reference [2] in root dir
 * Copyright (c) 2010, Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof 
 --------------------------------------------------------------------------- */

interface expandHeuristic {

    /// H(b): heuristic for the orNode
    public double h_b(orNode o);

    /// H(b,a): edge between orNode and andNode
    /// easier to compute at the orNode level
    public double[] h_ba(orNode o);

    public double[] h_baUpdate(orNode o, int a);

    /// H(b,a,o): edge between andNode anr orNode
    public double h_bao(orNode o);

    /// H*(b,a)
    public double hANDStar(andNode a);

    /// H*(b)
    public double hORStar(orNode o);

    /// o* = argmax_o {H(b,a,o) H*(tao(b,a,o))}
    public int oStar(andNode a);

    //public int oStarUpdate(andNode a, int o);

    /// a* = argmax_a {H(b,a) H*(b,a)}
    public int aStar(orNode o);
    
} // expandHeuristic
