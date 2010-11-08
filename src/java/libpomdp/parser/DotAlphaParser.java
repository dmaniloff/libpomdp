// $ANTLR 3.0.1 /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g 2010-10-20 11:54:32

    package libpomdp.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: DotAlpha.g
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */
public class DotAlphaParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "INT", "EXPONENT", "FLOAT", "WS", "'+'", "'-'"
    };
    public static final int EXPONENT=5;
    public static final int WS=7;
    public static final int INT=4;
    public static final int FLOAT=6;
    public static final int EOF=-1;

        public DotAlphaParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "/home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g"; }


        // main method
        public static void main(String[] args) throws Exception {
            DotAlphaLexer lex = new DotAlphaLexer(new ANTLRFileStream(args[0]));
           	CommonTokenStream tokens = new CommonTokenStream(lex);
            DotAlphaParser parser = new DotAlphaParser(tokens);

            try {
                parser.dotAlpha();
            } catch (RecognitionException e)  {
                e.printStackTrace();
            }
        }

        // main structures
        private ArrayList<Integer> actions = new ArrayList<Integer>();
        private ArrayList<Double[]> alphas = new ArrayList<Double[]>();    

        // return main structure
        public Integer[] getActions() {
            return actions.toArray(new Integer[1]);
        }

        public Double[][] getAlphas() {
            return alphas.toArray(new Double[1][1]);
        }

        // simple debug mesg
        private void err(String msg) {
            System.err.println(msg);
        }



    // $ANTLR start dotAlpha
    // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:86:1: dotAlpha : ( action_decl alpha_vector )+ ;
    public final void dotAlpha() throws RecognitionException {
        int action_decl1 = 0;

        ArrayList<Double> alpha_vector2 = null;


        try {
            // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:87:5: ( ( action_decl alpha_vector )+ )
            // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:88:9: ( action_decl alpha_vector )+
            {
            // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:88:9: ( action_decl alpha_vector )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==INT) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:89:13: action_decl alpha_vector
            	    {
            	    pushFollow(FOLLOW_action_decl_in_dotAlpha287);
            	    action_decl1=action_decl();
            	    _fsp--;


            	                    // each action seen is added to the list
            	                    actions.add(action_decl1);
            	                
            	    pushFollow(FOLLOW_alpha_vector_in_dotAlpha316);
            	    alpha_vector2=alpha_vector();
            	    _fsp--;


            	                    // each full alpha vector seen is converted to an array and
            	                    // added to the matrix
            	                    alphas.add(alpha_vector2.toArray(new Double[1]));
            	                

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
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
    // $ANTLR end dotAlpha


    // $ANTLR start action_decl
    // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:103:1: action_decl returns [int a] : INT ;
    public final int action_decl() throws RecognitionException {
        int a = 0;

        Token INT3=null;

        try {
            // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:104:5: ( INT )
            // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:104:9: INT
            {
            INT3=(Token)input.LT(1);
            match(input,INT,FOLLOW_INT_in_action_decl368); 
            a = Integer.parseInt(INT3.getText());

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return a;
    }
    // $ANTLR end action_decl


    // $ANTLR start alpha_vector
    // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:108:1: alpha_vector returns [ArrayList<Double> v = new ArrayList<Double>()] : ( optional_sign FLOAT )+ ;
    public final ArrayList<Double> alpha_vector() throws RecognitionException {
        ArrayList<Double> v =  new ArrayList<Double>();

        Token FLOAT5=null;
        int optional_sign4 = 0;


        try {
            // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:109:5: ( ( optional_sign FLOAT )+ )
            // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:109:9: ( optional_sign FLOAT )+
            {
            // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:109:9: ( optional_sign FLOAT )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==FLOAT||(LA2_0>=8 && LA2_0<=9)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:109:10: optional_sign FLOAT
            	    {
            	    pushFollow(FOLLOW_optional_sign_in_alpha_vector402);
            	    optional_sign4=optional_sign();
            	    _fsp--;

            	    FLOAT5=(Token)input.LT(1);
            	    match(input,FLOAT,FOLLOW_FLOAT_in_alpha_vector404); 

            	                v.add(optional_sign4 * Double.parseDouble(FLOAT5.getText()));
            	            

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return v;
    }
    // $ANTLR end alpha_vector


    // $ANTLR start optional_sign
    // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:117:1: optional_sign returns [int s] : ( '+' | '-' | );
    public final int optional_sign() throws RecognitionException {
        int s = 0;

        try {
            // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:118:5: ( '+' | '-' | )
            int alt3=3;
            switch ( input.LA(1) ) {
            case 8:
                {
                alt3=1;
                }
                break;
            case 9:
                {
                alt3=2;
                }
                break;
            case FLOAT:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("117:1: optional_sign returns [int s] : ( '+' | '-' | );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:118:7: '+'
                    {
                    match(input,8,FOLLOW_8_in_optional_sign455); 
                    s = 1;

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:120:7: '-'
                    {
                    match(input,9,FOLLOW_9_in_optional_sign473); 
                    s = -1;

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/java/libpomdp/parser/DotAlpha.g:123:9: 
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


 

    public static final BitSet FOLLOW_action_decl_in_dotAlpha287 = new BitSet(new long[]{0x0000000000000340L});
    public static final BitSet FOLLOW_alpha_vector_in_dotAlpha316 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_INT_in_action_decl368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_optional_sign_in_alpha_vector402 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_FLOAT_in_alpha_vector404 = new BitSet(new long[]{0x0000000000000342L});
    public static final BitSet FOLLOW_8_in_optional_sign455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_9_in_optional_sign473 = new BitSet(new long[]{0x0000000000000002L});

}