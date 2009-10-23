/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: heuristic.java
 * Author: Diego Maniloff
 * Description: interface to define different online heuristic search
 *              methods
 * Copyright (c) 2009, Diego Maniloff.  
 --------------------------------------------------------------------------- */

interface heuristic {

    /// H(b)
    public double hOR(orNode o);

    /// H(b,a,o)
    public double hOR_o(orNode o);

    /// H(b,a)
    public double hAND(andNode a);

}
	