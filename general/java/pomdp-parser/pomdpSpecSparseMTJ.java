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

// imports
import java.util.*;
import java.io.*;
import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.sparse.*;

public class pomdpSpecSparseMTJ implements Serializable{

    // serial id
    static final long serialVersionUID = 1L;

    // discount factor
    public double discount;
    
    // number of states
    public int nrSta;
    
    // state list in case given as such
    public ArrayList staList;
    
    // number of actions
    public int nrAct;
    
    // action list in case given as such
    public ArrayList actList;
    
    // number of observations
    public int nrObs;
    
    // list of observations in case given as such
    public ArrayList obsList;
    
    // start state
    public SparseVector startState;

    // transition matrices - a x s x s'
    // T: <action> : <start-state> : <end-state> %f
    public DenseMatrix T[];

    // observation matrices - a x s' x o
    // O : <action> : <end-state> : <observation> %f
    public DenseMatrix O[];

    // reward vectors - a x s
    // R: <action> : <start-state> : * : * %f
    public SparseVector R[];

}