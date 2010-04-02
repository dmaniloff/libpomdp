/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: dotpomdpMTJ.g
 * Description: ANTLRv3 grammar specification to parse a .POMDP file in
 *              Cassandra's format. Not all features are supported yet.
 *              Sparse matrices and arrays use the MTJ matrix package.
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

grammar dotpomdpMTJ;


/*------------------------------------------------------------------
 * TOKENS
 *------------------------------------------------------------------*/
tokens {
    DISCOUNTTOK     = 'discount' ;
    VALUESTOK       = 'values' ;
    STATESTOK       = 'states' ;
    ACTIONSTOK      = 'actions' ;
    OBSERVATIONSTOK = 'observations' ;
    TTOK            = 'T' ;
    OTOK            = 'O' ;
    RTOK            = 'R' ;
    UNIFORMTOK      = 'uniform' ;
    IDENTITYTOK     = 'identity' ;
    REWARDTOK       = 'reward' ; 
    COSTTOK         = 'cost' ;
    STARTTOK        = 'start' ;
    INCLUDETOK      = 'include' ; 
    EXCLUDETOK      = 'exclude' ;
    RESETTOK        = 'reset' ;
    COLONTOK        = ':' ;
    ASTERICKTOK     = '*' ;
    PLUSTOK         = '+' ;
    MINUSTOK        = '-' ;
}

/*------------------------------------------------------------------
 * PARSER INITIALIZATIONS
 *------------------------------------------------------------------*/
@header {
    // we're using mtj to store the data
    import no.uib.cipr.matrix.*;    
    import no.uib.cipr.matrix.sparse.*;   
}

@members {
    // main method
    public static void main(String[] args) throws Exception {
        dotpomdpLexer lex = new dotpomdpLexer(new ANTLRFileStream(args[0]));
       	CommonTokenStream tokens = new CommonTokenStream(lex);
        dotpomdpParser parser = new dotpomdpParser(tokens);

        try {
            parser.dotpomdp();
        } catch (RecognitionException e)  {
            e.printStackTrace();
        }
    }

    // main structure
    private pomdpSpecSparseMTJ dotpomdpSpec = new pomdpSpecSparseMTJ();

    // threshold for sums of distros
    final double THRESHOLD = 1e-5;
    // return main structure
    public pomdpSpecSparseMTJ getSpec() {
        return dotpomdpSpec;
    }

    // simple debug mesg
    private void err(String msg) {
        System.err.println(msg);
    }
}

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/
STRING  
    :   ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*
    ;

INT     
    :   '0' | ('1'..'9') ('0'..'9')*
    ;

FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

COMMENT
    :   '#' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;} // can also use skip()?
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;


fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
dotpomdp
    : preamble
        {
            // we can now initialize the data structures for T, O, R
            System.out.println("Successfully parsed preamble");            
            /* initialize |A| s x s' dense matrices (they're actually sparse)
               T: <action> : <start-state> : <end-state> prob  */
            dotpomdpSpec.T = new DenseMatrix[dotpomdpSpec.nrAct];
            for(int a=0; a<dotpomdpSpec.nrAct; a++) 
                dotpomdpSpec.T[a] = new DenseMatrix(dotpomdpSpec.nrSta,
                                                    dotpomdpSpec.nrSta);
            /* initialize |A| s' x o dense matrices (they're actually sparse)
               O : <action> : <end-state> : <observation> prob */        
            dotpomdpSpec.O = new DenseMatrix[dotpomdpSpec.nrAct];
            for(int a=0; a<dotpomdpSpec.nrAct; a++) 
                dotpomdpSpec.O[a] = new DenseMatrix(dotpomdpSpec.nrSta,
                                                    dotpomdpSpec.nrObs);
            /* initialize |A| 1 x s' sparse vectors
               R: <action> : <start-state> : * : * float */
            dotpomdpSpec.R = new SparseVector[dotpomdpSpec.nrAct];
            for(int a=0; a<dotpomdpSpec.nrAct; a++) 
                dotpomdpSpec.R[a] = new SparseVector(dotpomdpSpec.nrSta);        
        }
      start_state 
        {
            // make sure the start state is a distribution
            System.out.println("Successfully parsed start state");
            if (dotpomdpSpec.startState.norm(Vector.Norm.One) - 1.0 > THRESHOLD)
                err("Start state not a distribution" + dotpomdpSpec.startState.norm(Vector.Norm.One));
        }
      param_list 
        {
            // there should be a check for the parameter distros here...
            System.out.println("Successfully parsed parameters");
            
        }
    ;

preamble        
    : param_type*        
    ;

param_type      
    : discount_param
    | value_param
    | state_param
    | action_param
    | obs_param
    ;


discount_param  
    : DISCOUNTTOK COLONTOK FLOAT
      {// set discount factor in global problem struct
       dotpomdpSpec.discount = Double.parseDouble($FLOAT.text);}
    ;

value_param     
    : VALUESTOK COLONTOK value_tail
    ;

value_tail      
    : REWARDTOK
    | COSTTOK
    ;

state_param     
    : STATESTOK COLONTOK state_tail
    ;

state_tail      
    : INT
        // we only get the total # of states
        {dotpomdpSpec.nrSta   = Integer.parseInt($INT.text);}
    | ident_list
        // we get a list of states, convert to array
        {dotpomdpSpec.staList = $ident_list.list;
         dotpomdpSpec.nrSta   = dotpomdpSpec.staList.size();}
    ;

action_param     
    : ACTIONSTOK COLONTOK action_tail
    ;

action_tail      
    : INT
        // we only get the total # of actions
        {dotpomdpSpec.nrAct   = Integer.parseInt($INT.text);}
    | ident_list
        // we get a list of actions
        {dotpomdpSpec.actList = (ArrayList) $ident_list.list;
         dotpomdpSpec.nrAct   = dotpomdpSpec.actList.size();}
    ;

obs_param 
    : OBSERVATIONSTOK COLONTOK obs_param_tail
    ;

obs_param_tail 
    : INT
        // we only get the total # of observations
        {dotpomdpSpec.nrObs   = Integer.parseInt($INT.text);}
    | ident_list
        // we get a list of observations
        {dotpomdpSpec.obsList = (ArrayList) $ident_list.list;
         dotpomdpSpec.nrObs   = dotpomdpSpec.obsList.size();}
    ;

start_state     
    : STARTTOK COLONTOK prob_vector
        // we'll focus on this case for now, just a sparse vector
        {
            System.out.println("ENTERED the first case for start state");
            dotpomdpSpec.startState = $prob_vector.vector;
        }
    | STARTTOK COLONTOK STRING
        {err("unsopported feature");}
    | STARTTOK INCLUDETOK COLONTOK start_state_list
         {err("unsopported feature");}
    | STARTTOK EXCLUDETOK COLONTOK start_state_list
         {err("unsopported feature");}
    |  /* empty */
    ;

start_state_list    
    : state+
    ;

param_list     
    : param_spec*
    ;

param_spec     
    : trans_prob_spec
    | obs_prob_spec 
    | reward_spec
    ;

trans_prob_spec     
    : TTOK COLONTOK trans_spec_tail
    ;

trans_spec_tail     
    : paction COLONTOK s_1=state COLONTOK s_2=state prob // this would not detect probs>1
        // triple loop with lists, might want to add a check for zeros here
        {
            //if($prob.p > 0.0)  this causes MORE entries to exist - don't know why yet
                for(int a : $paction.l)
                    for(int s1 : $s_1.l)
                        for(int s2 : $s_2.l)
                            dotpomdpSpec.T[a].set(s1, s2, $prob.p);
        }
    | paction COLONTOK state u_matrix 
        {err("unsopported feature");}
    | paction ui_matrix
        // full matrix specification, set if for each action 
        {for(int a : $paction.l) dotpomdpSpec.T[a] = $ui_matrix.m;}
    ;

obs_prob_spec  
    : OTOK COLONTOK obs_spec_tail
    ;

obs_spec_tail  
    : paction COLONTOK state COLONTOK obs prob
        // triple loop with lists, might want to add a check for zeros here
        {
            for(int a : $paction.l)
                for(int s2 : $state.l)
                    for(int o : $obs.l)
                        dotpomdpSpec.O[a].set(s2, o, $prob.p);
        }
    | paction COLONTOK state u_matrix
        {err("unsopported feature");}
    | paction u_matrix
        {err("unsopported feature");}
    ;

reward_spec    
    : RTOK COLONTOK reward_spec_tail
    ;

reward_spec_tail 
    : paction COLONTOK s_1=state COLONTOK s_2=state COLONTOK obs number 
        // for this case, we will only allow R(s,a) type rewards
        {            
            if($s_2.text.compareTo(Character.toString('*'))!=0 || 
                        $obs.text.compareTo(Character.toString('*'))!=0){
                err("We only allow for R(s,a) type rewards for now...");
                //System.out.println($s_2.text + $obs.text);
            }
            //System.out.println("number is"+$number.n);
            for(int a : $paction.l)
                for(int s1 : $s_1.l) 
                    dotpomdpSpec.R[a].set(s1, $number.n);                   
        }
    | paction COLONTOK state COLONTOK state num_matrix
        {err("unsopported feature");}
    | paction COLONTOK state num_matrix
        {err("unsopported feature");}
    ;

ui_matrix returns [DenseMatrix m]     
    : UNIFORMTOK 
    | IDENTITYTOK 
        {$m = Matrices.identity(dotpomdpSpec.nrSta);}
    | prob_matrix
    ;

u_matrix  
    : UNIFORMTOK 
    | RESETTOK
    | prob_matrix
    ;

prob_matrix    
    : prob+
    ;

prob_vector returns [SparseVector vector]
    : 
        // initialization here is OK
        {int index = 0; $vector = new SparseVector(dotpomdpSpec.nrSta);} 
        (prob 
        {
            // action here - the check for 0 actually doesn't matter
            if ($prob.p > 0.0) $vector.set(index, $prob.p);
            index++;
        }
        )+
    ;

num_matrix     
    : number+
    ;

state returns [ArrayList<Integer> l = new ArrayList<Integer>()]
    :
        INT 
        {$l.add(Integer.parseInt($INT.text));}
    | 
        STRING
        {$l.add(dotpomdpSpec.staList.indexOf($STRING.text));}
    | 
        ASTERICKTOK
        {for(int s=0; s<dotpomdpSpec.nrSta; s++) $l.add(s);}
    ;   

paction returns [ArrayList<Integer> l = new ArrayList<Integer>()]
    :
        INT 
        {$l.add(Integer.parseInt($INT.text));}
    | 
        STRING
        {$l.add(dotpomdpSpec.actList.indexOf($STRING.text));}
    | 
        ASTERICKTOK
        {for(int a=0; a<dotpomdpSpec.nrAct; a++) $l.add(a);}
    ;

obs returns [ArrayList<Integer> l = new ArrayList<Integer>()]    
    :
        INT 
        {$l.add(Integer.parseInt($INT.text));}
    | 
        STRING
        {$l.add(dotpomdpSpec.obsList.indexOf($STRING.text));}
    | 
        ASTERICKTOK
        {for(int o=0; o<dotpomdpSpec.nrObs; o++) $l.add(o);}
    ;

ident_list returns [ArrayList<String> list]     
    : 
        {$list = new ArrayList<String>();}
        (STRING
        {$list.add($STRING.text);}
        )+
    ;

prob returns [double p]      
    : INT
        {$p = Double.parseDouble($INT.text);}
    | FLOAT
        {$p = Double.parseDouble($FLOAT.text);}  
    ;

number returns [double n]          
    : optional_sign INT
        {$n = $optional_sign.s * Double.parseDouble($INT.text);} 
    | optional_sign FLOAT
        {$n = $optional_sign.s * Double.parseDouble($FLOAT.text);} 
    ;

optional_sign returns [int s]
    : PLUSTOK
        {$s = 1;}
    | MINUSTOK
        {$s = -1;}
    |  /* empty */
        {$s = 1;}
    ;
