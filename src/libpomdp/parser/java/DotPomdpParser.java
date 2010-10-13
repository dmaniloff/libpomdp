// $ANTLR 3.0.1 /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g 2010-10-13 11:11:34

    package libpomdp.parser.java;
    import libpomdp.common.java.Utils;
    import libpomdp.common.java.CustomVector;
    import libpomdp.common.java.CustomMatrix;  
  


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: DotPomdp.g
 * Description: ANTLRv3 grammar specification to parse a .POMDP file in
 *              Cassandra's format. Not all features are supported yet.
 *              Sparse matrices and arrays use the MTJ matrix package.
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */
public class DotPomdpParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "DISCOUNTTOK", "VALUESTOK", "STATESTOK", "ACTIONSTOK", "OBSERVATIONSTOK", "TTOK", "OTOK", "RTOK", "UNIFORMTOK", "IDENTITYTOK", "REWARDTOK", "COSTTOK", "STARTTOK", "INCLUDETOK", "EXCLUDETOK", "RESETTOK", "COLONTOK", "ASTERICKTOK", "PLUSTOK", "MINUSTOK", "STRING", "INT", "EXPONENT", "FLOAT", "COMMENT", "WS"
    };
    public static final int IDENTITYTOK=13;
    public static final int EXPONENT=26;
    public static final int ACTIONSTOK=7;
    public static final int OTOK=10;
    public static final int COSTTOK=15;
    public static final int STARTTOK=16;
    public static final int DISCOUNTTOK=4;
    public static final int MINUSTOK=23;
    public static final int FLOAT=27;
    public static final int INT=25;
    public static final int VALUESTOK=5;
    public static final int INCLUDETOK=17;
    public static final int EOF=-1;
    public static final int OBSERVATIONSTOK=8;
    public static final int ASTERICKTOK=21;
    public static final int WS=29;
    public static final int EXCLUDETOK=18;
    public static final int TTOK=9;
    public static final int COLONTOK=20;
    public static final int PLUSTOK=22;
    public static final int UNIFORMTOK=12;
    public static final int REWARDTOK=14;
    public static final int COMMENT=28;
    public static final int RTOK=11;
    public static final int STATESTOK=6;
    public static final int STRING=24;
    public static final int RESETTOK=19;

        public DotPomdpParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g"; }


        // main method
        public static void main(String[] args) throws Exception {
            DotPomdpLexer lex = new DotPomdpLexer(new ANTLRFileStream(args[0]));
           	CommonTokenStream tokens = new CommonTokenStream(lex);
            DotPomdpParser parser = new DotPomdpParser(tokens);

            try {
                parser.dotPomdp();
            } catch (RecognitionException e)  {
                e.printStackTrace();
            }
        }

    	
    	private int matrixContext;
    	
    	private static final int MC_TRANSITION = 0;
    	private static final int MC_TRANSITION_ROW = 1;
    	private static final int MC_OBSERVATION = 2;
    	private static final int MC_OBSERVATION_ROW = 3;
        // main structure
        private PomdpSpecStandard dotPomdpSpec = new PomdpSpecStandard();

        // threshold for sums of distros
        final double THRESHOLD = 1e-5;

        // return main structure
        public PomdpSpecStandard getSpec() {
            return dotPomdpSpec;
        }

        // simple debug mesg
        private void err(String msg) {
            System.err.println(msg);
        }



    // $ANTLR start dotPomdp
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:135:1: dotPomdp : preamble start_state param_list ;
    public final void dotPomdp() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:136:5: ( preamble start_state param_list )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:137:9: preamble start_state param_list
            {

                  		System.out.println("PARSER: Parsing preamble...");
                    
            pushFollow(FOLLOW_preamble_in_dotPomdp796);
            preamble();
            _fsp--;


                    	dotPomdpSpec.compReward=true;
                    	System.out.println("PARSER: Summary -> states "+dotPomdpSpec.nrSta);
                    	System.out.println("                -> observations "+dotPomdpSpec.nrObs);
                    	System.out.println("                -> actions "+dotPomdpSpec.nrAct);
                        // we can now initialize the data structures for T, O, R
                        /* initialize |A| s x s' dense matrices (they're actually sparse)
                           T: <action> : <start-state> : <end-state> prob  */
                        dotPomdpSpec.T = new CustomMatrix[dotPomdpSpec.nrAct];
                        for(int a=0; a<dotPomdpSpec.nrAct; a++) 
                            dotPomdpSpec.T[a] = new CustomMatrix(dotPomdpSpec.nrSta,
                                                                dotPomdpSpec.nrSta);
                        /* initialize |A| s' x o dense matrices (they're actually sparse)
                           O : <action> : <end-state> : <observation> prob */        
                        dotPomdpSpec.O = new CustomMatrix[dotPomdpSpec.nrAct];
                        for(int a=0; a<dotPomdpSpec.nrAct; a++) 
                            dotPomdpSpec.O[a] = new CustomMatrix(dotPomdpSpec.nrSta,
                                                                dotPomdpSpec.nrObs);
                        /* initialize |A| 1 x s' sparse vectors (comp reward)
                           R: <action> : <start-state> : * : * float */
                           dotPomdpSpec.R = new CustomVector[dotPomdpSpec.nrAct];
                           for(int a=0; a<dotPomdpSpec.nrAct; a++)
                           dotPomdpSpec.R[a] = new CustomVector(dotPomdpSpec.nrSta); 
                           System.out.println("PARSER: Parsing starting state/belief...");   
                    
            pushFollow(FOLLOW_start_state_in_dotPomdp814);
            start_state();
            _fsp--;


                        // make sure the start state is a distribution
                        
                        //System.out.println("Successfully parsed start state");
                        if (dotPomdpSpec.startState.norm(1.0) - 1.0 > THRESHOLD)
                            err("Start state not a distribution" + dotPomdpSpec.startState.norm(1));
                        System.out.println("PARSER: Parsing parameters...");
                    
            pushFollow(FOLLOW_param_list_in_dotPomdp833);
            param_list();
            _fsp--;


                        // there should be a check for the parameter distros here...
                        // System.out.println("Successfully parsed parameters");
                        if (dotPomdpSpec.compReward==false){
                        	System.out.println("PARSER: Compressing rewards...");
                        	//Create the R(a,s) type of reward (not very efficient, but only one time)
            				for (int a=0;a<dotPomdpSpec.nrAct;a++){
            					//R[a]=new CustomVector(dotPomdpSpec.nrSta);
            					for (int s=0;s<dotPomdpSpec.nrSta;s++){
            						CustomMatrix prod=new CustomMatrix(dotPomdpSpec.nrSta,dotPomdpSpec.nrSta);
            						prod=dotPomdpSpec.O[a].transBmult(dotPomdpSpec.fullR[a][s]);
            						double value=0;
            						for (int sp=0;sp<dotPomdpSpec.nrSta;sp++){
            							value+=prod.get(sp,sp)*dotPomdpSpec.T[a].get(s, sp);
            						}
            						dotPomdpSpec.R[a].set(s,value);
            					}
            				}
                        }
                        System.out.println("PARSER: [DONE]");
                        
                    

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end dotPomdp


    // $ANTLR start preamble
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:200:1: preamble : ( param_type )* ;
    public final void preamble() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:201:5: ( ( param_type )* )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:201:7: ( param_type )*
            {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:201:7: ( param_type )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=DISCOUNTTOK && LA1_0<=OBSERVATIONSTOK)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:201:7: param_type
            	    {
            	    pushFollow(FOLLOW_param_type_in_preamble869);
            	    param_type();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end preamble


    // $ANTLR start param_type
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:204:1: param_type : ( discount_param | value_param | state_param | action_param | obs_param );
    public final void param_type() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:205:5: ( discount_param | value_param | state_param | action_param | obs_param )
            int alt2=5;
            switch ( input.LA(1) ) {
            case DISCOUNTTOK:
                {
                alt2=1;
                }
                break;
            case VALUESTOK:
                {
                alt2=2;
                }
                break;
            case STATESTOK:
                {
                alt2=3;
                }
                break;
            case ACTIONSTOK:
                {
                alt2=4;
                }
                break;
            case OBSERVATIONSTOK:
                {
                alt2=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("204:1: param_type : ( discount_param | value_param | state_param | action_param | obs_param );", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:205:7: discount_param
                    {
                    pushFollow(FOLLOW_discount_param_in_param_type901);
                    discount_param();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:206:7: value_param
                    {
                    pushFollow(FOLLOW_value_param_in_param_type909);
                    value_param();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:207:7: state_param
                    {
                    pushFollow(FOLLOW_state_param_in_param_type917);
                    state_param();
                    _fsp--;


                    }
                    break;
                case 4 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:208:7: action_param
                    {
                    pushFollow(FOLLOW_action_param_in_param_type925);
                    action_param();
                    _fsp--;


                    }
                    break;
                case 5 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:209:7: obs_param
                    {
                    pushFollow(FOLLOW_obs_param_in_param_type933);
                    obs_param();
                    _fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end param_type


    // $ANTLR start discount_param
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:213:1: discount_param : DISCOUNTTOK COLONTOK FLOAT ;
    public final void discount_param() throws RecognitionException {
        Token FLOAT1=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:214:5: ( DISCOUNTTOK COLONTOK FLOAT )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:214:7: DISCOUNTTOK COLONTOK FLOAT
            {
            match(input,DISCOUNTTOK,FOLLOW_DISCOUNTTOK_in_discount_param953); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_discount_param955); 
            FLOAT1=(Token)input.LT(1);
            match(input,FLOAT,FOLLOW_FLOAT_in_discount_param957); 
            // set discount factor in global problem struct
                   dotPomdpSpec.discount = Double.parseDouble(FLOAT1.getText());

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end discount_param


    // $ANTLR start value_param
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:219:1: value_param : VALUESTOK COLONTOK value_tail ;
    public final void value_param() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:220:5: ( VALUESTOK COLONTOK value_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:220:7: VALUESTOK COLONTOK value_tail
            {
            match(input,VALUESTOK,FOLLOW_VALUESTOK_in_value_param987); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_value_param989); 
            pushFollow(FOLLOW_value_tail_in_value_param991);
            value_tail();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end value_param


    // $ANTLR start value_tail
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:223:1: value_tail : ( REWARDTOK | COSTTOK );
    public final void value_tail() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:224:5: ( REWARDTOK | COSTTOK )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==REWARDTOK) ) {
                alt3=1;
            }
            else if ( (LA3_0==COSTTOK) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("223:1: value_tail : ( REWARDTOK | COSTTOK );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:224:7: REWARDTOK
                    {
                    match(input,REWARDTOK,FOLLOW_REWARDTOK_in_value_tail1014); 

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:225:7: COSTTOK
                    {
                    match(input,COSTTOK,FOLLOW_COSTTOK_in_value_tail1022); 
                    err("PARSER: Costs are not supported... sure that you want to use costs?");

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end value_tail


    // $ANTLR start state_param
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:229:1: state_param : STATESTOK COLONTOK state_tail ;
    public final void state_param() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:230:5: ( STATESTOK COLONTOK state_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:230:7: STATESTOK COLONTOK state_tail
            {
            match(input,STATESTOK,FOLLOW_STATESTOK_in_state_param1055); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_state_param1057); 
            pushFollow(FOLLOW_state_tail_in_state_param1059);
            state_tail();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end state_param


    // $ANTLR start state_tail
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:233:1: state_tail : ( INT | ident_list );
    public final void state_tail() throws RecognitionException {
        Token INT2=null;
        ArrayList<String> ident_list3 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:234:5: ( INT | ident_list )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==INT) ) {
                alt4=1;
            }
            else if ( (LA4_0==STRING) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("233:1: state_tail : ( INT | ident_list );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:234:7: INT
                    {
                    INT2=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_state_tail1082); 
                    dotPomdpSpec.nrSta   = Integer.parseInt(INT2.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:237:7: ident_list
                    {
                    pushFollow(FOLLOW_ident_list_in_state_tail1109);
                    ident_list3=ident_list();
                    _fsp--;

                    dotPomdpSpec.staList = ident_list3;
                             dotPomdpSpec.nrSta   = dotPomdpSpec.staList.size();

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end state_tail


    // $ANTLR start action_param
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:243:1: action_param : ACTIONSTOK COLONTOK action_tail ;
    public final void action_param() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:244:5: ( ACTIONSTOK COLONTOK action_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:244:7: ACTIONSTOK COLONTOK action_tail
            {
            match(input,ACTIONSTOK,FOLLOW_ACTIONSTOK_in_action_param1150); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_action_param1152); 
            pushFollow(FOLLOW_action_tail_in_action_param1154);
            action_tail();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end action_param


    // $ANTLR start action_tail
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:247:1: action_tail : ( INT | ident_list );
    public final void action_tail() throws RecognitionException {
        Token INT4=null;
        ArrayList<String> ident_list5 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:248:5: ( INT | ident_list )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==INT) ) {
                alt5=1;
            }
            else if ( (LA5_0==STRING) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("247:1: action_tail : ( INT | ident_list );", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:248:7: INT
                    {
                    INT4=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_action_tail1177); 
                    dotPomdpSpec.nrAct   = Integer.parseInt(INT4.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:251:7: ident_list
                    {
                    pushFollow(FOLLOW_ident_list_in_action_tail1204);
                    ident_list5=ident_list();
                    _fsp--;

                    dotPomdpSpec.actList = ident_list5;
                             dotPomdpSpec.nrAct   = dotPomdpSpec.actList.size();

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end action_tail


    // $ANTLR start obs_param
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:257:1: obs_param : OBSERVATIONSTOK COLONTOK obs_param_tail ;
    public final void obs_param() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:258:5: ( OBSERVATIONSTOK COLONTOK obs_param_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:258:7: OBSERVATIONSTOK COLONTOK obs_param_tail
            {
            match(input,OBSERVATIONSTOK,FOLLOW_OBSERVATIONSTOK_in_obs_param1241); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_param1243); 
            pushFollow(FOLLOW_obs_param_tail_in_obs_param1245);
            obs_param_tail();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end obs_param


    // $ANTLR start obs_param_tail
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:261:1: obs_param_tail : ( INT | ident_list );
    public final void obs_param_tail() throws RecognitionException {
        Token INT6=null;
        ArrayList<String> ident_list7 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:262:5: ( INT | ident_list )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==INT) ) {
                alt6=1;
            }
            else if ( (LA6_0==STRING) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("261:1: obs_param_tail : ( INT | ident_list );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:262:7: INT
                    {
                    INT6=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_obs_param_tail1263); 
                    dotPomdpSpec.nrObs   = Integer.parseInt(INT6.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:265:7: ident_list
                    {
                    pushFollow(FOLLOW_ident_list_in_obs_param_tail1290);
                    ident_list7=ident_list();
                    _fsp--;

                    dotPomdpSpec.obsList = ident_list7;
                             dotPomdpSpec.nrObs   = dotPomdpSpec.obsList.size();

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end obs_param_tail


    // $ANTLR start start_state
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:271:1: start_state : ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | );
    public final void start_state() throws RecognitionException {
        CustomVector prob_vector8 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:272:5: ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | )
            int alt7=5;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==STARTTOK) ) {
                switch ( input.LA(2) ) {
                case COLONTOK:
                    {
                    int LA7_3 = input.LA(3);

                    if ( (LA7_3==STRING) ) {
                        alt7=2;
                    }
                    else if ( (LA7_3==INT||LA7_3==FLOAT) ) {
                        alt7=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("271:1: start_state : ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | );", 7, 3, input);

                        throw nvae;
                    }
                    }
                    break;
                case EXCLUDETOK:
                    {
                    alt7=4;
                    }
                    break;
                case INCLUDETOK:
                    {
                    alt7=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("271:1: start_state : ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | );", 7, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA7_0==EOF||(LA7_0>=TTOK && LA7_0<=RTOK)) ) {
                alt7=5;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("271:1: start_state : ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | );", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:272:7: STARTTOK COLONTOK prob_vector
                    {
                    match(input,STARTTOK,FOLLOW_STARTTOK_in_start_state1331); 
                    match(input,COLONTOK,FOLLOW_COLONTOK_in_start_state1333); 
                    pushFollow(FOLLOW_prob_vector_in_start_state1335);
                    prob_vector8=prob_vector();
                    _fsp--;


                                //System.out.println("ENTERED the first case for start state");
                                dotPomdpSpec.startState = prob_vector8;
                            

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:278:7: STARTTOK COLONTOK STRING
                    {
                    match(input,STARTTOK,FOLLOW_STARTTOK_in_start_state1362); 
                    match(input,COLONTOK,FOLLOW_COLONTOK_in_start_state1364); 
                    match(input,STRING,FOLLOW_STRING_in_start_state1366); 
                    err("PARSER: MDPs are not supported yet, only POMDPs");

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:280:7: STARTTOK INCLUDETOK COLONTOK start_state_list
                    {
                    match(input,STARTTOK,FOLLOW_STARTTOK_in_start_state1384); 
                    match(input,INCLUDETOK,FOLLOW_INCLUDETOK_in_start_state1386); 
                    match(input,COLONTOK,FOLLOW_COLONTOK_in_start_state1388); 
                    pushFollow(FOLLOW_start_state_list_in_start_state1390);
                    start_state_list();
                    _fsp--;

                    err("PARSER: Include and exclude features are not supported yet");

                    }
                    break;
                case 4 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:282:7: STARTTOK EXCLUDETOK COLONTOK start_state_list
                    {
                    match(input,STARTTOK,FOLLOW_STARTTOK_in_start_state1409); 
                    match(input,EXCLUDETOK,FOLLOW_EXCLUDETOK_in_start_state1411); 
                    match(input,COLONTOK,FOLLOW_COLONTOK_in_start_state1413); 
                    pushFollow(FOLLOW_start_state_list_in_start_state1415);
                    start_state_list();
                    _fsp--;

                    err("PARSER: Include and exclude features are not supported yet");

                    }
                    break;
                case 5 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:285:6: 
                    {

                        	// Empty start state means uniform belief
                        	dotPomdpSpec.startState=new CustomVector(CustomVector.getUniform(dotPomdpSpec.nrSta));
                        	

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end start_state


    // $ANTLR start start_state_list
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:291:1: start_state_list : ( state )+ ;
    public final void start_state_list() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:292:5: ( ( state )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:292:7: ( state )+
            {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:292:7: ( state )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==ASTERICKTOK||(LA8_0>=STRING && LA8_0<=INT)) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:292:7: state
            	    {
            	    pushFollow(FOLLOW_state_in_start_state_list1463);
            	    state();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end start_state_list


    // $ANTLR start param_list
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:295:1: param_list : ( param_spec )* ;
    public final void param_list() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:296:5: ( ( param_spec )* )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:296:7: ( param_spec )*
            {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:296:7: ( param_spec )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>=TTOK && LA9_0<=RTOK)) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:296:7: param_spec
            	    {
            	    pushFollow(FOLLOW_param_spec_in_param_list1486);
            	    param_spec();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end param_list


    // $ANTLR start param_spec
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:299:1: param_spec : ( trans_prob_spec | obs_prob_spec | reward_spec );
    public final void param_spec() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:300:5: ( trans_prob_spec | obs_prob_spec | reward_spec )
            int alt10=3;
            switch ( input.LA(1) ) {
            case TTOK:
                {
                alt10=1;
                }
                break;
            case OTOK:
                {
                alt10=2;
                }
                break;
            case RTOK:
                {
                alt10=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("299:1: param_spec : ( trans_prob_spec | obs_prob_spec | reward_spec );", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:300:7: trans_prob_spec
                    {
                    pushFollow(FOLLOW_trans_prob_spec_in_param_spec1509);
                    trans_prob_spec();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:301:7: obs_prob_spec
                    {
                    pushFollow(FOLLOW_obs_prob_spec_in_param_spec1517);
                    obs_prob_spec();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:302:7: reward_spec
                    {
                    pushFollow(FOLLOW_reward_spec_in_param_spec1526);
                    reward_spec();
                    _fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end param_spec


    // $ANTLR start trans_prob_spec
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:305:1: trans_prob_spec : TTOK COLONTOK trans_spec_tail ;
    public final void trans_prob_spec() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:306:5: ( TTOK COLONTOK trans_spec_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:306:7: TTOK COLONTOK trans_spec_tail
            {
            match(input,TTOK,FOLLOW_TTOK_in_trans_prob_spec1548); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_trans_prob_spec1550); 
            pushFollow(FOLLOW_trans_spec_tail_in_trans_prob_spec1552);
            trans_spec_tail();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end trans_prob_spec


    // $ANTLR start trans_spec_tail
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );
    public final void trans_spec_tail() throws RecognitionException {
        state_return s_1 = null;

        state_return s_2 = null;

        double prob9 = 0.0;

        ArrayList<Integer> paction10 = null;

        ArrayList<Integer> paction11 = null;

        state_return state12 = null;

        CustomMatrix u_matrix13 = null;

        ArrayList<Integer> paction14 = null;

        CustomMatrix ui_matrix15 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:310:5: ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix )
            int alt11=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA11_6 = input.LA(4);

                        if ( (LA11_6==UNIFORMTOK||LA11_6==RESETTOK||LA11_6==INT||LA11_6==FLOAT) ) {
                            alt11=2;
                        }
                        else if ( (LA11_6==COLONTOK) ) {
                            alt11=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA11_7 = input.LA(4);

                        if ( (LA11_7==UNIFORMTOK||LA11_7==RESETTOK||LA11_7==INT||LA11_7==FLOAT) ) {
                            alt11=2;
                        }
                        else if ( (LA11_7==COLONTOK) ) {
                            alt11=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA11_8 = input.LA(4);

                        if ( (LA11_8==UNIFORMTOK||LA11_8==RESETTOK||LA11_8==INT||LA11_8==FLOAT) ) {
                            alt11=2;
                        }
                        else if ( (LA11_8==COLONTOK) ) {
                            alt11=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 4, input);

                        throw nvae;
                    }

                }
                else if ( ((LA11_1>=UNIFORMTOK && LA11_1<=IDENTITYTOK)||LA11_1==INT||LA11_1==FLOAT) ) {
                    alt11=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 1, input);

                    throw nvae;
                }
                }
                break;
            case STRING:
                {
                int LA11_2 = input.LA(2);

                if ( (LA11_2==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA11_6 = input.LA(4);

                        if ( (LA11_6==UNIFORMTOK||LA11_6==RESETTOK||LA11_6==INT||LA11_6==FLOAT) ) {
                            alt11=2;
                        }
                        else if ( (LA11_6==COLONTOK) ) {
                            alt11=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA11_7 = input.LA(4);

                        if ( (LA11_7==UNIFORMTOK||LA11_7==RESETTOK||LA11_7==INT||LA11_7==FLOAT) ) {
                            alt11=2;
                        }
                        else if ( (LA11_7==COLONTOK) ) {
                            alt11=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA11_8 = input.LA(4);

                        if ( (LA11_8==UNIFORMTOK||LA11_8==RESETTOK||LA11_8==INT||LA11_8==FLOAT) ) {
                            alt11=2;
                        }
                        else if ( (LA11_8==COLONTOK) ) {
                            alt11=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 4, input);

                        throw nvae;
                    }

                }
                else if ( ((LA11_2>=UNIFORMTOK && LA11_2<=IDENTITYTOK)||LA11_2==INT||LA11_2==FLOAT) ) {
                    alt11=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 2, input);

                    throw nvae;
                }
                }
                break;
            case ASTERICKTOK:
                {
                int LA11_3 = input.LA(2);

                if ( (LA11_3==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA11_6 = input.LA(4);

                        if ( (LA11_6==UNIFORMTOK||LA11_6==RESETTOK||LA11_6==INT||LA11_6==FLOAT) ) {
                            alt11=2;
                        }
                        else if ( (LA11_6==COLONTOK) ) {
                            alt11=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA11_7 = input.LA(4);

                        if ( (LA11_7==UNIFORMTOK||LA11_7==RESETTOK||LA11_7==INT||LA11_7==FLOAT) ) {
                            alt11=2;
                        }
                        else if ( (LA11_7==COLONTOK) ) {
                            alt11=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA11_8 = input.LA(4);

                        if ( (LA11_8==UNIFORMTOK||LA11_8==RESETTOK||LA11_8==INT||LA11_8==FLOAT) ) {
                            alt11=2;
                        }
                        else if ( (LA11_8==COLONTOK) ) {
                            alt11=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 4, input);

                        throw nvae;
                    }

                }
                else if ( ((LA11_3>=UNIFORMTOK && LA11_3<=IDENTITYTOK)||LA11_3==INT||LA11_3==FLOAT) ) {
                    alt11=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("309:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:310:7: paction COLONTOK s_1= state COLONTOK s_2= state prob
                    {
                    pushFollow(FOLLOW_paction_in_trans_spec_tail1574);
                    paction10=paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_trans_spec_tail1576); 
                    pushFollow(FOLLOW_state_in_trans_spec_tail1580);
                    s_1=state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_trans_spec_tail1582); 
                    pushFollow(FOLLOW_state_in_trans_spec_tail1586);
                    s_2=state();
                    _fsp--;

                    pushFollow(FOLLOW_prob_in_trans_spec_tail1588);
                    prob9=prob();
                    _fsp--;


                                if(prob9 > 0.0) // this causes MORE entries to exist - don't know why yet
                                    for(int a : paction10)
                                        for(int s1 : s_1.l)
                                            for(int s2 : s_2.l)
                                                dotPomdpSpec.T[a].set(s1, s2, prob9);
                            

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:319:7: paction COLONTOK state u_matrix
                    {
                    pushFollow(FOLLOW_paction_in_trans_spec_tail1616);
                    paction11=paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_trans_spec_tail1618); 
                    pushFollow(FOLLOW_state_in_trans_spec_tail1620);
                    state12=state();
                    _fsp--;

                    pushFollow(FOLLOW_u_matrix_in_trans_spec_tail1622);
                    u_matrix13=u_matrix();
                    _fsp--;


                            	matrixContext=MC_TRANSITION_ROW;
                            	for(int a : paction11)	
                            		for (int s : state12.l)
                            			for (int i=0;i<dotPomdpSpec.nrSta;i++)
                            				dotPomdpSpec.T[a].set(s,i,u_matrix13.get(i,0));
                            

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:327:7: paction ui_matrix
                    {
                    pushFollow(FOLLOW_paction_in_trans_spec_tail1641);
                    paction14=paction();
                    _fsp--;

                    pushFollow(FOLLOW_ui_matrix_in_trans_spec_tail1643);
                    ui_matrix15=ui_matrix();
                    _fsp--;


                            matrixContext=MC_TRANSITION;
                            for(int a : paction14) dotPomdpSpec.T[a] = ui_matrix15;
                            

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end trans_spec_tail


    // $ANTLR start obs_prob_spec
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:335:1: obs_prob_spec : OTOK COLONTOK obs_spec_tail ;
    public final void obs_prob_spec() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:336:5: ( OTOK COLONTOK obs_spec_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:336:7: OTOK COLONTOK obs_spec_tail
            {
            match(input,OTOK,FOLLOW_OTOK_in_obs_prob_spec1681); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_prob_spec1683); 
            pushFollow(FOLLOW_obs_spec_tail_in_obs_prob_spec1685);
            obs_spec_tail();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end obs_prob_spec


    // $ANTLR start obs_spec_tail
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );
    public final void obs_spec_tail() throws RecognitionException {
        double prob16 = 0.0;

        ArrayList<Integer> paction17 = null;

        state_return state18 = null;

        obs_return obs19 = null;

        ArrayList<Integer> paction20 = null;

        state_return state21 = null;

        CustomMatrix u_matrix22 = null;

        ArrayList<Integer> paction23 = null;

        CustomMatrix u_matrix24 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:340:5: ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix )
            int alt12=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA12_6 = input.LA(4);

                        if ( (LA12_6==COLONTOK) ) {
                            alt12=1;
                        }
                        else if ( (LA12_6==UNIFORMTOK||LA12_6==RESETTOK||LA12_6==INT||LA12_6==FLOAT) ) {
                            alt12=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA12_7 = input.LA(4);

                        if ( (LA12_7==COLONTOK) ) {
                            alt12=1;
                        }
                        else if ( (LA12_7==UNIFORMTOK||LA12_7==RESETTOK||LA12_7==INT||LA12_7==FLOAT) ) {
                            alt12=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA12_8 = input.LA(4);

                        if ( (LA12_8==COLONTOK) ) {
                            alt12=1;
                        }
                        else if ( (LA12_8==UNIFORMTOK||LA12_8==RESETTOK||LA12_8==INT||LA12_8==FLOAT) ) {
                            alt12=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 4, input);

                        throw nvae;
                    }

                }
                else if ( (LA12_1==UNIFORMTOK||LA12_1==RESETTOK||LA12_1==INT||LA12_1==FLOAT) ) {
                    alt12=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 1, input);

                    throw nvae;
                }
                }
                break;
            case STRING:
                {
                int LA12_2 = input.LA(2);

                if ( (LA12_2==UNIFORMTOK||LA12_2==RESETTOK||LA12_2==INT||LA12_2==FLOAT) ) {
                    alt12=3;
                }
                else if ( (LA12_2==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA12_6 = input.LA(4);

                        if ( (LA12_6==COLONTOK) ) {
                            alt12=1;
                        }
                        else if ( (LA12_6==UNIFORMTOK||LA12_6==RESETTOK||LA12_6==INT||LA12_6==FLOAT) ) {
                            alt12=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA12_7 = input.LA(4);

                        if ( (LA12_7==COLONTOK) ) {
                            alt12=1;
                        }
                        else if ( (LA12_7==UNIFORMTOK||LA12_7==RESETTOK||LA12_7==INT||LA12_7==FLOAT) ) {
                            alt12=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA12_8 = input.LA(4);

                        if ( (LA12_8==COLONTOK) ) {
                            alt12=1;
                        }
                        else if ( (LA12_8==UNIFORMTOK||LA12_8==RESETTOK||LA12_8==INT||LA12_8==FLOAT) ) {
                            alt12=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 2, input);

                    throw nvae;
                }
                }
                break;
            case ASTERICKTOK:
                {
                int LA12_3 = input.LA(2);

                if ( (LA12_3==UNIFORMTOK||LA12_3==RESETTOK||LA12_3==INT||LA12_3==FLOAT) ) {
                    alt12=3;
                }
                else if ( (LA12_3==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA12_6 = input.LA(4);

                        if ( (LA12_6==COLONTOK) ) {
                            alt12=1;
                        }
                        else if ( (LA12_6==UNIFORMTOK||LA12_6==RESETTOK||LA12_6==INT||LA12_6==FLOAT) ) {
                            alt12=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA12_7 = input.LA(4);

                        if ( (LA12_7==COLONTOK) ) {
                            alt12=1;
                        }
                        else if ( (LA12_7==UNIFORMTOK||LA12_7==RESETTOK||LA12_7==INT||LA12_7==FLOAT) ) {
                            alt12=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA12_8 = input.LA(4);

                        if ( (LA12_8==COLONTOK) ) {
                            alt12=1;
                        }
                        else if ( (LA12_8==UNIFORMTOK||LA12_8==RESETTOK||LA12_8==INT||LA12_8==FLOAT) ) {
                            alt12=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("339:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 12, 0, input);

                throw nvae;
            }

            switch (alt12) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:340:7: paction COLONTOK state COLONTOK obs prob
                    {
                    pushFollow(FOLLOW_paction_in_obs_spec_tail1704);
                    paction17=paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_spec_tail1706); 
                    pushFollow(FOLLOW_state_in_obs_spec_tail1708);
                    state18=state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_spec_tail1710); 
                    pushFollow(FOLLOW_obs_in_obs_spec_tail1712);
                    obs19=obs();
                    _fsp--;

                    pushFollow(FOLLOW_prob_in_obs_spec_tail1714);
                    prob16=prob();
                    _fsp--;


                            if(prob16 > 0.0)
                                for(int a : paction17)
                                    for(int s2 : state18.l)
                                        for(int o : obs19.l)
                                            dotPomdpSpec.O[a].set(s2, o, prob16);
                            

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:349:7: paction COLONTOK state u_matrix
                    {
                    pushFollow(FOLLOW_paction_in_obs_spec_tail1741);
                    paction20=paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_spec_tail1743); 
                    pushFollow(FOLLOW_state_in_obs_spec_tail1745);
                    state21=state();
                    _fsp--;

                    pushFollow(FOLLOW_u_matrix_in_obs_spec_tail1747);
                    u_matrix22=u_matrix();
                    _fsp--;


                            	matrixContext=MC_OBSERVATION_ROW;
                            	for(int a : paction20)	
                            		for (int s : state21.l)
                            			for (int i=0;i<dotPomdpSpec.nrObs;i++)
                            				dotPomdpSpec.O[a].set(s,i,u_matrix22.get(i,0));
                            	

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:357:7: paction u_matrix
                    {
                    pushFollow(FOLLOW_paction_in_obs_spec_tail1766);
                    paction23=paction();
                    _fsp--;

                    pushFollow(FOLLOW_u_matrix_in_obs_spec_tail1768);
                    u_matrix24=u_matrix();
                    _fsp--;


                            	matrixContext=MC_OBSERVATION;
                            	for(int a : paction23) dotPomdpSpec.O[a] = u_matrix24;
                            

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end obs_spec_tail


    // $ANTLR start reward_spec
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:365:1: reward_spec : RTOK COLONTOK reward_spec_tail ;
    public final void reward_spec() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:366:5: ( RTOK COLONTOK reward_spec_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:366:7: RTOK COLONTOK reward_spec_tail
            {
            match(input,RTOK,FOLLOW_RTOK_in_reward_spec1808); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec1810); 
            pushFollow(FOLLOW_reward_spec_tail_in_reward_spec1812);
            reward_spec_tail();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end reward_spec


    // $ANTLR start reward_spec_tail
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );
    public final void reward_spec_tail() throws RecognitionException {
        state_return s_1 = null;

        state_return s_2 = null;

        obs_return obs25 = null;

        double number26 = 0.0;

        ArrayList<Integer> paction27 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:370:5: ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix )
            int alt13=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA13_5 = input.LA(4);

                        if ( (LA13_5==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA13_10 = input.LA(6);

                                if ( (LA13_10==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_10>=PLUSTOK && LA13_10<=MINUSTOK)||LA13_10==INT||LA13_10==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA13_11 = input.LA(6);

                                if ( (LA13_11==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_11>=PLUSTOK && LA13_11<=MINUSTOK)||LA13_11==INT||LA13_11==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA13_12 = input.LA(6);

                                if ( (LA13_12==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_12>=PLUSTOK && LA13_12<=MINUSTOK)||LA13_12==INT||LA13_12==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA13_5>=PLUSTOK && LA13_5<=MINUSTOK)||LA13_5==INT||LA13_5==FLOAT) ) {
                            alt13=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 5, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA13_6 = input.LA(4);

                        if ( (LA13_6==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA13_10 = input.LA(6);

                                if ( (LA13_10==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_10>=PLUSTOK && LA13_10<=MINUSTOK)||LA13_10==INT||LA13_10==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA13_11 = input.LA(6);

                                if ( (LA13_11==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_11>=PLUSTOK && LA13_11<=MINUSTOK)||LA13_11==INT||LA13_11==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA13_12 = input.LA(6);

                                if ( (LA13_12==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_12>=PLUSTOK && LA13_12<=MINUSTOK)||LA13_12==INT||LA13_12==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA13_6>=PLUSTOK && LA13_6<=MINUSTOK)||LA13_6==INT||LA13_6==FLOAT) ) {
                            alt13=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA13_7 = input.LA(4);

                        if ( (LA13_7==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA13_10 = input.LA(6);

                                if ( (LA13_10==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_10>=PLUSTOK && LA13_10<=MINUSTOK)||LA13_10==INT||LA13_10==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA13_11 = input.LA(6);

                                if ( (LA13_11==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_11>=PLUSTOK && LA13_11<=MINUSTOK)||LA13_11==INT||LA13_11==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA13_12 = input.LA(6);

                                if ( (LA13_12==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_12>=PLUSTOK && LA13_12<=MINUSTOK)||LA13_12==INT||LA13_12==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA13_7>=PLUSTOK && LA13_7<=MINUSTOK)||LA13_7==INT||LA13_7==FLOAT) ) {
                            alt13=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 1, input);

                    throw nvae;
                }
                }
                break;
            case STRING:
                {
                int LA13_2 = input.LA(2);

                if ( (LA13_2==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA13_5 = input.LA(4);

                        if ( (LA13_5==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA13_10 = input.LA(6);

                                if ( (LA13_10==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_10>=PLUSTOK && LA13_10<=MINUSTOK)||LA13_10==INT||LA13_10==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA13_11 = input.LA(6);

                                if ( (LA13_11==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_11>=PLUSTOK && LA13_11<=MINUSTOK)||LA13_11==INT||LA13_11==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA13_12 = input.LA(6);

                                if ( (LA13_12==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_12>=PLUSTOK && LA13_12<=MINUSTOK)||LA13_12==INT||LA13_12==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA13_5>=PLUSTOK && LA13_5<=MINUSTOK)||LA13_5==INT||LA13_5==FLOAT) ) {
                            alt13=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 5, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA13_6 = input.LA(4);

                        if ( (LA13_6==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA13_10 = input.LA(6);

                                if ( (LA13_10==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_10>=PLUSTOK && LA13_10<=MINUSTOK)||LA13_10==INT||LA13_10==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA13_11 = input.LA(6);

                                if ( (LA13_11==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_11>=PLUSTOK && LA13_11<=MINUSTOK)||LA13_11==INT||LA13_11==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA13_12 = input.LA(6);

                                if ( (LA13_12==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_12>=PLUSTOK && LA13_12<=MINUSTOK)||LA13_12==INT||LA13_12==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA13_6>=PLUSTOK && LA13_6<=MINUSTOK)||LA13_6==INT||LA13_6==FLOAT) ) {
                            alt13=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA13_7 = input.LA(4);

                        if ( (LA13_7==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA13_10 = input.LA(6);

                                if ( (LA13_10==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_10>=PLUSTOK && LA13_10<=MINUSTOK)||LA13_10==INT||LA13_10==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA13_11 = input.LA(6);

                                if ( (LA13_11==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_11>=PLUSTOK && LA13_11<=MINUSTOK)||LA13_11==INT||LA13_11==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA13_12 = input.LA(6);

                                if ( (LA13_12==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_12>=PLUSTOK && LA13_12<=MINUSTOK)||LA13_12==INT||LA13_12==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA13_7>=PLUSTOK && LA13_7<=MINUSTOK)||LA13_7==INT||LA13_7==FLOAT) ) {
                            alt13=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 2, input);

                    throw nvae;
                }
                }
                break;
            case ASTERICKTOK:
                {
                int LA13_3 = input.LA(2);

                if ( (LA13_3==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA13_5 = input.LA(4);

                        if ( (LA13_5==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA13_10 = input.LA(6);

                                if ( (LA13_10==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_10>=PLUSTOK && LA13_10<=MINUSTOK)||LA13_10==INT||LA13_10==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA13_11 = input.LA(6);

                                if ( (LA13_11==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_11>=PLUSTOK && LA13_11<=MINUSTOK)||LA13_11==INT||LA13_11==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA13_12 = input.LA(6);

                                if ( (LA13_12==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_12>=PLUSTOK && LA13_12<=MINUSTOK)||LA13_12==INT||LA13_12==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA13_5>=PLUSTOK && LA13_5<=MINUSTOK)||LA13_5==INT||LA13_5==FLOAT) ) {
                            alt13=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 5, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA13_6 = input.LA(4);

                        if ( (LA13_6==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA13_10 = input.LA(6);

                                if ( (LA13_10==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_10>=PLUSTOK && LA13_10<=MINUSTOK)||LA13_10==INT||LA13_10==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA13_11 = input.LA(6);

                                if ( (LA13_11==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_11>=PLUSTOK && LA13_11<=MINUSTOK)||LA13_11==INT||LA13_11==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA13_12 = input.LA(6);

                                if ( (LA13_12==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_12>=PLUSTOK && LA13_12<=MINUSTOK)||LA13_12==INT||LA13_12==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA13_6>=PLUSTOK && LA13_6<=MINUSTOK)||LA13_6==INT||LA13_6==FLOAT) ) {
                            alt13=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA13_7 = input.LA(4);

                        if ( (LA13_7==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA13_10 = input.LA(6);

                                if ( (LA13_10==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_10>=PLUSTOK && LA13_10<=MINUSTOK)||LA13_10==INT||LA13_10==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA13_11 = input.LA(6);

                                if ( (LA13_11==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_11>=PLUSTOK && LA13_11<=MINUSTOK)||LA13_11==INT||LA13_11==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA13_12 = input.LA(6);

                                if ( (LA13_12==COLONTOK) ) {
                                    alt13=1;
                                }
                                else if ( ((LA13_12>=PLUSTOK && LA13_12<=MINUSTOK)||LA13_12==INT||LA13_12==FLOAT) ) {
                                    alt13=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA13_7>=PLUSTOK && LA13_7<=MINUSTOK)||LA13_7==INT||LA13_7==FLOAT) ) {
                            alt13=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("369:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:370:7: paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number
                    {
                    pushFollow(FOLLOW_paction_in_reward_spec_tail1830);
                    paction27=paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1832); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1836);
                    s_1=state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1838); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1842);
                    s_2=state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1844); 
                    pushFollow(FOLLOW_obs_in_reward_spec_tail1846);
                    obs25=obs();
                    _fsp--;

                    pushFollow(FOLLOW_number_in_reward_spec_tail1848);
                    number26=number();
                    _fsp--;

                       
                            	if(dotPomdpSpec.compReward && input.toString(s_2.start,s_2.stop).compareTo(Character.toString('*'))!=0 ||
                                            input.toString(obs25.start,obs25.stop).compareTo(Character.toString('*'))!=0){
                                    System.out.println("PARSER: full reward representation detected, probably you will get out of memory");        
                                	// Compressed rewards do not apply any more :(, trying full rewards
                                	dotPomdpSpec.compReward=false;
                                    // Creating Huge Reward Matrix (4D)
                                    dotPomdpSpec.fullR=new CustomMatrix[dotPomdpSpec.nrAct][dotPomdpSpec.nrSta];    
                                	for(int a=0; a<dotPomdpSpec.nrAct; a++) 
                                		for(int s=0; s<dotPomdpSpec.nrSta; s++){ 
                                    		dotPomdpSpec.fullR[a][s] = new CustomMatrix(dotPomdpSpec.nrSta,dotPomdpSpec.nrObs);
                                    		// Now we have to copy the date from R to fullR
                                    		CustomVector colV=CustomVector.getHomogene(dotPomdpSpec.nrSta,dotPomdpSpec.R[a].get(s));
                                    		//new CustomVector(dotPomdpSpec.nrSta);
                                    		//for (int sp=0;sp<dotPomdpSpec.nrSta;sp++)
                                    		//	colV.set(sp,dotPomdpSpec.R[a].get(s));	
                                    		for (int o=0;o<dotPomdpSpec.nrObs;o++)
                                    			dotPomdpSpec.fullR[a][s].setColumn(o,colV);
                                    	}
                                }
                            	if (dotPomdpSpec.compReward){
                            	    if(number26 != 0.0)
                            			for(int a : paction27)
                                    		for(int s1 : s_1.l)
                                        		dotPomdpSpec.R[a].set(s1, number26); 
                            	}
                            	else{         
                                	if(number26 != 0.0)
                                		for(int a : paction27)
                                    		for(int s1 : s_1.l)
                                    			for(int s2 : s_2.l)
                                    				for(int o : obs25.l) 
                                        				dotPomdpSpec.fullR[a][s1].set(s2,o,number26);                   
                            	}
                            

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:406:7: paction COLONTOK state COLONTOK state num_matrix
                    {
                    pushFollow(FOLLOW_paction_in_reward_spec_tail1866);
                    paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1868); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1870);
                    state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1872); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1874);
                    state();
                    _fsp--;

                    pushFollow(FOLLOW_num_matrix_in_reward_spec_tail1876);
                    num_matrix();
                    _fsp--;


                            err("unsupported feature COLONTOK state COLONTOK state num_matrix");

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:409:7: paction COLONTOK state num_matrix
                    {
                    pushFollow(FOLLOW_paction_in_reward_spec_tail1894);
                    paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1896); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1898);
                    state();
                    _fsp--;

                    pushFollow(FOLLOW_num_matrix_in_reward_spec_tail1900);
                    num_matrix();
                    _fsp--;

                    err("unsupported feature COLONTOK state num_matrix");

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end reward_spec_tail


    // $ANTLR start ui_matrix
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:413:1: ui_matrix returns [CustomMatrix m] : ( UNIFORMTOK | IDENTITYTOK | prob_matrix );
    public final CustomMatrix ui_matrix() throws RecognitionException {
        CustomMatrix m = null;

        CustomMatrix prob_matrix28 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:414:5: ( UNIFORMTOK | IDENTITYTOK | prob_matrix )
            int alt14=3;
            switch ( input.LA(1) ) {
            case UNIFORMTOK:
                {
                alt14=1;
                }
                break;
            case IDENTITYTOK:
                {
                alt14=2;
                }
                break;
            case INT:
            case FLOAT:
                {
                alt14=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("413:1: ui_matrix returns [CustomMatrix m] : ( UNIFORMTOK | IDENTITYTOK | prob_matrix );", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:414:7: UNIFORMTOK
                    {
                    match(input,UNIFORMTOK,FOLLOW_UNIFORMTOK_in_ui_matrix1936); 
                    m = CustomMatrix.getUniform(dotPomdpSpec.nrSta,dotPomdpSpec.nrSta);

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:416:7: IDENTITYTOK
                    {
                    match(input,IDENTITYTOK,FOLLOW_IDENTITYTOK_in_ui_matrix1952); 
                    m = CustomMatrix.getIdentity(dotPomdpSpec.nrSta);

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:418:7: prob_matrix
                    {
                    pushFollow(FOLLOW_prob_matrix_in_ui_matrix1971);
                    prob_matrix28=prob_matrix();
                    _fsp--;

                    m = prob_matrix28;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return m;
    }
    // $ANTLR end ui_matrix


    // $ANTLR start u_matrix
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:422:1: u_matrix returns [CustomMatrix m] : ( UNIFORMTOK | RESETTOK | prob_matrix );
    public final CustomMatrix u_matrix() throws RecognitionException {
        CustomMatrix m = null;

        CustomMatrix prob_matrix29 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:423:5: ( UNIFORMTOK | RESETTOK | prob_matrix )
            int alt15=3;
            switch ( input.LA(1) ) {
            case UNIFORMTOK:
                {
                alt15=1;
                }
                break;
            case RESETTOK:
                {
                alt15=2;
                }
                break;
            case INT:
            case FLOAT:
                {
                alt15=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("422:1: u_matrix returns [CustomMatrix m] : ( UNIFORMTOK | RESETTOK | prob_matrix );", 15, 0, input);

                throw nvae;
            }

            switch (alt15) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:423:7: UNIFORMTOK
                    {
                    match(input,UNIFORMTOK,FOLLOW_UNIFORMTOK_in_u_matrix1999); 

                        	switch (matrixContext){
                        	case MC_OBSERVATION: 
                        		m = CustomMatrix.getUniform(dotPomdpSpec.nrSta,dotPomdpSpec.nrObs);
                        		break;
                        	case MC_TRANSITION:
                        		m = CustomMatrix.getUniform(dotPomdpSpec.nrSta,dotPomdpSpec.nrSta);
                        		break;
                        	case MC_TRANSITION_ROW:
                        		m = CustomMatrix.getUniform(1,dotPomdpSpec.nrSta);
                        		break;
                     		case MC_OBSERVATION_ROW:
                        		m = CustomMatrix.getUniform(1,dotPomdpSpec.nrObs);
                        		break;
                        	default:
                        		err("PARSER: wrong matrix context... umh? (UNIFORMTOK)");
                        		break;
                        	}
                        	

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:443:7: RESETTOK
                    {
                    match(input,RESETTOK,FOLLOW_RESETTOK_in_u_matrix2014); 
                    err("PARSER: the reset feature is not supported yet");

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:445:7: prob_matrix
                    {
                    pushFollow(FOLLOW_prob_matrix_in_u_matrix2029);
                    prob_matrix29=prob_matrix();
                    _fsp--;

                    m = prob_matrix29;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return m;
    }
    // $ANTLR end u_matrix


    // $ANTLR start prob_matrix
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:449:1: prob_matrix returns [CustomMatrix m] : ( prob )+ ;
    public final CustomMatrix prob_matrix() throws RecognitionException {
        CustomMatrix m = null;

        double prob30 = 0.0;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:450:5: ( ( prob )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:451:6: ( prob )+
            {

                 int index = 0;
                 int i_max,j_max;
                 	switch (matrixContext){
                	case MC_OBSERVATION:
                	 	i_max = dotPomdpSpec.nrObs;
                	 	j_max = dotPomdpSpec.nrSta;
               			break;
                	case MC_TRANSITION:
                	 	i_max = dotPomdpSpec.nrSta;
                	 	j_max = dotPomdpSpec.nrSta;
                		break;
                	case MC_TRANSITION_ROW:
                	 	i_max = dotPomdpSpec.nrSta;
                	 	j_max = 1;
                		break;
             		case MC_OBSERVATION_ROW:
             		    i_max = dotPomdpSpec.nrObs;
                	 	j_max = 1;
                		break;
                	default:
                		err("PARSER: wrong matrix context... umh? (prob_matrix)");
                		j_max=0;
                		i_max=0;
                		break;
                	}  
                 m = new CustomMatrix(i_max,j_max);
                 
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:479:9: ( prob )+
            int cnt16=0;
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==INT||LA16_0==FLOAT) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:479:10: prob
            	    {
            	    pushFollow(FOLLOW_prob_in_prob_matrix2075);
            	    prob30=prob();
            	    _fsp--;


            	            	if (prob30 > 0.0) m.set(index % i_max,index / i_max,prob30);
            	                index++;
            	            

            	    }
            	    break;

            	default :
            	    if ( cnt16 >= 1 ) break loop16;
                        EarlyExitException eee =
                            new EarlyExitException(16, input);
                        throw eee;
                }
                cnt16++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return m;
    }
    // $ANTLR end prob_matrix


    // $ANTLR start prob_vector
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:487:1: prob_vector returns [CustomVector vector] : ( prob )+ ;
    public final CustomVector prob_vector() throws RecognitionException {
        CustomVector vector = null;

        double prob31 = 0.0;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:488:5: ( ( prob )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:490:9: ( prob )+
            {
            int index = 0; vector = new CustomVector(dotPomdpSpec.nrSta);
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:491:9: ( prob )+
            int cnt17=0;
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==INT||LA17_0==FLOAT) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:491:10: prob
            	    {
            	    pushFollow(FOLLOW_prob_in_prob_vector2148);
            	    prob31=prob();
            	    _fsp--;


            	                // action here - the check for 0 actually doesn't matter
            	                if (prob31 > 0.0) vector.set(index, prob31);
            	                index++;
            	            

            	    }
            	    break;

            	default :
            	    if ( cnt17 >= 1 ) break loop17;
                        EarlyExitException eee =
                            new EarlyExitException(17, input);
                        throw eee;
                }
                cnt17++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return vector;
    }
    // $ANTLR end prob_vector


    // $ANTLR start num_matrix
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:500:1: num_matrix : ( number )+ ;
    public final void num_matrix() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:501:5: ( ( number )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:501:12: ( number )+
            {

                 int index = 0;
                 //int i_max;
                 
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:505:9: ( number )+
            int cnt18=0;
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>=PLUSTOK && LA18_0<=MINUSTOK)||LA18_0==INT||LA18_0==FLOAT) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:505:10: number
            	    {
            	    pushFollow(FOLLOW_number_in_num_matrix2209);
            	    number();
            	    _fsp--;


            	                index++;
            	            

            	    }
            	    break;

            	default :
            	    if ( cnt18 >= 1 ) break loop18;
                        EarlyExitException eee =
                            new EarlyExitException(18, input);
                        throw eee;
                }
                cnt18++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end num_matrix

    public static class state_return extends ParserRuleReturnScope {
        public ArrayList<Integer> l = new ArrayList<Integer>();
    };

    // $ANTLR start state
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:512:1: state returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );
    public final state_return state() throws RecognitionException {
        state_return retval = new state_return();
        retval.start = input.LT(1);

        Token INT32=null;
        Token STRING33=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:513:5: ( INT | STRING | ASTERICKTOK )
            int alt19=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt19=1;
                }
                break;
            case STRING:
                {
                alt19=2;
                }
                break;
            case ASTERICKTOK:
                {
                alt19=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("512:1: state returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:514:9: INT
                    {
                    INT32=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_state2260); 
                    retval.l.add(Integer.parseInt(INT32.getText()));

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:517:9: STRING
                    {
                    STRING33=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_state2288); 
                    retval.l.add(dotPomdpSpec.staList.indexOf(STRING33.getText()));

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:520:9: ASTERICKTOK
                    {
                    match(input,ASTERICKTOK,FOLLOW_ASTERICKTOK_in_state2315); 
                    for(int s=0; s<dotPomdpSpec.nrSta; s++) retval.l.add(s);

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end state


    // $ANTLR start paction
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:524:1: paction returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );
    public final ArrayList<Integer> paction() throws RecognitionException {
        ArrayList<Integer> l =  new ArrayList<Integer>();

        Token INT34=null;
        Token STRING35=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:525:5: ( INT | STRING | ASTERICKTOK )
            int alt20=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt20=1;
                }
                break;
            case STRING:
                {
                alt20=2;
                }
                break;
            case ASTERICKTOK:
                {
                alt20=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("524:1: paction returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:526:9: INT
                    {
                    INT34=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_paction2357); 
                    l.add(Integer.parseInt(INT34.getText()));

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:529:9: STRING
                    {
                    STRING35=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_paction2385); 
                    l.add(dotPomdpSpec.actList.indexOf(STRING35.getText()));

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:532:9: ASTERICKTOK
                    {
                    match(input,ASTERICKTOK,FOLLOW_ASTERICKTOK_in_paction2412); 
                    for(int a=0; a<dotPomdpSpec.nrAct; a++) l.add(a);

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return l;
    }
    // $ANTLR end paction

    public static class obs_return extends ParserRuleReturnScope {
        public ArrayList<Integer> l = new ArrayList<Integer>();
    };

    // $ANTLR start obs
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:536:1: obs returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );
    public final obs_return obs() throws RecognitionException {
        obs_return retval = new obs_return();
        retval.start = input.LT(1);

        Token INT36=null;
        Token STRING37=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:537:5: ( INT | STRING | ASTERICKTOK )
            int alt21=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt21=1;
                }
                break;
            case STRING:
                {
                alt21=2;
                }
                break;
            case ASTERICKTOK:
                {
                alt21=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("536:1: obs returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:538:9: INT
                    {
                    INT36=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_obs2455); 
                    retval.l.add(Integer.parseInt(INT36.getText()));

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:541:9: STRING
                    {
                    STRING37=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_obs2483); 
                    retval.l.add(dotPomdpSpec.obsList.indexOf(STRING37.getText()));

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:544:9: ASTERICKTOK
                    {
                    match(input,ASTERICKTOK,FOLLOW_ASTERICKTOK_in_obs2510); 
                    for(int o=0; o<dotPomdpSpec.nrObs; o++) retval.l.add(o);

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end obs


    // $ANTLR start ident_list
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:548:1: ident_list returns [ArrayList<String> list] : ( STRING )+ ;
    public final ArrayList<String> ident_list() throws RecognitionException {
        ArrayList<String> list = null;

        Token STRING38=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:549:5: ( ( STRING )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:550:9: ( STRING )+
            {
            list = new ArrayList<String>();
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:551:9: ( STRING )+
            int cnt22=0;
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==STRING) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:551:10: STRING
            	    {
            	    STRING38=(Token)input.LT(1);
            	    match(input,STRING,FOLLOW_STRING_in_ident_list2566); 
            	    list.add(STRING38.getText());

            	    }
            	    break;

            	default :
            	    if ( cnt22 >= 1 ) break loop22;
                        EarlyExitException eee =
                            new EarlyExitException(22, input);
                        throw eee;
                }
                cnt22++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return list;
    }
    // $ANTLR end ident_list


    // $ANTLR start prob
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:556:1: prob returns [double p] : ( INT | FLOAT );
    public final double prob() throws RecognitionException {
        double p = 0.0;

        Token INT39=null;
        Token FLOAT40=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:557:5: ( INT | FLOAT )
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==INT) ) {
                alt23=1;
            }
            else if ( (LA23_0==FLOAT) ) {
                alt23=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("556:1: prob returns [double p] : ( INT | FLOAT );", 23, 0, input);

                throw nvae;
            }
            switch (alt23) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:557:7: INT
                    {
                    INT39=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_prob2614); 
                    p = Double.parseDouble(INT39.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:559:7: FLOAT
                    {
                    FLOAT40=(Token)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_prob2632); 
                    p = Double.parseDouble(FLOAT40.getText());

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return p;
    }
    // $ANTLR end prob


    // $ANTLR start number
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:563:1: number returns [double n] : ( optional_sign INT | optional_sign FLOAT );
    public final double number() throws RecognitionException {
        double n = 0.0;

        Token INT42=null;
        Token FLOAT44=null;
        int optional_sign41 = 0;

        int optional_sign43 = 0;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:564:5: ( optional_sign INT | optional_sign FLOAT )
            int alt24=2;
            switch ( input.LA(1) ) {
            case PLUSTOK:
                {
                int LA24_1 = input.LA(2);

                if ( (LA24_1==INT) ) {
                    alt24=1;
                }
                else if ( (LA24_1==FLOAT) ) {
                    alt24=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("563:1: number returns [double n] : ( optional_sign INT | optional_sign FLOAT );", 24, 1, input);

                    throw nvae;
                }
                }
                break;
            case MINUSTOK:
                {
                int LA24_2 = input.LA(2);

                if ( (LA24_2==FLOAT) ) {
                    alt24=2;
                }
                else if ( (LA24_2==INT) ) {
                    alt24=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("563:1: number returns [double n] : ( optional_sign INT | optional_sign FLOAT );", 24, 2, input);

                    throw nvae;
                }
                }
                break;
            case INT:
                {
                alt24=1;
                }
                break;
            case FLOAT:
                {
                alt24=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("563:1: number returns [double n] : ( optional_sign INT | optional_sign FLOAT );", 24, 0, input);

                throw nvae;
            }

            switch (alt24) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:564:7: optional_sign INT
                    {
                    pushFollow(FOLLOW_optional_sign_in_number2675);
                    optional_sign41=optional_sign();
                    _fsp--;

                    INT42=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_number2677); 
                    n = optional_sign41 * Double.parseDouble(INT42.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:566:7: optional_sign FLOAT
                    {
                    pushFollow(FOLLOW_optional_sign_in_number2696);
                    optional_sign43=optional_sign();
                    _fsp--;

                    FLOAT44=(Token)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_number2698); 
                    n = optional_sign43 * Double.parseDouble(FLOAT44.getText());

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end number


    // $ANTLR start optional_sign
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:570:1: optional_sign returns [int s] : ( PLUSTOK | MINUSTOK | );
    public final int optional_sign() throws RecognitionException {
        int s = 0;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:571:5: ( PLUSTOK | MINUSTOK | )
            int alt25=3;
            switch ( input.LA(1) ) {
            case PLUSTOK:
                {
                alt25=1;
                }
                break;
            case MINUSTOK:
                {
                alt25=2;
                }
                break;
            case INT:
            case FLOAT:
                {
                alt25=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("570:1: optional_sign returns [int s] : ( PLUSTOK | MINUSTOK | );", 25, 0, input);

                throw nvae;
            }

            switch (alt25) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:571:7: PLUSTOK
                    {
                    match(input,PLUSTOK,FOLLOW_PLUSTOK_in_optional_sign2730); 
                    s = 1;

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:573:7: MINUSTOK
                    {
                    match(input,MINUSTOK,FOLLOW_MINUSTOK_in_optional_sign2748); 
                    s = -1;

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:576:9: 
                    {
                    s = 1;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return s;
    }
    // $ANTLR end optional_sign


 

    public static final BitSet FOLLOW_preamble_in_dotPomdp796 = new BitSet(new long[]{0x0000000000010E02L});
    public static final BitSet FOLLOW_start_state_in_dotPomdp814 = new BitSet(new long[]{0x0000000000000E02L});
    public static final BitSet FOLLOW_param_list_in_dotPomdp833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_param_type_in_preamble869 = new BitSet(new long[]{0x00000000000001F2L});
    public static final BitSet FOLLOW_discount_param_in_param_type901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_param_in_param_type909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_state_param_in_param_type917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_action_param_in_param_type925 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_obs_param_in_param_type933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISCOUNTTOK_in_discount_param953 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_discount_param955 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_FLOAT_in_discount_param957 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VALUESTOK_in_value_param987 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_value_param989 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_value_tail_in_value_param991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REWARDTOK_in_value_tail1014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COSTTOK_in_value_tail1022 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STATESTOK_in_state_param1055 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_state_param1057 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_state_tail_in_state_param1059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_state_tail1082 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ident_list_in_state_tail1109 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTIONSTOK_in_action_param1150 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_action_param1152 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_action_tail_in_action_param1154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_action_tail1177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ident_list_in_action_tail1204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OBSERVATIONSTOK_in_obs_param1241 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_param1243 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_obs_param_tail_in_obs_param1245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_obs_param_tail1263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ident_list_in_obs_param_tail1290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTTOK_in_start_state1331 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_start_state1333 = new BitSet(new long[]{0x000000000A000000L});
    public static final BitSet FOLLOW_prob_vector_in_start_state1335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTTOK_in_start_state1362 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_start_state1364 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_STRING_in_start_state1366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTTOK_in_start_state1384 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_INCLUDETOK_in_start_state1386 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_start_state1388 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_start_state_list_in_start_state1390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTTOK_in_start_state1409 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_EXCLUDETOK_in_start_state1411 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_start_state1413 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_start_state_list_in_start_state1415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_state_in_start_state_list1463 = new BitSet(new long[]{0x0000000003200002L});
    public static final BitSet FOLLOW_param_spec_in_param_list1486 = new BitSet(new long[]{0x0000000000000E02L});
    public static final BitSet FOLLOW_trans_prob_spec_in_param_spec1509 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_obs_prob_spec_in_param_spec1517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_reward_spec_in_param_spec1526 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TTOK_in_trans_prob_spec1548 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_trans_prob_spec1550 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_trans_spec_tail_in_trans_prob_spec1552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_trans_spec_tail1574 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_trans_spec_tail1576 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_trans_spec_tail1580 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_trans_spec_tail1582 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_trans_spec_tail1586 = new BitSet(new long[]{0x000000000A000000L});
    public static final BitSet FOLLOW_prob_in_trans_spec_tail1588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_trans_spec_tail1616 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_trans_spec_tail1618 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_trans_spec_tail1620 = new BitSet(new long[]{0x000000000A081000L});
    public static final BitSet FOLLOW_u_matrix_in_trans_spec_tail1622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_trans_spec_tail1641 = new BitSet(new long[]{0x000000000A003000L});
    public static final BitSet FOLLOW_ui_matrix_in_trans_spec_tail1643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OTOK_in_obs_prob_spec1681 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_prob_spec1683 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_obs_spec_tail_in_obs_prob_spec1685 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_obs_spec_tail1704 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_spec_tail1706 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_obs_spec_tail1708 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_spec_tail1710 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_obs_in_obs_spec_tail1712 = new BitSet(new long[]{0x000000000A000000L});
    public static final BitSet FOLLOW_prob_in_obs_spec_tail1714 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_obs_spec_tail1741 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_spec_tail1743 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_obs_spec_tail1745 = new BitSet(new long[]{0x000000000A081000L});
    public static final BitSet FOLLOW_u_matrix_in_obs_spec_tail1747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_obs_spec_tail1766 = new BitSet(new long[]{0x000000000A081000L});
    public static final BitSet FOLLOW_u_matrix_in_obs_spec_tail1768 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RTOK_in_reward_spec1808 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec1810 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_reward_spec_tail_in_reward_spec1812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_reward_spec_tail1830 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1832 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1836 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1838 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1842 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1844 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_obs_in_reward_spec_tail1846 = new BitSet(new long[]{0x0000000002C00000L});
    public static final BitSet FOLLOW_number_in_reward_spec_tail1848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_reward_spec_tail1866 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1868 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1870 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1872 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1874 = new BitSet(new long[]{0x0000000002C00000L});
    public static final BitSet FOLLOW_num_matrix_in_reward_spec_tail1876 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_reward_spec_tail1894 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1896 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1898 = new BitSet(new long[]{0x0000000002C00000L});
    public static final BitSet FOLLOW_num_matrix_in_reward_spec_tail1900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNIFORMTOK_in_ui_matrix1936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTITYTOK_in_ui_matrix1952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_prob_matrix_in_ui_matrix1971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNIFORMTOK_in_u_matrix1999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RESETTOK_in_u_matrix2014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_prob_matrix_in_u_matrix2029 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_prob_in_prob_matrix2075 = new BitSet(new long[]{0x000000000A000002L});
    public static final BitSet FOLLOW_prob_in_prob_vector2148 = new BitSet(new long[]{0x000000000A000002L});
    public static final BitSet FOLLOW_number_in_num_matrix2209 = new BitSet(new long[]{0x0000000002C00002L});
    public static final BitSet FOLLOW_INT_in_state2260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_state2288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASTERICKTOK_in_state2315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_paction2357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_paction2385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASTERICKTOK_in_paction2412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_obs2455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_obs2483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASTERICKTOK_in_obs2510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_ident_list2566 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_INT_in_prob2614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_prob2632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_optional_sign_in_number2675 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_INT_in_number2677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_optional_sign_in_number2696 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_FLOAT_in_number2698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSTOK_in_optional_sign2730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUSTOK_in_optional_sign2748 = new BitSet(new long[]{0x0000000000000002L});

}