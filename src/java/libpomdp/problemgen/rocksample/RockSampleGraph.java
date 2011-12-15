/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: rocksampleGraph.java
 * Description: simple class to graph the state of a rocksample problem
 * Copyright (c) 2009, 2010 Diego Maniloff
 --------------------------------------------------------------------------- */

package libpomdp.problemgen.rocksample;

import libpomdp.problemgen.StateDrawer;

public class RockSampleGraph implements StateDrawer {

    final int n;
    final int k[][];

    public RockSampleGraph(final int n, final int k[][]) {
        this.n = n;
        this.k = k;
    }

    // draw the world
    public void drawState(int factoredS[][]) {
	int c, d, r;
	boolean fr;
	// start with a line
	for(int f=0; f<n; f++) System.out.print("-----");
	System.out.println("------");

	for(d=n-1; d>=0; d--) {
	    for(c=0; c<n+1; c++) {
		System.out.print("|");
		if (c==factoredS[1][0] - 1 &&
		    d==factoredS[1][1] - 1)
		    System.out.print(" A");
		else
		    System.out.print("  ");
		fr = false;
		for(r=0; r<k.length; r++) {
		    if (c==k[r][0] && d==k[r][1]) {
			if (factoredS[1][2+r]==1)
			    System.out.print("gr");
			else
			    System.out.print("br");
			fr = true;
		    }
		}
		    if(!fr)
			System.out.print("  ");

		    //System.out.print(" |");
	    }
	    System.out.println(" |");
	    // print line here
	    for(int f=0; f<n; f++) System.out.print("-----");
	    System.out.println("------");

	}
    } // drawWorld

} // rocksampleGraph