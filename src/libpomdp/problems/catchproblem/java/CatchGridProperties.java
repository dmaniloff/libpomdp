/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CatchGridProperties.java
 * Description: Interface to implement different grid configurations of the
 *              catch environment. By configurations we mean walls for now.
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.problems.catchproblem.java;

interface CatchGridProperties {

    // is there a wall ahead of position in direcion m?
    public boolean wallAhead (int position, CatchGen.Direction move);

    // is this position a legal one?
    public boolean isLegalPosition (int position);

    // how many legal locations are there?
    public int totPossibleLocations ();

    // simple utility function to get x and y coordinates
    public int[] getxy (int pos);
}
