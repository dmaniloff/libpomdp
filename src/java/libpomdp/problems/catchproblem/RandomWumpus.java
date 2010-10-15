/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: RandomWumpus.java
 * Description: Random-moving wumpus with bouncing off the walls of the world.
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.problems.catchproblem;

public class RandomWumpus implements Wumpus {

    // ------------------------------------------------------------------------
    // properties
    // ------------------------------------------------------------------------

    CatchGridProperties gp;

    // constructor
    public RandomWumpus(CatchGridProperties gp) {
	this.gp = gp;
    }

    // returns a vector with the action distribution of the wumpus
    // (0-none, 1-north, 2-east, 3-south, 4-west)
    // the wumpus makes decisions based on its
    // own location only - it does not use knowledge of the walls in the env
    public double[] getActDist(int apos, int wpos) {	
	
	double ret[]    = new double[5];

	// unifrorm random action distribution
	for (int i=0; i<5; i++) ret[i] = .2d;

 	// illegal moves into probabilities that the wumpus does not move - bouncing
	if (gp.wallAhead(wpos, CatchGen.Direction.N)) {ret[0]+=ret[1]; ret[1]=0d;}
	if (gp.wallAhead(wpos, CatchGen.Direction.S)) {ret[0]+=ret[3]; ret[3]=0d;}
	if (gp.wallAhead(wpos, CatchGen.Direction.E)) {ret[0]+=ret[2]; ret[2]=0d;}
	if (gp.wallAhead(wpos, CatchGen.Direction.W)) {ret[0]+=ret[4]; ret[4]=0d;}

	return ret;
    } // getActDist
    
} // RandomWumpus