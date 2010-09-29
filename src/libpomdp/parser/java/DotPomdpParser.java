// $ANTLR 3.0.1 /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g 2010-09-29 15:22:38

    package libpomdp.parser.java;

    // we're using mtj to store the data
    import no.uib.cipr.matrix.*;    
    import no.uib.cipr.matrix.sparse.*;   


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

        // main structure
        private PomdpSpecSparse dotPomdpSpec = new PomdpSpecSparse();

        // threshold for sums of distros
        final double THRESHOLD = 1e-5;

        // return main structure
        public PomdpSpecSparse getSpec() {
            return dotPomdpSpec;
        }

        // simple debug mesg
        private void err(String msg) {
            System.err.println(msg);
        }



    // $ANTLR start dotPomdp
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:128:1: dotPomdp : preamble start_state param_list ;
    public final void dotPomdp() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:129:5: ( preamble start_state param_list )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:129:7: preamble start_state param_list
            {
            pushFollow(FOLLOW_preamble_in_dotPomdp768);
            preamble();
            _fsp--;


                        // we can now initialize the data structures for T, O, R
                        System.out.println("Successfully parsed preamble");            
                        /* initialize |A| s x s' dense matrices (they're actually sparse)
                           T: <action> : <start-state> : <end-state> prob  */
                        dotPomdpSpec.T = new DenseMatrix[dotPomdpSpec.nrAct];
                        for(int a=0; a<dotPomdpSpec.nrAct; a++) 
                            dotPomdpSpec.T[a] = new DenseMatrix(dotPomdpSpec.nrSta,
                                                                dotPomdpSpec.nrSta);
                        /* initialize |A| s' x o dense matrices (they're actually sparse)
                           O : <action> : <end-state> : <observation> prob */        
                        dotPomdpSpec.O = new DenseMatrix[dotPomdpSpec.nrAct];
                        for(int a=0; a<dotPomdpSpec.nrAct; a++) 
                            dotPomdpSpec.O[a] = new DenseMatrix(dotPomdpSpec.nrSta,
                                                                dotPomdpSpec.nrObs);
                        /* initialize |A| 1 x s' sparse vectors
                           R: <action> : <start-state> : * : * float */
                        dotPomdpSpec.R = new SparseVector[dotPomdpSpec.nrAct];
                        for(int a=0; a<dotPomdpSpec.nrAct; a++) 
                            dotPomdpSpec.R[a] = new SparseVector(dotPomdpSpec.nrSta);        
                    
            pushFollow(FOLLOW_start_state_in_dotPomdp786);
            start_state();
            _fsp--;


                        // make sure the start state is a distribution
                        System.out.println("Successfully parsed start state");
                        if (dotPomdpSpec.startState.norm(Vector.Norm.One) - 1.0 > THRESHOLD)
                            err("Start state not a distribution" + dotPomdpSpec.startState.norm(Vector.Norm.One));
                    
            pushFollow(FOLLOW_param_list_in_dotPomdp805);
            param_list();
            _fsp--;


                        // there should be a check for the parameter distros here...
                        System.out.println("Successfully parsed parameters");
                        
                    

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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:166:1: preamble : ( param_type )* ;
    public final void preamble() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:167:5: ( ( param_type )* )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:167:7: ( param_type )*
            {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:167:7: ( param_type )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=DISCOUNTTOK && LA1_0<=OBSERVATIONSTOK)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:167:7: param_type
            	    {
            	    pushFollow(FOLLOW_param_type_in_preamble841);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:170:1: param_type : ( discount_param | value_param | state_param | action_param | obs_param );
    public final void param_type() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:171:5: ( discount_param | value_param | state_param | action_param | obs_param )
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
                    new NoViableAltException("170:1: param_type : ( discount_param | value_param | state_param | action_param | obs_param );", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:171:7: discount_param
                    {
                    pushFollow(FOLLOW_discount_param_in_param_type873);
                    discount_param();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:172:7: value_param
                    {
                    pushFollow(FOLLOW_value_param_in_param_type881);
                    value_param();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:173:7: state_param
                    {
                    pushFollow(FOLLOW_state_param_in_param_type889);
                    state_param();
                    _fsp--;


                    }
                    break;
                case 4 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:174:7: action_param
                    {
                    pushFollow(FOLLOW_action_param_in_param_type897);
                    action_param();
                    _fsp--;


                    }
                    break;
                case 5 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:175:7: obs_param
                    {
                    pushFollow(FOLLOW_obs_param_in_param_type905);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:179:1: discount_param : DISCOUNTTOK COLONTOK FLOAT ;
    public final void discount_param() throws RecognitionException {
        Token FLOAT1=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:180:5: ( DISCOUNTTOK COLONTOK FLOAT )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:180:7: DISCOUNTTOK COLONTOK FLOAT
            {
            match(input,DISCOUNTTOK,FOLLOW_DISCOUNTTOK_in_discount_param925); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_discount_param927); 
            FLOAT1=(Token)input.LT(1);
            match(input,FLOAT,FOLLOW_FLOAT_in_discount_param929); 
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:185:1: value_param : VALUESTOK COLONTOK value_tail ;
    public final void value_param() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:186:5: ( VALUESTOK COLONTOK value_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:186:7: VALUESTOK COLONTOK value_tail
            {
            match(input,VALUESTOK,FOLLOW_VALUESTOK_in_value_param959); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_value_param961); 
            pushFollow(FOLLOW_value_tail_in_value_param963);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:189:1: value_tail : ( REWARDTOK | COSTTOK );
    public final void value_tail() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:190:5: ( REWARDTOK | COSTTOK )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:
            {
            if ( (input.LA(1)>=REWARDTOK && input.LA(1)<=COSTTOK) ) {
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_value_tail0);    throw mse;
            }


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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:194:1: state_param : STATESTOK COLONTOK state_tail ;
    public final void state_param() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:195:5: ( STATESTOK COLONTOK state_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:195:7: STATESTOK COLONTOK state_tail
            {
            match(input,STATESTOK,FOLLOW_STATESTOK_in_state_param1016); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_state_param1018); 
            pushFollow(FOLLOW_state_tail_in_state_param1020);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:198:1: state_tail : ( INT | ident_list );
    public final void state_tail() throws RecognitionException {
        Token INT2=null;
        ArrayList<String> ident_list3 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:199:5: ( INT | ident_list )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==INT) ) {
                alt3=1;
            }
            else if ( (LA3_0==STRING) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("198:1: state_tail : ( INT | ident_list );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:199:7: INT
                    {
                    INT2=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_state_tail1043); 
                    dotPomdpSpec.nrSta   = Integer.parseInt(INT2.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:202:7: ident_list
                    {
                    pushFollow(FOLLOW_ident_list_in_state_tail1070);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:208:1: action_param : ACTIONSTOK COLONTOK action_tail ;
    public final void action_param() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:209:5: ( ACTIONSTOK COLONTOK action_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:209:7: ACTIONSTOK COLONTOK action_tail
            {
            match(input,ACTIONSTOK,FOLLOW_ACTIONSTOK_in_action_param1111); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_action_param1113); 
            pushFollow(FOLLOW_action_tail_in_action_param1115);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:212:1: action_tail : ( INT | ident_list );
    public final void action_tail() throws RecognitionException {
        Token INT4=null;
        ArrayList<String> ident_list5 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:213:5: ( INT | ident_list )
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
                    new NoViableAltException("212:1: action_tail : ( INT | ident_list );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:213:7: INT
                    {
                    INT4=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_action_tail1138); 
                    dotPomdpSpec.nrAct   = Integer.parseInt(INT4.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:216:7: ident_list
                    {
                    pushFollow(FOLLOW_ident_list_in_action_tail1165);
                    ident_list5=ident_list();
                    _fsp--;

                    dotPomdpSpec.actList = (ArrayList) ident_list5;
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:222:1: obs_param : OBSERVATIONSTOK COLONTOK obs_param_tail ;
    public final void obs_param() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:223:5: ( OBSERVATIONSTOK COLONTOK obs_param_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:223:7: OBSERVATIONSTOK COLONTOK obs_param_tail
            {
            match(input,OBSERVATIONSTOK,FOLLOW_OBSERVATIONSTOK_in_obs_param1202); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_param1204); 
            pushFollow(FOLLOW_obs_param_tail_in_obs_param1206);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:226:1: obs_param_tail : ( INT | ident_list );
    public final void obs_param_tail() throws RecognitionException {
        Token INT6=null;
        ArrayList<String> ident_list7 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:227:5: ( INT | ident_list )
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
                    new NoViableAltException("226:1: obs_param_tail : ( INT | ident_list );", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:227:7: INT
                    {
                    INT6=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_obs_param_tail1224); 
                    dotPomdpSpec.nrObs   = Integer.parseInt(INT6.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:230:7: ident_list
                    {
                    pushFollow(FOLLOW_ident_list_in_obs_param_tail1251);
                    ident_list7=ident_list();
                    _fsp--;

                    dotPomdpSpec.obsList = (ArrayList) ident_list7;
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:236:1: start_state : ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | );
    public final void start_state() throws RecognitionException {
        SparseVector prob_vector8 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:237:5: ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | )
            int alt6=5;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==STARTTOK) ) {
                switch ( input.LA(2) ) {
                case INCLUDETOK:
                    {
                    alt6=3;
                    }
                    break;
                case COLONTOK:
                    {
                    int LA6_4 = input.LA(3);

                    if ( (LA6_4==STRING) ) {
                        alt6=2;
                    }
                    else if ( (LA6_4==INT||LA6_4==FLOAT) ) {
                        alt6=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("236:1: start_state : ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | );", 6, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case EXCLUDETOK:
                    {
                    alt6=4;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("236:1: start_state : ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | );", 6, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA6_0==EOF||(LA6_0>=TTOK && LA6_0<=RTOK)) ) {
                alt6=5;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("236:1: start_state : ( STARTTOK COLONTOK prob_vector | STARTTOK COLONTOK STRING | STARTTOK INCLUDETOK COLONTOK start_state_list | STARTTOK EXCLUDETOK COLONTOK start_state_list | );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:237:7: STARTTOK COLONTOK prob_vector
                    {
                    match(input,STARTTOK,FOLLOW_STARTTOK_in_start_state1292); 
                    match(input,COLONTOK,FOLLOW_COLONTOK_in_start_state1294); 
                    pushFollow(FOLLOW_prob_vector_in_start_state1296);
                    prob_vector8=prob_vector();
                    _fsp--;


                                System.out.println("ENTERED the first case for start state");
                                dotPomdpSpec.startState = prob_vector8;
                            

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:243:7: STARTTOK COLONTOK STRING
                    {
                    match(input,STARTTOK,FOLLOW_STARTTOK_in_start_state1323); 
                    match(input,COLONTOK,FOLLOW_COLONTOK_in_start_state1325); 
                    match(input,STRING,FOLLOW_STRING_in_start_state1327); 
                    err("unsopported feature");

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:245:7: STARTTOK INCLUDETOK COLONTOK start_state_list
                    {
                    match(input,STARTTOK,FOLLOW_STARTTOK_in_start_state1345); 
                    match(input,INCLUDETOK,FOLLOW_INCLUDETOK_in_start_state1347); 
                    match(input,COLONTOK,FOLLOW_COLONTOK_in_start_state1349); 
                    pushFollow(FOLLOW_start_state_list_in_start_state1351);
                    start_state_list();
                    _fsp--;

                    err("unsopported feature");

                    }
                    break;
                case 4 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:247:7: STARTTOK EXCLUDETOK COLONTOK start_state_list
                    {
                    match(input,STARTTOK,FOLLOW_STARTTOK_in_start_state1370); 
                    match(input,EXCLUDETOK,FOLLOW_EXCLUDETOK_in_start_state1372); 
                    match(input,COLONTOK,FOLLOW_COLONTOK_in_start_state1374); 
                    pushFollow(FOLLOW_start_state_list_in_start_state1376);
                    start_state_list();
                    _fsp--;

                    err("unsopported feature");

                    }
                    break;
                case 5 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:250:5: 
                    {
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:252:1: start_state_list : ( state )+ ;
    public final void start_state_list() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:253:5: ( ( state )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:253:7: ( state )+
            {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:253:7: ( state )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==ASTERICKTOK||(LA7_0>=STRING && LA7_0<=INT)) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:253:7: state
            	    {
            	    pushFollow(FOLLOW_state_in_start_state_list1417);
            	    state();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:256:1: param_list : ( param_spec )* ;
    public final void param_list() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:257:5: ( ( param_spec )* )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:257:7: ( param_spec )*
            {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:257:7: ( param_spec )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>=TTOK && LA8_0<=RTOK)) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:257:7: param_spec
            	    {
            	    pushFollow(FOLLOW_param_spec_in_param_list1440);
            	    param_spec();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop8;
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:260:1: param_spec : ( trans_prob_spec | obs_prob_spec | reward_spec );
    public final void param_spec() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:261:5: ( trans_prob_spec | obs_prob_spec | reward_spec )
            int alt9=3;
            switch ( input.LA(1) ) {
            case TTOK:
                {
                alt9=1;
                }
                break;
            case OTOK:
                {
                alt9=2;
                }
                break;
            case RTOK:
                {
                alt9=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("260:1: param_spec : ( trans_prob_spec | obs_prob_spec | reward_spec );", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:261:7: trans_prob_spec
                    {
                    pushFollow(FOLLOW_trans_prob_spec_in_param_spec1463);
                    trans_prob_spec();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:262:7: obs_prob_spec
                    {
                    pushFollow(FOLLOW_obs_prob_spec_in_param_spec1471);
                    obs_prob_spec();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:263:7: reward_spec
                    {
                    pushFollow(FOLLOW_reward_spec_in_param_spec1480);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:266:1: trans_prob_spec : TTOK COLONTOK trans_spec_tail ;
    public final void trans_prob_spec() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:267:5: ( TTOK COLONTOK trans_spec_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:267:7: TTOK COLONTOK trans_spec_tail
            {
            match(input,TTOK,FOLLOW_TTOK_in_trans_prob_spec1502); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_trans_prob_spec1504); 
            pushFollow(FOLLOW_trans_spec_tail_in_trans_prob_spec1506);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );
    public final void trans_spec_tail() throws RecognitionException {
        state_return s_1 = null;

        state_return s_2 = null;

        double prob9 = 0.0;

        ArrayList<Integer> paction10 = null;

        ArrayList<Integer> paction11 = null;

        DenseMatrix ui_matrix12 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:271:5: ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix )
            int alt10=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA10_6 = input.LA(4);

                        if ( (LA10_6==UNIFORMTOK||LA10_6==RESETTOK||LA10_6==INT||LA10_6==FLOAT) ) {
                            alt10=2;
                        }
                        else if ( (LA10_6==COLONTOK) ) {
                            alt10=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA10_7 = input.LA(4);

                        if ( (LA10_7==UNIFORMTOK||LA10_7==RESETTOK||LA10_7==INT||LA10_7==FLOAT) ) {
                            alt10=2;
                        }
                        else if ( (LA10_7==COLONTOK) ) {
                            alt10=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA10_8 = input.LA(4);

                        if ( (LA10_8==UNIFORMTOK||LA10_8==RESETTOK||LA10_8==INT||LA10_8==FLOAT) ) {
                            alt10=2;
                        }
                        else if ( (LA10_8==COLONTOK) ) {
                            alt10=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 4, input);

                        throw nvae;
                    }

                }
                else if ( ((LA10_1>=UNIFORMTOK && LA10_1<=IDENTITYTOK)||LA10_1==INT||LA10_1==FLOAT) ) {
                    alt10=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 1, input);

                    throw nvae;
                }
                }
                break;
            case STRING:
                {
                int LA10_2 = input.LA(2);

                if ( ((LA10_2>=UNIFORMTOK && LA10_2<=IDENTITYTOK)||LA10_2==INT||LA10_2==FLOAT) ) {
                    alt10=3;
                }
                else if ( (LA10_2==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA10_6 = input.LA(4);

                        if ( (LA10_6==UNIFORMTOK||LA10_6==RESETTOK||LA10_6==INT||LA10_6==FLOAT) ) {
                            alt10=2;
                        }
                        else if ( (LA10_6==COLONTOK) ) {
                            alt10=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA10_7 = input.LA(4);

                        if ( (LA10_7==UNIFORMTOK||LA10_7==RESETTOK||LA10_7==INT||LA10_7==FLOAT) ) {
                            alt10=2;
                        }
                        else if ( (LA10_7==COLONTOK) ) {
                            alt10=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA10_8 = input.LA(4);

                        if ( (LA10_8==UNIFORMTOK||LA10_8==RESETTOK||LA10_8==INT||LA10_8==FLOAT) ) {
                            alt10=2;
                        }
                        else if ( (LA10_8==COLONTOK) ) {
                            alt10=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 2, input);

                    throw nvae;
                }
                }
                break;
            case ASTERICKTOK:
                {
                int LA10_3 = input.LA(2);

                if ( ((LA10_3>=UNIFORMTOK && LA10_3<=IDENTITYTOK)||LA10_3==INT||LA10_3==FLOAT) ) {
                    alt10=3;
                }
                else if ( (LA10_3==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA10_6 = input.LA(4);

                        if ( (LA10_6==UNIFORMTOK||LA10_6==RESETTOK||LA10_6==INT||LA10_6==FLOAT) ) {
                            alt10=2;
                        }
                        else if ( (LA10_6==COLONTOK) ) {
                            alt10=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA10_7 = input.LA(4);

                        if ( (LA10_7==UNIFORMTOK||LA10_7==RESETTOK||LA10_7==INT||LA10_7==FLOAT) ) {
                            alt10=2;
                        }
                        else if ( (LA10_7==COLONTOK) ) {
                            alt10=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA10_8 = input.LA(4);

                        if ( (LA10_8==UNIFORMTOK||LA10_8==RESETTOK||LA10_8==INT||LA10_8==FLOAT) ) {
                            alt10=2;
                        }
                        else if ( (LA10_8==COLONTOK) ) {
                            alt10=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("270:1: trans_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state prob | paction COLONTOK state u_matrix | paction ui_matrix );", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:271:7: paction COLONTOK s_1= state COLONTOK s_2= state prob
                    {
                    pushFollow(FOLLOW_paction_in_trans_spec_tail1528);
                    paction10=paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_trans_spec_tail1530); 
                    pushFollow(FOLLOW_state_in_trans_spec_tail1534);
                    s_1=state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_trans_spec_tail1536); 
                    pushFollow(FOLLOW_state_in_trans_spec_tail1540);
                    s_2=state();
                    _fsp--;

                    pushFollow(FOLLOW_prob_in_trans_spec_tail1542);
                    prob9=prob();
                    _fsp--;


                                //if(prob9 > 0.0)  this causes MORE entries to exist - don't know why yet
                                    for(int a : paction10)
                                        for(int s1 : s_1.l)
                                            for(int s2 : s_2.l)
                                                dotPomdpSpec.T[a].set(s1, s2, prob9);
                            

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:280:7: paction COLONTOK state u_matrix
                    {
                    pushFollow(FOLLOW_paction_in_trans_spec_tail1570);
                    paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_trans_spec_tail1572); 
                    pushFollow(FOLLOW_state_in_trans_spec_tail1574);
                    state();
                    _fsp--;

                    pushFollow(FOLLOW_u_matrix_in_trans_spec_tail1576);
                    u_matrix();
                    _fsp--;

                    err("unsopported feature");

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:282:7: paction ui_matrix
                    {
                    pushFollow(FOLLOW_paction_in_trans_spec_tail1595);
                    paction11=paction();
                    _fsp--;

                    pushFollow(FOLLOW_ui_matrix_in_trans_spec_tail1597);
                    ui_matrix12=ui_matrix();
                    _fsp--;

                    for(int a : paction11) dotPomdpSpec.T[a] = ui_matrix12;

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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:287:1: obs_prob_spec : OTOK COLONTOK obs_spec_tail ;
    public final void obs_prob_spec() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:288:5: ( OTOK COLONTOK obs_spec_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:288:7: OTOK COLONTOK obs_spec_tail
            {
            match(input,OTOK,FOLLOW_OTOK_in_obs_prob_spec1635); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_prob_spec1637); 
            pushFollow(FOLLOW_obs_spec_tail_in_obs_prob_spec1639);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );
    public final void obs_spec_tail() throws RecognitionException {
        ArrayList<Integer> paction13 = null;

        state_return state14 = null;

        obs_return obs15 = null;

        double prob16 = 0.0;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:292:5: ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix )
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

                        if ( (LA11_6==COLONTOK) ) {
                            alt11=1;
                        }
                        else if ( (LA11_6==UNIFORMTOK||LA11_6==RESETTOK||LA11_6==INT||LA11_6==FLOAT) ) {
                            alt11=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA11_7 = input.LA(4);

                        if ( (LA11_7==COLONTOK) ) {
                            alt11=1;
                        }
                        else if ( (LA11_7==UNIFORMTOK||LA11_7==RESETTOK||LA11_7==INT||LA11_7==FLOAT) ) {
                            alt11=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA11_8 = input.LA(4);

                        if ( (LA11_8==COLONTOK) ) {
                            alt11=1;
                        }
                        else if ( (LA11_8==UNIFORMTOK||LA11_8==RESETTOK||LA11_8==INT||LA11_8==FLOAT) ) {
                            alt11=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 4, input);

                        throw nvae;
                    }

                }
                else if ( (LA11_1==UNIFORMTOK||LA11_1==RESETTOK||LA11_1==INT||LA11_1==FLOAT) ) {
                    alt11=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 1, input);

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

                        if ( (LA11_6==COLONTOK) ) {
                            alt11=1;
                        }
                        else if ( (LA11_6==UNIFORMTOK||LA11_6==RESETTOK||LA11_6==INT||LA11_6==FLOAT) ) {
                            alt11=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA11_7 = input.LA(4);

                        if ( (LA11_7==COLONTOK) ) {
                            alt11=1;
                        }
                        else if ( (LA11_7==UNIFORMTOK||LA11_7==RESETTOK||LA11_7==INT||LA11_7==FLOAT) ) {
                            alt11=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA11_8 = input.LA(4);

                        if ( (LA11_8==COLONTOK) ) {
                            alt11=1;
                        }
                        else if ( (LA11_8==UNIFORMTOK||LA11_8==RESETTOK||LA11_8==INT||LA11_8==FLOAT) ) {
                            alt11=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 4, input);

                        throw nvae;
                    }

                }
                else if ( (LA11_2==UNIFORMTOK||LA11_2==RESETTOK||LA11_2==INT||LA11_2==FLOAT) ) {
                    alt11=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 2, input);

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

                        if ( (LA11_6==COLONTOK) ) {
                            alt11=1;
                        }
                        else if ( (LA11_6==UNIFORMTOK||LA11_6==RESETTOK||LA11_6==INT||LA11_6==FLOAT) ) {
                            alt11=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA11_7 = input.LA(4);

                        if ( (LA11_7==COLONTOK) ) {
                            alt11=1;
                        }
                        else if ( (LA11_7==UNIFORMTOK||LA11_7==RESETTOK||LA11_7==INT||LA11_7==FLOAT) ) {
                            alt11=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA11_8 = input.LA(4);

                        if ( (LA11_8==COLONTOK) ) {
                            alt11=1;
                        }
                        else if ( (LA11_8==UNIFORMTOK||LA11_8==RESETTOK||LA11_8==INT||LA11_8==FLOAT) ) {
                            alt11=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 8, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 4, input);

                        throw nvae;
                    }

                }
                else if ( (LA11_3==UNIFORMTOK||LA11_3==RESETTOK||LA11_3==INT||LA11_3==FLOAT) ) {
                    alt11=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("291:1: obs_spec_tail : ( paction COLONTOK state COLONTOK obs prob | paction COLONTOK state u_matrix | paction u_matrix );", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:292:7: paction COLONTOK state COLONTOK obs prob
                    {
                    pushFollow(FOLLOW_paction_in_obs_spec_tail1658);
                    paction13=paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_spec_tail1660); 
                    pushFollow(FOLLOW_state_in_obs_spec_tail1662);
                    state14=state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_spec_tail1664); 
                    pushFollow(FOLLOW_obs_in_obs_spec_tail1666);
                    obs15=obs();
                    _fsp--;

                    pushFollow(FOLLOW_prob_in_obs_spec_tail1668);
                    prob16=prob();
                    _fsp--;


                                for(int a : paction13)
                                    for(int s2 : state14.l)
                                        for(int o : obs15.l)
                                            dotPomdpSpec.O[a].set(s2, o, prob16);
                            

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:300:7: paction COLONTOK state u_matrix
                    {
                    pushFollow(FOLLOW_paction_in_obs_spec_tail1695);
                    paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_obs_spec_tail1697); 
                    pushFollow(FOLLOW_state_in_obs_spec_tail1699);
                    state();
                    _fsp--;

                    pushFollow(FOLLOW_u_matrix_in_obs_spec_tail1701);
                    u_matrix();
                    _fsp--;

                    err("unsopported feature");

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:302:7: paction u_matrix
                    {
                    pushFollow(FOLLOW_paction_in_obs_spec_tail1719);
                    paction();
                    _fsp--;

                    pushFollow(FOLLOW_u_matrix_in_obs_spec_tail1721);
                    u_matrix();
                    _fsp--;

                    err("unsopported feature");

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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:306:1: reward_spec : RTOK COLONTOK reward_spec_tail ;
    public final void reward_spec() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:307:5: ( RTOK COLONTOK reward_spec_tail )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:307:7: RTOK COLONTOK reward_spec_tail
            {
            match(input,RTOK,FOLLOW_RTOK_in_reward_spec1752); 
            match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec1754); 
            pushFollow(FOLLOW_reward_spec_tail_in_reward_spec1756);
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );
    public final void reward_spec_tail() throws RecognitionException {
        state_return s_1 = null;

        state_return s_2 = null;

        obs_return obs17 = null;

        double number18 = 0.0;

        ArrayList<Integer> paction19 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:311:5: ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix )
            int alt12=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA12_5 = input.LA(4);

                        if ( (LA12_5==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA12_10 = input.LA(6);

                                if ( (LA12_10==COLONTOK) ) {
                                    alt12=1;
                                }
                                else if ( ((LA12_10>=PLUSTOK && LA12_10<=MINUSTOK)||LA12_10==INT||LA12_10==FLOAT) ) {
                                    alt12=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA12_11 = input.LA(6);

                                if ( ((LA12_11>=PLUSTOK && LA12_11<=MINUSTOK)||LA12_11==INT||LA12_11==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_11==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA12_12 = input.LA(6);

                                if ( ((LA12_12>=PLUSTOK && LA12_12<=MINUSTOK)||LA12_12==INT||LA12_12==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_12==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA12_5>=PLUSTOK && LA12_5<=MINUSTOK)||LA12_5==INT||LA12_5==FLOAT) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 5, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA12_6 = input.LA(4);

                        if ( (LA12_6==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA12_10 = input.LA(6);

                                if ( (LA12_10==COLONTOK) ) {
                                    alt12=1;
                                }
                                else if ( ((LA12_10>=PLUSTOK && LA12_10<=MINUSTOK)||LA12_10==INT||LA12_10==FLOAT) ) {
                                    alt12=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA12_11 = input.LA(6);

                                if ( ((LA12_11>=PLUSTOK && LA12_11<=MINUSTOK)||LA12_11==INT||LA12_11==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_11==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA12_12 = input.LA(6);

                                if ( ((LA12_12>=PLUSTOK && LA12_12<=MINUSTOK)||LA12_12==INT||LA12_12==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_12==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA12_6>=PLUSTOK && LA12_6<=MINUSTOK)||LA12_6==INT||LA12_6==FLOAT) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA12_7 = input.LA(4);

                        if ( (LA12_7==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA12_10 = input.LA(6);

                                if ( (LA12_10==COLONTOK) ) {
                                    alt12=1;
                                }
                                else if ( ((LA12_10>=PLUSTOK && LA12_10<=MINUSTOK)||LA12_10==INT||LA12_10==FLOAT) ) {
                                    alt12=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA12_11 = input.LA(6);

                                if ( ((LA12_11>=PLUSTOK && LA12_11<=MINUSTOK)||LA12_11==INT||LA12_11==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_11==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA12_12 = input.LA(6);

                                if ( ((LA12_12>=PLUSTOK && LA12_12<=MINUSTOK)||LA12_12==INT||LA12_12==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_12==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA12_7>=PLUSTOK && LA12_7<=MINUSTOK)||LA12_7==INT||LA12_7==FLOAT) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 1, input);

                    throw nvae;
                }
                }
                break;
            case STRING:
                {
                int LA12_2 = input.LA(2);

                if ( (LA12_2==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA12_5 = input.LA(4);

                        if ( (LA12_5==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA12_10 = input.LA(6);

                                if ( (LA12_10==COLONTOK) ) {
                                    alt12=1;
                                }
                                else if ( ((LA12_10>=PLUSTOK && LA12_10<=MINUSTOK)||LA12_10==INT||LA12_10==FLOAT) ) {
                                    alt12=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA12_11 = input.LA(6);

                                if ( ((LA12_11>=PLUSTOK && LA12_11<=MINUSTOK)||LA12_11==INT||LA12_11==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_11==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA12_12 = input.LA(6);

                                if ( ((LA12_12>=PLUSTOK && LA12_12<=MINUSTOK)||LA12_12==INT||LA12_12==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_12==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA12_5>=PLUSTOK && LA12_5<=MINUSTOK)||LA12_5==INT||LA12_5==FLOAT) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 5, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA12_6 = input.LA(4);

                        if ( (LA12_6==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA12_10 = input.LA(6);

                                if ( (LA12_10==COLONTOK) ) {
                                    alt12=1;
                                }
                                else if ( ((LA12_10>=PLUSTOK && LA12_10<=MINUSTOK)||LA12_10==INT||LA12_10==FLOAT) ) {
                                    alt12=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA12_11 = input.LA(6);

                                if ( ((LA12_11>=PLUSTOK && LA12_11<=MINUSTOK)||LA12_11==INT||LA12_11==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_11==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA12_12 = input.LA(6);

                                if ( ((LA12_12>=PLUSTOK && LA12_12<=MINUSTOK)||LA12_12==INT||LA12_12==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_12==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA12_6>=PLUSTOK && LA12_6<=MINUSTOK)||LA12_6==INT||LA12_6==FLOAT) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA12_7 = input.LA(4);

                        if ( (LA12_7==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA12_10 = input.LA(6);

                                if ( (LA12_10==COLONTOK) ) {
                                    alt12=1;
                                }
                                else if ( ((LA12_10>=PLUSTOK && LA12_10<=MINUSTOK)||LA12_10==INT||LA12_10==FLOAT) ) {
                                    alt12=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA12_11 = input.LA(6);

                                if ( ((LA12_11>=PLUSTOK && LA12_11<=MINUSTOK)||LA12_11==INT||LA12_11==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_11==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA12_12 = input.LA(6);

                                if ( ((LA12_12>=PLUSTOK && LA12_12<=MINUSTOK)||LA12_12==INT||LA12_12==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_12==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA12_7>=PLUSTOK && LA12_7<=MINUSTOK)||LA12_7==INT||LA12_7==FLOAT) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 2, input);

                    throw nvae;
                }
                }
                break;
            case ASTERICKTOK:
                {
                int LA12_3 = input.LA(2);

                if ( (LA12_3==COLONTOK) ) {
                    switch ( input.LA(3) ) {
                    case INT:
                        {
                        int LA12_5 = input.LA(4);

                        if ( (LA12_5==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA12_10 = input.LA(6);

                                if ( (LA12_10==COLONTOK) ) {
                                    alt12=1;
                                }
                                else if ( ((LA12_10>=PLUSTOK && LA12_10<=MINUSTOK)||LA12_10==INT||LA12_10==FLOAT) ) {
                                    alt12=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA12_11 = input.LA(6);

                                if ( ((LA12_11>=PLUSTOK && LA12_11<=MINUSTOK)||LA12_11==INT||LA12_11==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_11==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA12_12 = input.LA(6);

                                if ( ((LA12_12>=PLUSTOK && LA12_12<=MINUSTOK)||LA12_12==INT||LA12_12==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_12==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA12_5>=PLUSTOK && LA12_5<=MINUSTOK)||LA12_5==INT||LA12_5==FLOAT) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 5, input);

                            throw nvae;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA12_6 = input.LA(4);

                        if ( (LA12_6==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA12_10 = input.LA(6);

                                if ( (LA12_10==COLONTOK) ) {
                                    alt12=1;
                                }
                                else if ( ((LA12_10>=PLUSTOK && LA12_10<=MINUSTOK)||LA12_10==INT||LA12_10==FLOAT) ) {
                                    alt12=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA12_11 = input.LA(6);

                                if ( ((LA12_11>=PLUSTOK && LA12_11<=MINUSTOK)||LA12_11==INT||LA12_11==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_11==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA12_12 = input.LA(6);

                                if ( ((LA12_12>=PLUSTOK && LA12_12<=MINUSTOK)||LA12_12==INT||LA12_12==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_12==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA12_6>=PLUSTOK && LA12_6<=MINUSTOK)||LA12_6==INT||LA12_6==FLOAT) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 6, input);

                            throw nvae;
                        }
                        }
                        break;
                    case ASTERICKTOK:
                        {
                        int LA12_7 = input.LA(4);

                        if ( (LA12_7==COLONTOK) ) {
                            switch ( input.LA(5) ) {
                            case INT:
                                {
                                int LA12_10 = input.LA(6);

                                if ( (LA12_10==COLONTOK) ) {
                                    alt12=1;
                                }
                                else if ( ((LA12_10>=PLUSTOK && LA12_10<=MINUSTOK)||LA12_10==INT||LA12_10==FLOAT) ) {
                                    alt12=2;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 10, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case STRING:
                                {
                                int LA12_11 = input.LA(6);

                                if ( ((LA12_11>=PLUSTOK && LA12_11<=MINUSTOK)||LA12_11==INT||LA12_11==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_11==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 11, input);

                                    throw nvae;
                                }
                                }
                                break;
                            case ASTERICKTOK:
                                {
                                int LA12_12 = input.LA(6);

                                if ( ((LA12_12>=PLUSTOK && LA12_12<=MINUSTOK)||LA12_12==INT||LA12_12==FLOAT) ) {
                                    alt12=2;
                                }
                                else if ( (LA12_12==COLONTOK) ) {
                                    alt12=1;
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 12, input);

                                    throw nvae;
                                }
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 8, input);

                                throw nvae;
                            }

                        }
                        else if ( ((LA12_7>=PLUSTOK && LA12_7<=MINUSTOK)||LA12_7==INT||LA12_7==FLOAT) ) {
                            alt12=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 7, input);

                            throw nvae;
                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 4, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("310:1: reward_spec_tail : ( paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number | paction COLONTOK state COLONTOK state num_matrix | paction COLONTOK state num_matrix );", 12, 0, input);

                throw nvae;
            }

            switch (alt12) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:311:7: paction COLONTOK s_1= state COLONTOK s_2= state COLONTOK obs number
                    {
                    pushFollow(FOLLOW_paction_in_reward_spec_tail1774);
                    paction19=paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1776); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1780);
                    s_1=state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1782); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1786);
                    s_2=state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1788); 
                    pushFollow(FOLLOW_obs_in_reward_spec_tail1790);
                    obs17=obs();
                    _fsp--;

                    pushFollow(FOLLOW_number_in_reward_spec_tail1792);
                    number18=number();
                    _fsp--;

                                
                                if(input.toString(s_2.start,s_2.stop).compareTo(Character.toString('*'))!=0 || 
                                            input.toString(obs17.start,obs17.stop).compareTo(Character.toString('*'))!=0){
                                    err("We only allow for R(s,a) type rewards for now...");
                                    //System.out.println(input.toString(s_2.start,s_2.stop) + input.toString(obs17.start,obs17.stop));
                                }
                                //System.out.println("number is"+number18);
                                for(int a : paction19)
                                    for(int s1 : s_1.l) 
                                        dotPomdpSpec.R[a].set(s1, number18);                   
                            

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:324:7: paction COLONTOK state COLONTOK state num_matrix
                    {
                    pushFollow(FOLLOW_paction_in_reward_spec_tail1820);
                    paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1822); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1824);
                    state();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1826); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1828);
                    state();
                    _fsp--;

                    pushFollow(FOLLOW_num_matrix_in_reward_spec_tail1830);
                    num_matrix();
                    _fsp--;

                    err("unsopported feature");

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:326:7: paction COLONTOK state num_matrix
                    {
                    pushFollow(FOLLOW_paction_in_reward_spec_tail1848);
                    paction();
                    _fsp--;

                    match(input,COLONTOK,FOLLOW_COLONTOK_in_reward_spec_tail1850); 
                    pushFollow(FOLLOW_state_in_reward_spec_tail1852);
                    state();
                    _fsp--;

                    pushFollow(FOLLOW_num_matrix_in_reward_spec_tail1854);
                    num_matrix();
                    _fsp--;

                    err("unsopported feature");

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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:330:1: ui_matrix returns [DenseMatrix m] : ( UNIFORMTOK | IDENTITYTOK | prob_matrix );
    public final DenseMatrix ui_matrix() throws RecognitionException {
        DenseMatrix m = null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:331:5: ( UNIFORMTOK | IDENTITYTOK | prob_matrix )
            int alt13=3;
            switch ( input.LA(1) ) {
            case UNIFORMTOK:
                {
                alt13=1;
                }
                break;
            case IDENTITYTOK:
                {
                alt13=2;
                }
                break;
            case INT:
            case FLOAT:
                {
                alt13=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("330:1: ui_matrix returns [DenseMatrix m] : ( UNIFORMTOK | IDENTITYTOK | prob_matrix );", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:331:7: UNIFORMTOK
                    {
                    match(input,UNIFORMTOK,FOLLOW_UNIFORMTOK_in_ui_matrix1890); 

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:332:7: IDENTITYTOK
                    {
                    match(input,IDENTITYTOK,FOLLOW_IDENTITYTOK_in_ui_matrix1899); 
                    m = Matrices.identity(dotPomdpSpec.nrSta);

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:334:7: prob_matrix
                    {
                    pushFollow(FOLLOW_prob_matrix_in_ui_matrix1918);
                    prob_matrix();
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
        return m;
    }
    // $ANTLR end ui_matrix


    // $ANTLR start u_matrix
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:337:1: u_matrix : ( UNIFORMTOK | RESETTOK | prob_matrix );
    public final void u_matrix() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:338:5: ( UNIFORMTOK | RESETTOK | prob_matrix )
            int alt14=3;
            switch ( input.LA(1) ) {
            case UNIFORMTOK:
                {
                alt14=1;
                }
                break;
            case RESETTOK:
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
                    new NoViableAltException("337:1: u_matrix : ( UNIFORMTOK | RESETTOK | prob_matrix );", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:338:7: UNIFORMTOK
                    {
                    match(input,UNIFORMTOK,FOLLOW_UNIFORMTOK_in_u_matrix1937); 

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:339:7: RESETTOK
                    {
                    match(input,RESETTOK,FOLLOW_RESETTOK_in_u_matrix1946); 

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:340:7: prob_matrix
                    {
                    pushFollow(FOLLOW_prob_matrix_in_u_matrix1954);
                    prob_matrix();
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
    // $ANTLR end u_matrix


    // $ANTLR start prob_matrix
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:343:1: prob_matrix : ( prob )+ ;
    public final void prob_matrix() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:344:5: ( ( prob )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:344:7: ( prob )+
            {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:344:7: ( prob )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==INT||LA15_0==FLOAT) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:344:7: prob
            	    {
            	    pushFollow(FOLLOW_prob_in_prob_matrix1975);
            	    prob();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt15 >= 1 ) break loop15;
                        EarlyExitException eee =
                            new EarlyExitException(15, input);
                        throw eee;
                }
                cnt15++;
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
    // $ANTLR end prob_matrix


    // $ANTLR start prob_vector
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:347:1: prob_vector returns [SparseVector vector] : ( prob )+ ;
    public final SparseVector prob_vector() throws RecognitionException {
        SparseVector vector = null;

        double prob20 = 0.0;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:348:5: ( ( prob )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:350:9: ( prob )+
            {
            int index = 0; vector = new SparseVector(dotPomdpSpec.nrSta);
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:351:9: ( prob )+
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
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:351:10: prob
            	    {
            	    pushFollow(FOLLOW_prob_in_prob_vector2027);
            	    prob20=prob();
            	    _fsp--;


            	                // action here - the check for 0 actually doesn't matter
            	                if (prob20 > 0.0) vector.set(index, prob20);
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
        return vector;
    }
    // $ANTLR end prob_vector


    // $ANTLR start num_matrix
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:360:1: num_matrix : ( number )+ ;
    public final void num_matrix() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:361:5: ( ( number )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:361:7: ( number )+
            {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:361:7: ( number )+
            int cnt17=0;
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0>=PLUSTOK && LA17_0<=MINUSTOK)||LA17_0==INT||LA17_0==FLOAT) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:361:7: number
            	    {
            	    pushFollow(FOLLOW_number_in_num_matrix2071);
            	    number();
            	    _fsp--;


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
        return ;
    }
    // $ANTLR end num_matrix

    public static class state_return extends ParserRuleReturnScope {
        public ArrayList<Integer> l = new ArrayList<Integer>();
    };

    // $ANTLR start state
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:364:1: state returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );
    public final state_return state() throws RecognitionException {
        state_return retval = new state_return();
        retval.start = input.LT(1);

        Token INT21=null;
        Token STRING22=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:365:5: ( INT | STRING | ASTERICKTOK )
            int alt18=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt18=1;
                }
                break;
            case STRING:
                {
                alt18=2;
                }
                break;
            case ASTERICKTOK:
                {
                alt18=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("364:1: state returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:366:9: INT
                    {
                    INT21=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_state2101); 
                    retval.l.add(Integer.parseInt(INT21.getText()));

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:369:9: STRING
                    {
                    STRING22=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_state2129); 
                    retval.l.add(dotPomdpSpec.staList.indexOf(STRING22.getText()));

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:372:9: ASTERICKTOK
                    {
                    match(input,ASTERICKTOK,FOLLOW_ASTERICKTOK_in_state2156); 
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:376:1: paction returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );
    public final ArrayList<Integer> paction() throws RecognitionException {
        ArrayList<Integer> l =  new ArrayList<Integer>();

        Token INT23=null;
        Token STRING24=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:377:5: ( INT | STRING | ASTERICKTOK )
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
                    new NoViableAltException("376:1: paction returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:378:9: INT
                    {
                    INT23=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_paction2198); 
                    l.add(Integer.parseInt(INT23.getText()));

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:381:9: STRING
                    {
                    STRING24=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_paction2226); 
                    l.add(dotPomdpSpec.actList.indexOf(STRING24.getText()));

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:384:9: ASTERICKTOK
                    {
                    match(input,ASTERICKTOK,FOLLOW_ASTERICKTOK_in_paction2253); 
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:388:1: obs returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );
    public final obs_return obs() throws RecognitionException {
        obs_return retval = new obs_return();
        retval.start = input.LT(1);

        Token INT25=null;
        Token STRING26=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:389:5: ( INT | STRING | ASTERICKTOK )
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
                    new NoViableAltException("388:1: obs returns [ArrayList<Integer> l = new ArrayList<Integer>()] : ( INT | STRING | ASTERICKTOK );", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:390:9: INT
                    {
                    INT25=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_obs2296); 
                    retval.l.add(Integer.parseInt(INT25.getText()));

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:393:9: STRING
                    {
                    STRING26=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_obs2324); 
                    retval.l.add(dotPomdpSpec.obsList.indexOf(STRING26.getText()));

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:396:9: ASTERICKTOK
                    {
                    match(input,ASTERICKTOK,FOLLOW_ASTERICKTOK_in_obs2351); 
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:400:1: ident_list returns [ArrayList<String> list] : ( STRING )+ ;
    public final ArrayList<String> ident_list() throws RecognitionException {
        ArrayList<String> list = null;

        Token STRING27=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:401:5: ( ( STRING )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:402:9: ( STRING )+
            {
            list = new ArrayList<String>();
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:403:9: ( STRING )+
            int cnt21=0;
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==STRING) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:403:10: STRING
            	    {
            	    STRING27=(Token)input.LT(1);
            	    match(input,STRING,FOLLOW_STRING_in_ident_list2407); 
            	    list.add(STRING27.getText());

            	    }
            	    break;

            	default :
            	    if ( cnt21 >= 1 ) break loop21;
                        EarlyExitException eee =
                            new EarlyExitException(21, input);
                        throw eee;
                }
                cnt21++;
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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:408:1: prob returns [double p] : ( INT | FLOAT );
    public final double prob() throws RecognitionException {
        double p = 0.0;

        Token INT28=null;
        Token FLOAT29=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:409:5: ( INT | FLOAT )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==INT) ) {
                alt22=1;
            }
            else if ( (LA22_0==FLOAT) ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("408:1: prob returns [double p] : ( INT | FLOAT );", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:409:7: INT
                    {
                    INT28=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_prob2455); 
                    p = Double.parseDouble(INT28.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:411:7: FLOAT
                    {
                    FLOAT29=(Token)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_prob2473); 
                    p = Double.parseDouble(FLOAT29.getText());

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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:415:1: number returns [double n] : ( optional_sign INT | optional_sign FLOAT );
    public final double number() throws RecognitionException {
        double n = 0.0;

        Token INT31=null;
        Token FLOAT33=null;
        int optional_sign30 = 0;

        int optional_sign32 = 0;


        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:416:5: ( optional_sign INT | optional_sign FLOAT )
            int alt23=2;
            switch ( input.LA(1) ) {
            case PLUSTOK:
                {
                int LA23_1 = input.LA(2);

                if ( (LA23_1==FLOAT) ) {
                    alt23=2;
                }
                else if ( (LA23_1==INT) ) {
                    alt23=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("415:1: number returns [double n] : ( optional_sign INT | optional_sign FLOAT );", 23, 1, input);

                    throw nvae;
                }
                }
                break;
            case MINUSTOK:
                {
                int LA23_2 = input.LA(2);

                if ( (LA23_2==FLOAT) ) {
                    alt23=2;
                }
                else if ( (LA23_2==INT) ) {
                    alt23=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("415:1: number returns [double n] : ( optional_sign INT | optional_sign FLOAT );", 23, 2, input);

                    throw nvae;
                }
                }
                break;
            case INT:
                {
                alt23=1;
                }
                break;
            case FLOAT:
                {
                alt23=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("415:1: number returns [double n] : ( optional_sign INT | optional_sign FLOAT );", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:416:7: optional_sign INT
                    {
                    pushFollow(FOLLOW_optional_sign_in_number2516);
                    optional_sign30=optional_sign();
                    _fsp--;

                    INT31=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_number2518); 
                    n = optional_sign30 * Double.parseDouble(INT31.getText());

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:418:7: optional_sign FLOAT
                    {
                    pushFollow(FOLLOW_optional_sign_in_number2537);
                    optional_sign32=optional_sign();
                    _fsp--;

                    FLOAT33=(Token)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_number2539); 
                    n = optional_sign32 * Double.parseDouble(FLOAT33.getText());

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
    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:422:1: optional_sign returns [int s] : ( PLUSTOK | MINUSTOK | );
    public final int optional_sign() throws RecognitionException {
        int s = 0;

        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:423:5: ( PLUSTOK | MINUSTOK | )
            int alt24=3;
            switch ( input.LA(1) ) {
            case PLUSTOK:
                {
                alt24=1;
                }
                break;
            case MINUSTOK:
                {
                alt24=2;
                }
                break;
            case INT:
            case FLOAT:
                {
                alt24=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("422:1: optional_sign returns [int s] : ( PLUSTOK | MINUSTOK | );", 24, 0, input);

                throw nvae;
            }

            switch (alt24) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:423:7: PLUSTOK
                    {
                    match(input,PLUSTOK,FOLLOW_PLUSTOK_in_optional_sign2571); 
                    s = 1;

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:425:7: MINUSTOK
                    {
                    match(input,MINUSTOK,FOLLOW_MINUSTOK_in_optional_sign2589); 
                    s = -1;

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:428:9: 
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


 

    public static final BitSet FOLLOW_preamble_in_dotPomdp768 = new BitSet(new long[]{0x0000000000010E02L});
    public static final BitSet FOLLOW_start_state_in_dotPomdp786 = new BitSet(new long[]{0x0000000000000E02L});
    public static final BitSet FOLLOW_param_list_in_dotPomdp805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_param_type_in_preamble841 = new BitSet(new long[]{0x00000000000001F2L});
    public static final BitSet FOLLOW_discount_param_in_param_type873 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_param_in_param_type881 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_state_param_in_param_type889 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_action_param_in_param_type897 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_obs_param_in_param_type905 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISCOUNTTOK_in_discount_param925 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_discount_param927 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_FLOAT_in_discount_param929 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VALUESTOK_in_value_param959 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_value_param961 = new BitSet(new long[]{0x000000000000C000L});
    public static final BitSet FOLLOW_value_tail_in_value_param963 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_value_tail0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STATESTOK_in_state_param1016 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_state_param1018 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_state_tail_in_state_param1020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_state_tail1043 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ident_list_in_state_tail1070 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTIONSTOK_in_action_param1111 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_action_param1113 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_action_tail_in_action_param1115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_action_tail1138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ident_list_in_action_tail1165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OBSERVATIONSTOK_in_obs_param1202 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_param1204 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_obs_param_tail_in_obs_param1206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_obs_param_tail1224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ident_list_in_obs_param_tail1251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTTOK_in_start_state1292 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_start_state1294 = new BitSet(new long[]{0x000000000A000000L});
    public static final BitSet FOLLOW_prob_vector_in_start_state1296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTTOK_in_start_state1323 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_start_state1325 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_STRING_in_start_state1327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTTOK_in_start_state1345 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_INCLUDETOK_in_start_state1347 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_start_state1349 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_start_state_list_in_start_state1351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTTOK_in_start_state1370 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_EXCLUDETOK_in_start_state1372 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_start_state1374 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_start_state_list_in_start_state1376 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_state_in_start_state_list1417 = new BitSet(new long[]{0x0000000003200002L});
    public static final BitSet FOLLOW_param_spec_in_param_list1440 = new BitSet(new long[]{0x0000000000000E02L});
    public static final BitSet FOLLOW_trans_prob_spec_in_param_spec1463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_obs_prob_spec_in_param_spec1471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_reward_spec_in_param_spec1480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TTOK_in_trans_prob_spec1502 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_trans_prob_spec1504 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_trans_spec_tail_in_trans_prob_spec1506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_trans_spec_tail1528 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_trans_spec_tail1530 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_trans_spec_tail1534 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_trans_spec_tail1536 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_trans_spec_tail1540 = new BitSet(new long[]{0x000000000A000000L});
    public static final BitSet FOLLOW_prob_in_trans_spec_tail1542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_trans_spec_tail1570 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_trans_spec_tail1572 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_trans_spec_tail1574 = new BitSet(new long[]{0x000000000A081000L});
    public static final BitSet FOLLOW_u_matrix_in_trans_spec_tail1576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_trans_spec_tail1595 = new BitSet(new long[]{0x000000000A003000L});
    public static final BitSet FOLLOW_ui_matrix_in_trans_spec_tail1597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OTOK_in_obs_prob_spec1635 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_prob_spec1637 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_obs_spec_tail_in_obs_prob_spec1639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_obs_spec_tail1658 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_spec_tail1660 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_obs_spec_tail1662 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_spec_tail1664 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_obs_in_obs_spec_tail1666 = new BitSet(new long[]{0x000000000A000000L});
    public static final BitSet FOLLOW_prob_in_obs_spec_tail1668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_obs_spec_tail1695 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_obs_spec_tail1697 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_obs_spec_tail1699 = new BitSet(new long[]{0x000000000A081000L});
    public static final BitSet FOLLOW_u_matrix_in_obs_spec_tail1701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_obs_spec_tail1719 = new BitSet(new long[]{0x000000000A081000L});
    public static final BitSet FOLLOW_u_matrix_in_obs_spec_tail1721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RTOK_in_reward_spec1752 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec1754 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_reward_spec_tail_in_reward_spec1756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_reward_spec_tail1774 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1776 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1780 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1782 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1786 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1788 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_obs_in_reward_spec_tail1790 = new BitSet(new long[]{0x0000000002C00000L});
    public static final BitSet FOLLOW_number_in_reward_spec_tail1792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_reward_spec_tail1820 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1822 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1824 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1826 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1828 = new BitSet(new long[]{0x0000000002C00000L});
    public static final BitSet FOLLOW_num_matrix_in_reward_spec_tail1830 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paction_in_reward_spec_tail1848 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_COLONTOK_in_reward_spec_tail1850 = new BitSet(new long[]{0x0000000003200000L});
    public static final BitSet FOLLOW_state_in_reward_spec_tail1852 = new BitSet(new long[]{0x0000000002C00000L});
    public static final BitSet FOLLOW_num_matrix_in_reward_spec_tail1854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNIFORMTOK_in_ui_matrix1890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTITYTOK_in_ui_matrix1899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_prob_matrix_in_ui_matrix1918 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNIFORMTOK_in_u_matrix1937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RESETTOK_in_u_matrix1946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_prob_matrix_in_u_matrix1954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_prob_in_prob_matrix1975 = new BitSet(new long[]{0x000000000A000002L});
    public static final BitSet FOLLOW_prob_in_prob_vector2027 = new BitSet(new long[]{0x000000000A000002L});
    public static final BitSet FOLLOW_number_in_num_matrix2071 = new BitSet(new long[]{0x0000000002C00002L});
    public static final BitSet FOLLOW_INT_in_state2101 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_state2129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASTERICKTOK_in_state2156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_paction2198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_paction2226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASTERICKTOK_in_paction2253 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_obs2296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_obs2324 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASTERICKTOK_in_obs2351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_ident_list2407 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_INT_in_prob2455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_prob2473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_optional_sign_in_number2516 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_INT_in_number2518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_optional_sign_in_number2537 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_FLOAT_in_number2539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSTOK_in_optional_sign2571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUSTOK_in_optional_sign2589 = new BitSet(new long[]{0x0000000000000002L});

}