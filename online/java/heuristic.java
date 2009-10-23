/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: heuristic.java
 * Description: interface to define different online heuristic search
 *              methods
 * Copyright (c) 2009, Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof 
 --------------------------------------------------------------------------- */

interface heuristic {

    /// H(b)
    public double hOR(orNode o);

    /// H(b,a,o)
    public double hOR_o(orNode o);

    /// H(b,a)
    public double hAND(andNode a);

}
	