/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: Sensor.java
 * Description: Interface to implement different observation functions
 *              for the agent in the catch environment
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.problems.catchproblem.java;

interface Sensor {

    // simply say wether the wumpus is in range for this
    // observation function - can be implemented with various radii
    public boolean inRange(int apos, int wpos);

}