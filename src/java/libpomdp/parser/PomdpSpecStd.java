/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: pomdpSpecSparse.java
 * Description: Object that contains all .POMDP file params
 *              It is returned by dotpomdpParserSparseMTJ
 *              Uses MTJ for sparse matrices and vectors
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

package libpomdp.parser;

// imports
import java.io.Serializable;
import java.util.ArrayList;

import libpomdp.common.CustomMatrix;
import libpomdp.common.CustomVector;

public class PomdpSpecStd implements Serializable{

    // serial id
    static final long serialVersionUID = 1L;
    
    public boolean compReward;
    
    // discount factor
    public double discount;
    
    // number of states
    public int nrSta;
    
    // state list in case given as such
    public ArrayList<String> staList;
    
    // number of actions
    public int nrAct;
    
    // action list in case given as such
    public ArrayList<String> actList;
    
    // number of observations
    public int nrObs;
    
    // list of observations in case given as such
    public ArrayList<String> obsList;
    
    // start state
    public CustomVector startState;

    // transition matrices - a x s x s'
    // T: <action> : <start-state> : <end-state> %f
    public CustomMatrix T[];

    // observation matrices - a x s' x o
    // O : <action> : <end-state> : <observation> %f
    public CustomMatrix O[];

    // reward vectors - a x s
    // R: <action> : <start-state> : * : * %f
    public CustomVector R[];
    
    public CustomMatrix fullR[][];
    
    
    

}