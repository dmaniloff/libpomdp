/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: PomdpSpecStandard.java
 * Description: Object that contains all .POMDP file params
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * Copyright (c) 2010 Mauricio Araya
 --------------------------------------------------------------------------- */

package libpomdp.parser.java;

// imports
import java.io.Serializable;
import java.util.ArrayList;

import libpomdp.common.java.CustomMatrix;
import libpomdp.common.java.CustomVector;

public class PomdpSpecStd implements Serializable{

    // serial id
    static final long serialVersionUID = 1L;

    // R(a), R(s,a), R(s,a,s'), R(s,a,s',o')
    public enum RewardType { 
    	R_s_a(1), R_s_a_sp(2), R_s_a_sp_op(3);
    	private final int _p;
    	private RewardType(int p) { _p = p; }
    	public int getP() { return _p; }
    }
    
    // the reward representation used
    public RewardType rewardType;
    
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
    
    // R(s,a,s)
    public CustomMatrix partialR[];
    
    // R(s,a,s',o')
    public CustomMatrix fullR[][];
    
} // PomdpSpecStandard
