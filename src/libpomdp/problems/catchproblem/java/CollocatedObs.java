/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CollocatedObs.java
 * Description: this observation fires up whenever the agent collocates with
 *              the wumpus
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.problems.catchproblem.java;

public class CollocatedObs implements Sensor {

    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    CatchGridProperties gp;

    // constructor
    public CollocatedObs(CatchGridProperties gp) {
	this.gp = gp; 
    }

    public boolean inRange(int apos, int wpos) {
	// simply check wether this is an adjacent position
	return (apos == wpos);
	
    }    

}