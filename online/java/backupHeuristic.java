/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: backupHeuristic.java
 * Description: interface to define different online backup heuristics
 * Copyright (c) 2010, Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof 
 --------------------------------------------------------------------------- */

interface backupHeuristic {

    public double h_b(orNode o);

    public orNode bakStar(andNode a);

    public double bakHStar(andNode a);

    public orNode updateBakStar(andNode a, int o);

    public orNode updateBakStar(orNode o, int a);
    
} // backupHeuristic
