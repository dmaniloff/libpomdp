/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CollocatedObs.java
 * Description: this observation fires up whenever the agent collocates with
 *              the wumpus
 * Copyright (c) 2009, 2010 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.problems.catchproblem;

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
	// simply check whether this is an adjacent position
	return (apos == wpos);

    }

}