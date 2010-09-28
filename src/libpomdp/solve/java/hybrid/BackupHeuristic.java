/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: backupHeuristic.java
 * Description: interface to define different online backup heuristics
 * Copyright (c) 2009, 2010 Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof 
 --------------------------------------------------------------------------- */

package libpomdp.solve.java.hybrid;

// imports
import libpomdp.solve.java.online.*;

interface BackupHeuristic {

    public double h_b(orNode o);

    public orNode bakStar(andNode a);

    public double bakHStar(andNode a);

    public orNode updateBakStar(andNode a, int o);

    public orNode updateBakStar(orNode o, int a);
    
} // backupHeuristic
