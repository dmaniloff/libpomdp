/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: OmniWumpus.java
 * Description: Wumpus implementation for the catch environment where
 *              the wumpus moves away from the agent with a varying
 *              omniciency level. Uses a CatchGridProperties to make sure
 *              the returned action distribution is consistent with the world.
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.problems.catchproblem;

public class OmniWumpus implements Wumpus {

    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    CatchGridProperties gp;
    double ol;

    // constructor
    public OmniWumpus(CatchGridProperties gp, double omnilevel) {
	this.gp = gp;
	this.ol = omnilevel;
    }

    // returns a vector with the action distribution of the wumpus
    // (0-none, 1-north, 2-east, 3-south, 4-west)
    // implements an omniscient wumpus with prob ol
    // the wumpus makes decisions based on the agent's location and its
    // own location only - it does not use knowledge of the walls in the env
    // also notice how setting ol = 0.5 is not exactly the same as implementing
    // a truly random wumpus
    public double[] getActDist(int apos, int wpos) {	
	
	double ret[]    = new double[5];
	// get positions
	int agentpos [] = gp.getxy(apos);
	int wumpuspos[] = gp.getxy(wpos);
	int ax = agentpos [0]; int ay = agentpos [1];
	int wx = wumpuspos[0]; int wy = wumpuspos[1];

	// will stay put with prob 1 - ol
	ret[0] = 1.0 - ol;

	// south of the wumpus, the wumpus will move north with probability ol / 2
	if(ay < wy) {
	    ret[1] = ol / 2.0;
	    ret[3] = 0d;	    
	}
	// north of the wumpus, the wumpus will move south with probability ol / 2
	else if (ay > wy) {
	    ret[1] = 0d;
	    ret[3] = ol  / 2.0;	    
	}
	// same row than the wumpus, the wumpus will move north or south with probability ol / 4
	else if(ay == wy){
	    ret[1] = ol / 4.0;
	    ret[3] = ol / 4.0;	    
	}
	// east of the wumpus, the wumpus will move west with probability ol / 2
	if(ax > wx) {
	    ret[2] = 0d;
	    ret[4] = ol / 2.0;
	}
	// west of the opponent, the wumpus will move east with probability ol / 2
	else if (ax < wx) {
	    ret[2] = ol / 2.0;
	    ret[4] = 0d;
	}
	// same column of the wumpus, the wumpus will move west or east with probability ol / 4
	else if(ax == wx) {
	    ret[2] = ol / 4.0;
	    ret[4] = ol / 4.0;
	}

 	// illegal moves into probabilities that the wumpus does not move
	if (gp.wallAhead(wpos, CatchGen.Direction.N)) {ret[0]+=ret[1]; ret[1]=0d;}
	if (gp.wallAhead(wpos, CatchGen.Direction.S)) {ret[0]+=ret[3]; ret[3]=0d;}
	if (gp.wallAhead(wpos, CatchGen.Direction.E)) {ret[0]+=ret[2]; ret[2]=0d;}
	if (gp.wallAhead(wpos, CatchGen.Direction.W)) {ret[0]+=ret[4]; ret[4]=0d;}

	return ret;
    } // getActDist
} // OmniWumpus