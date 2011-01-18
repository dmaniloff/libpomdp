/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CatchRectangularGrid.java
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 --------------------------------------------------------------------------- */

package libpomdp.problems.catchproblem;

public class CatchRectangularGrid implements CatchGridProperties {

    // class properties
    private int WIDTH;
    private int HEIGHT;
    // grid size
    private int N;

    // constructor
    public CatchRectangularGrid(int w, int h) {
	this.WIDTH = w;
	this.HEIGHT = h;
	this.N = WIDTH * HEIGHT;
    }

    // is there a wall in the move direction of position?
    // implemented for a simple rectangular grid
    public boolean wallAhead(int position, CatchGen.Direction move) {
	// get x and y
	int x = getxy(position)[0];
	int y = getxy(position)[1];
	switch (move) {
	case N:
	    return (HEIGHT - 1 == y);
	case S:
	    return (0 == y);
	case E:
	    return (WIDTH - 1 == x);
	case W:
	    return (0 == x);
	}
	// never reachable
	return false;
    }

    // in a rectangular configuration, all positions are legal
    public boolean isLegalPosition(int position) {
	return true;
    }

    public int totPossibleLocations() {
	int totlocs = 0;
	for (int c = 0; c < N; c++)
	    if (isLegalPosition(c))
		totlocs++;
	return totlocs;
    }

    // convert absolute position to (x,y) coordinates
    // xy[0] contains x coordinate
    // xy[1] contains y coordinate
    public int[] getxy(int pos) {
	int xy[] = new int[2];
	// compute row and col
	xy[0] = pos % WIDTH;
	xy[1] = pos / WIDTH;
	// return
	return xy;
    }
}