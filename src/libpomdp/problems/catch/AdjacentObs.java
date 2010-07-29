/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: AdjacentObs.java
 * Description: Wumpus-world like observation function: fires up whenever the
 *              wumpus is adjacent (in the N-S-E-W sense) to the agent.
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

public class AdjacentObs implements Sensor {

    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    CatchGridProperties gp;


    // constructor
    public AdjacentObs(CatchGridProperties gp) {
	this.gp = gp; 
    }

    public boolean inRange(int apos, int wpos) {
	// simply check wether this is an adjacent position
	return (getAdjIndex(apos, wpos) >= 0);
	
    }

    private int getAdjIndex(int currpos, int nextpos) {
	// 0-none, 1-north, 2-east, 3-south, 4-west
	int currx = gp.getxy(currpos)[0];
	int curry = gp.getxy(currpos)[1];
	int nextx = gp.getxy(nextpos)[0];
	int nexty = gp.getxy(nextpos)[1];
	// none
	if(nexty == curry && nextx == currx)
	    return 0;
	// north
	else if (nexty == curry+1 && nextx == currx)
	    return 1;
	// east
	else if (nexty == curry && nextx == currx+1)
	    return 2;
	// south
	else if (nexty == curry-1 && nextx == currx)
	    return 3;
	// west
	else if (nexty == curry && nextx == currx-1)
	    return 4;
	else
	    return -1;
    }    

}