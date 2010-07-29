/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: CatchGraph.java
 * Description: simple class to graph the state of a catch problem
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.problems.catchproblem;

public class CatchGraph {
    
    // instantiate empty generator to acces some of its methods
    CatchGridProperties gp;
    int WIDTH;
    int HEIGHT;

    // constructor
    public CatchGraph(int w, int h, CatchGridProperties gp) {
	this.gp  = gp;
	this.WIDTH = w;
	this.HEIGHT = h;
    }

    // draw the world
    public void drawState(int factoredS[][]) {
	int apos = factoredS[1][0] - 1;
	int ax   = getxy(apos)[0]; int ay = getxy(apos)[1];
	int wpos = factoredS[1][1] - 1;
	int wx   = getxy(wpos)[0]; int wy = getxy(wpos)[1];

	boolean fr;
	// start with a line
	for(int f=0; f<WIDTH; f++) System.out.print("----");
	System.out.println("-");
	
	for(int d=HEIGHT-1; d>=0; d--) {	    
	    for(int c=0; c<WIDTH; c++) {
		System.out.print("|");
		if (c==ax && d==ay)
		    System.out.print(" A ");
		else if (c==wx && d==wy)
		    System.out.print(" W ");
		else if (!gp.isLegalPosition(getpos(c,d)))
		    System.out.print("XXX");
		else
		    System.out.print("   ");		
	    }
	    System.out.println("|");
	    // print line here
	    for(int f=0; f<WIDTH; f++) System.out.print("----");
	    System.out.println("-");
	}
    } // drawWorld

    // convert (x,y) coordinates into integer position
    private int getpos(int x, int y) {
	return y * WIDTH + x;	
    }
    
    // convert absolute position to (x,y) coordinates
    // xy[0] contains x coordinate
    // xy[1] contains y coordinate
    private int[] getxy(int pos) {
	int xy[] = new int[2];
	// compute row and col
	xy[0] = pos % WIDTH;
	xy[1] = pos / WIDTH;
	// return
	return xy;
    }
    
} // CatchGraph