/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CatchTagGrid.java
 * Description: Defines wallAhead according to Pineau's T-shaped Tag POMDP
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 *
 * -----------------------------------------
 * |   |   |   |   |   |   |   |   |   |   |
 * -----------------------------------------
 * |   |   |   |   |   |   |   |   |   |   |
 * -----------------------------------------
 * |XXX|XXX|XXX|XXX|XXX|   |   |   |XXX|XXX|
 * -----------------------------------------
 * |XXX|XXX|XXX|XXX|XXX|   |   |   |XXX|XXX|
 * -----------------------------------------
 * |XXX|XXX|XXX|XXX|XXX|   |   |   |XXX|XXX|
 * -----------------------------------------
 * |   |   |   |   |   |   |   |   |   |   |
 * -----------------------------------------
 * | W | A |   |   |   |   |   |   |   |   |
 * -----------------------------------------
 --------------------------------------------------------------------------- */

package libpomdp.problems.catchproblem.java;

public class CatchTagHGrid implements CatchGridProperties {
    
    // class properties
    public int WIDTH;
    public int HEIGHT;
    // grid size
    private int N;

    // constructor
    // this expects to receive 10 x 7 
    public CatchTagHGrid(int w, int h) {
	this.WIDTH  = w;
	this.HEIGHT = h;
	this.N      = WIDTH * HEIGHT;
    }

    // is there a wall in the move direction of position?
    // implemented according to Pineau's T-shaped tag environment
    public boolean wallAhead(int position, CatchGen.Direction move) {
	// get x and y
	int x=getxy(position)[0]; int y=getxy(position)[1];
	switch (move) {
	case N:
	    return ((HEIGHT-1 == y) || (y==1 && (x<=4 || x>=8)));
	case S:
	    return ((y==0)          || (y==5 && (x<=4 || x>=8)));
	case E:
	    return (WIDTH-1 == x)   || (x==7 && (y>=2 && y<=4));	    
	case W:
	    return (x==0            || (x==5 && (y>=2 && y<=4)));
	}	
	// never reachable
	return false;
    }

    public boolean isLegalPosition(int position) {
	// get x and y
	int x=getxy(position)[0]; int y=getxy(position)[1];
	return !((y >= 2 && y <=4) && (x <= 4 || x>=8));
    }

    public int totPossibleLocations() {
	int totlocs = 0;
	for (int c=0; c<N; c++) if (isLegalPosition(c)) totlocs++;
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