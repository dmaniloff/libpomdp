/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: heuristic.java
 * Description: interface to define different online heuristic search
 *              methods - see README reference [2] in root dir
 * Copyright (c) 2009, Diego Maniloff
 * W3: http://www.cs.uic.edu/~dmanilof 
 --------------------------------------------------------------------------- */

interface heuristic {

    /// H(b)
    public double hOR(orNode o);

    /// H(b,a)
    public double[] hOR_a(orNode o);

    /// H*(b,a)
    public double hANDStar(andNode a);

    /// H(b,a,o)
    public double[] hAND_o(andNode a);

    /// argmax_o H(b,a,o) H*(tao(b,a,o))
    public int bestO(andNode a);

    /// argmax_a H(b,a) H*(b,a)
    public int bestA(orNode o);
    
} // heuristic
