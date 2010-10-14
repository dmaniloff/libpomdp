// $ANTLR 3.0.1 /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g 2010-10-14 14:45:57

    package libpomdp.parser.java;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class DotPomdpLexer extends Lexer {
    public static final int IDENTITYTOK=13;
    public static final int EXPONENT=26;
    public static final int OTOK=10;
    public static final int ACTIONSTOK=7;
    public static final int COSTTOK=15;
    public static final int STARTTOK=16;
    public static final int DISCOUNTTOK=4;
    public static final int MINUSTOK=23;
    public static final int FLOAT=27;
    public static final int INT=25;
    public static final int VALUESTOK=5;
    public static final int INCLUDETOK=17;
    public static final int Tokens=30;
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
    public static final int RTOK=11;
    public static final int COMMENT=28;
    public static final int STATESTOK=6;
    public static final int STRING=24;
    public static final int RESETTOK=19;
    public DotPomdpLexer() {;} 
    public DotPomdpLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g"; }

    // $ANTLR start DISCOUNTTOK
    public final void mDISCOUNTTOK() throws RecognitionException {
        try {
            int _type = DISCOUNTTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:6:13: ( 'discount' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:6:15: 'discount'
            {
            match("discount"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DISCOUNTTOK

    // $ANTLR start VALUESTOK
    public final void mVALUESTOK() throws RecognitionException {
        try {
            int _type = VALUESTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:7:11: ( 'values' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:7:13: 'values'
            {
            match("values"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end VALUESTOK

    // $ANTLR start STATESTOK
    public final void mSTATESTOK() throws RecognitionException {
        try {
            int _type = STATESTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:8:11: ( 'states' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:8:13: 'states'
            {
            match("states"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STATESTOK

    // $ANTLR start ACTIONSTOK
    public final void mACTIONSTOK() throws RecognitionException {
        try {
            int _type = ACTIONSTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:9:12: ( 'actions' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:9:14: 'actions'
            {
            match("actions"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ACTIONSTOK

    // $ANTLR start OBSERVATIONSTOK
    public final void mOBSERVATIONSTOK() throws RecognitionException {
        try {
            int _type = OBSERVATIONSTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:10:17: ( 'observations' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:10:19: 'observations'
            {
            match("observations"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OBSERVATIONSTOK

    // $ANTLR start TTOK
    public final void mTTOK() throws RecognitionException {
        try {
            int _type = TTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:11:6: ( 'T' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:11:8: 'T'
            {
            match('T'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TTOK

    // $ANTLR start OTOK
    public final void mOTOK() throws RecognitionException {
        try {
            int _type = OTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:12:6: ( 'O' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:12:8: 'O'
            {
            match('O'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OTOK

    // $ANTLR start RTOK
    public final void mRTOK() throws RecognitionException {
        try {
            int _type = RTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:13:6: ( 'R' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:13:8: 'R'
            {
            match('R'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RTOK

    // $ANTLR start UNIFORMTOK
    public final void mUNIFORMTOK() throws RecognitionException {
        try {
            int _type = UNIFORMTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:14:12: ( 'uniform' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:14:14: 'uniform'
            {
            match("uniform"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end UNIFORMTOK

    // $ANTLR start IDENTITYTOK
    public final void mIDENTITYTOK() throws RecognitionException {
        try {
            int _type = IDENTITYTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:15:13: ( 'identity' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:15:15: 'identity'
            {
            match("identity"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IDENTITYTOK

    // $ANTLR start REWARDTOK
    public final void mREWARDTOK() throws RecognitionException {
        try {
            int _type = REWARDTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:16:11: ( 'reward' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:16:13: 'reward'
            {
            match("reward"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end REWARDTOK

    // $ANTLR start COSTTOK
    public final void mCOSTTOK() throws RecognitionException {
        try {
            int _type = COSTTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:17:9: ( 'cost' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:17:11: 'cost'
            {
            match("cost"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COSTTOK

    // $ANTLR start STARTTOK
    public final void mSTARTTOK() throws RecognitionException {
        try {
            int _type = STARTTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:18:10: ( 'start' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:18:12: 'start'
            {
            match("start"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STARTTOK

    // $ANTLR start INCLUDETOK
    public final void mINCLUDETOK() throws RecognitionException {
        try {
            int _type = INCLUDETOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:19:12: ( 'include' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:19:14: 'include'
            {
            match("include"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INCLUDETOK

    // $ANTLR start EXCLUDETOK
    public final void mEXCLUDETOK() throws RecognitionException {
        try {
            int _type = EXCLUDETOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:20:12: ( 'exclude' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:20:14: 'exclude'
            {
            match("exclude"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EXCLUDETOK

    // $ANTLR start RESETTOK
    public final void mRESETTOK() throws RecognitionException {
        try {
            int _type = RESETTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:21:10: ( 'reset' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:21:12: 'reset'
            {
            match("reset"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RESETTOK

    // $ANTLR start COLONTOK
    public final void mCOLONTOK() throws RecognitionException {
        try {
            int _type = COLONTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:22:10: ( ':' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:22:12: ':'
            {
            match(':'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COLONTOK

    // $ANTLR start ASTERICKTOK
    public final void mASTERICKTOK() throws RecognitionException {
        try {
            int _type = ASTERICKTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:23:13: ( '*' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:23:15: '*'
            {
            match('*'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ASTERICKTOK

    // $ANTLR start PLUSTOK
    public final void mPLUSTOK() throws RecognitionException {
        try {
            int _type = PLUSTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:24:9: ( '+' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:24:11: '+'
            {
            match('+'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PLUSTOK

    // $ANTLR start MINUSTOK
    public final void mMINUSTOK() throws RecognitionException {
        try {
            int _type = MINUSTOK;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:25:10: ( '-' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:25:12: '-'
            {
            match('-'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end MINUSTOK

    // $ANTLR start STRING
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:101:5: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )* )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:101:9: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:101:29: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='-'||(LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STRING

    // $ANTLR start INT
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:105:5: ( '0' | ( '1' .. '9' ) ( '0' .. '9' )* )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='0') ) {
                alt3=1;
            }
            else if ( ((LA3_0>='1' && LA3_0<='9')) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("104:1: INT : ( '0' | ( '1' .. '9' ) ( '0' .. '9' )* );", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:105:9: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:105:15: ( '1' .. '9' ) ( '0' .. '9' )*
                    {
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:105:15: ( '1' .. '9' )
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:105:16: '1' .. '9'
                    {
                    matchRange('1','9'); 

                    }

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:105:26: ( '0' .. '9' )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:105:27: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INT

    // $ANTLR start FLOAT
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:109:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
            int alt10=3;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:109:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )?
                    {
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:109:9: ( '0' .. '9' )+
                    int cnt4=0;
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:109:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt4 >= 1 ) break loop4;
                                EarlyExitException eee =
                                    new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);

                    match('.'); 
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:109:25: ( '0' .. '9' )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:109:26: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:109:37: ( EXPONENT )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0=='E'||LA6_0=='e') ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:109:37: EXPONENT
                            {
                            mEXPONENT(); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:110:9: '.' ( '0' .. '9' )+ ( EXPONENT )?
                    {
                    match('.'); 
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:110:13: ( '0' .. '9' )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:110:14: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

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

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:110:25: ( EXPONENT )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='E'||LA8_0=='e') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:110:25: EXPONENT
                            {
                            mEXPONENT(); 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:111:9: ( '0' .. '9' )+ EXPONENT
                    {
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:111:9: ( '0' .. '9' )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:111:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt9 >= 1 ) break loop9;
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);

                    mEXPONENT(); 

                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FLOAT

    // $ANTLR start COMMENT
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:115:5: ( '#' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' )
            int alt16=3;
            int LA16_0 = input.LA(1);

            if ( (LA16_0=='#') ) {
                alt16=1;
            }
            else if ( (LA16_0=='/') ) {
                int LA16_2 = input.LA(2);

                if ( (LA16_2=='*') ) {
                    alt16=3;
                }
                else if ( (LA16_2=='/') ) {
                    alt16=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("114:1: COMMENT : ( '#' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' );", 16, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("114:1: COMMENT : ( '#' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' );", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:115:9: '#' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match('#'); 
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:115:13: (~ ( '\\n' | '\\r' ) )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0>='\u0000' && LA11_0<='\t')||(LA11_0>='\u000B' && LA11_0<='\f')||(LA11_0>='\u000E' && LA11_0<='\uFFFE')) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:115:13: ~ ( '\\n' | '\\r' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:115:27: ( '\\r' )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0=='\r') ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:115:27: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 
                    channel=HIDDEN;

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:116:9: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match("//"); 

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:116:14: (~ ( '\\n' | '\\r' ) )*
                    loop13:
                    do {
                        int alt13=2;
                        int LA13_0 = input.LA(1);

                        if ( ((LA13_0>='\u0000' && LA13_0<='\t')||(LA13_0>='\u000B' && LA13_0<='\f')||(LA13_0>='\u000E' && LA13_0<='\uFFFE')) ) {
                            alt13=1;
                        }


                        switch (alt13) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:116:14: ~ ( '\\n' | '\\r' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop13;
                        }
                    } while (true);

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:116:28: ( '\\r' )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0=='\r') ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:116:28: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 
                    channel=HIDDEN;

                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:117:9: '/*' ( options {greedy=false; } : . )* '*/'
                    {
                    match("/*"); 

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:117:14: ( options {greedy=false; } : . )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0=='*') ) {
                            int LA15_1 = input.LA(2);

                            if ( (LA15_1=='/') ) {
                                alt15=2;
                            }
                            else if ( ((LA15_1>='\u0000' && LA15_1<='.')||(LA15_1>='0' && LA15_1<='\uFFFE')) ) {
                                alt15=1;
                            }


                        }
                        else if ( ((LA15_0>='\u0000' && LA15_0<=')')||(LA15_0>='+' && LA15_0<='\uFFFE')) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:117:42: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);

                    match("*/"); 

                    channel=HIDDEN;

                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMENT

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:121:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:121:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            channel=HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WS

    // $ANTLR start EXPONENT
    public final void mEXPONENT() throws RecognitionException {
        try {
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:130:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:130:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:130:22: ( '+' | '-' )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0=='+'||LA17_0=='-') ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;

            }

            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:130:33: ( '0' .. '9' )+
            int cnt18=0;
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0>='0' && LA18_0<='9')) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:130:34: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

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
        finally {
        }
    }
    // $ANTLR end EXPONENT

    public void mTokens() throws RecognitionException {
        // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:8: ( DISCOUNTTOK | VALUESTOK | STATESTOK | ACTIONSTOK | OBSERVATIONSTOK | TTOK | OTOK | RTOK | UNIFORMTOK | IDENTITYTOK | REWARDTOK | COSTTOK | STARTTOK | INCLUDETOK | EXCLUDETOK | RESETTOK | COLONTOK | ASTERICKTOK | PLUSTOK | MINUSTOK | STRING | INT | FLOAT | COMMENT | WS )
        int alt19=25;
        alt19 = dfa19.predict(input);
        switch (alt19) {
            case 1 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:10: DISCOUNTTOK
                {
                mDISCOUNTTOK(); 

                }
                break;
            case 2 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:22: VALUESTOK
                {
                mVALUESTOK(); 

                }
                break;
            case 3 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:32: STATESTOK
                {
                mSTATESTOK(); 

                }
                break;
            case 4 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:42: ACTIONSTOK
                {
                mACTIONSTOK(); 

                }
                break;
            case 5 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:53: OBSERVATIONSTOK
                {
                mOBSERVATIONSTOK(); 

                }
                break;
            case 6 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:69: TTOK
                {
                mTTOK(); 

                }
                break;
            case 7 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:74: OTOK
                {
                mOTOK(); 

                }
                break;
            case 8 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:79: RTOK
                {
                mRTOK(); 

                }
                break;
            case 9 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:84: UNIFORMTOK
                {
                mUNIFORMTOK(); 

                }
                break;
            case 10 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:95: IDENTITYTOK
                {
                mIDENTITYTOK(); 

                }
                break;
            case 11 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:107: REWARDTOK
                {
                mREWARDTOK(); 

                }
                break;
            case 12 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:117: COSTTOK
                {
                mCOSTTOK(); 

                }
                break;
            case 13 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:125: STARTTOK
                {
                mSTARTTOK(); 

                }
                break;
            case 14 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:134: INCLUDETOK
                {
                mINCLUDETOK(); 

                }
                break;
            case 15 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:145: EXCLUDETOK
                {
                mEXCLUDETOK(); 

                }
                break;
            case 16 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:156: RESETTOK
                {
                mRESETTOK(); 

                }
                break;
            case 17 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:165: COLONTOK
                {
                mCOLONTOK(); 

                }
                break;
            case 18 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:174: ASTERICKTOK
                {
                mASTERICKTOK(); 

                }
                break;
            case 19 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:186: PLUSTOK
                {
                mPLUSTOK(); 

                }
                break;
            case 20 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:194: MINUSTOK
                {
                mMINUSTOK(); 

                }
                break;
            case 21 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:203: STRING
                {
                mSTRING(); 

                }
                break;
            case 22 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:210: INT
                {
                mINT(); 

                }
                break;
            case 23 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:214: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 24 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:220: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 25 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g:1:228: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    protected DFA19 dfa19 = new DFA19(this);
    static final String DFA10_eotS =
        "\5\uffff";
    static final String DFA10_eofS =
        "\5\uffff";
    static final String DFA10_minS =
        "\2\56\3\uffff";
    static final String DFA10_maxS =
        "\1\71\1\145\3\uffff";
    static final String DFA10_acceptS =
        "\2\uffff\1\2\1\3\1\1";
    static final String DFA10_specialS =
        "\5\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\4\1\uffff\12\1\13\uffff\1\3\37\uffff\1\3",
            "",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "108:1: FLOAT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT );";
        }
    }
    static final String DFA19_eotS =
        "\1\uffff\5\22\1\35\1\36\1\37\5\22\5\uffff\2\46\3\uffff\5\22\3\uffff"+
        "\6\22\1\uffff\1\46\27\22\1\114\3\22\1\120\6\22\1\127\1\22\1\uffff"+
        "\2\22\1\133\1\uffff\1\134\5\22\1\uffff\1\142\2\22\2\uffff\1\145"+
        "\1\22\1\147\1\150\1\22\1\uffff\1\152\1\153\1\uffff\1\22\2\uffff"+
        "\1\155\2\uffff\1\22\1\uffff\2\22\1\161\1\uffff";
    static final String DFA19_eofS =
        "\162\uffff";
    static final String DFA19_minS =
        "\1\11\1\151\1\141\1\164\1\143\1\142\3\55\1\156\1\144\1\145\1\157"+
        "\1\170\5\uffff\2\56\3\uffff\1\163\1\154\1\141\1\164\1\163\3\uffff"+
        "\1\151\1\143\1\145\2\163\1\143\1\uffff\1\56\1\143\1\165\1\162\1"+
        "\151\1\145\1\146\1\154\1\156\1\145\1\141\1\164\1\154\1\157\1\145"+
        "\1\164\1\145\1\157\1\162\1\157\1\165\2\164\1\162\1\55\2\165\1\163"+
        "\1\55\1\163\1\156\1\166\1\162\1\144\1\151\1\55\1\144\1\uffff\1\144"+
        "\1\156\1\55\1\uffff\1\55\1\163\1\141\1\155\1\145\1\164\1\uffff\1"+
        "\55\1\145\1\164\2\uffff\1\55\1\164\2\55\1\171\1\uffff\2\55\1\uffff"+
        "\1\151\2\uffff\1\55\2\uffff\1\157\1\uffff\1\156\1\163\1\55\1\uffff";
    static final String DFA19_maxS =
        "\1\172\1\151\1\141\1\164\1\143\1\142\3\172\2\156\1\145\1\157\1\170"+
        "\5\uffff\2\145\3\uffff\1\163\1\154\1\141\1\164\1\163\3\uffff\1\151"+
        "\1\143\1\145\1\167\1\163\1\143\1\uffff\1\145\1\143\1\165\1\164\1"+
        "\151\1\145\1\146\1\154\1\156\1\145\1\141\1\164\1\154\1\157\1\145"+
        "\1\164\1\145\1\157\1\162\1\157\1\165\2\164\1\162\1\172\2\165\1\163"+
        "\1\172\1\163\1\156\1\166\1\162\1\144\1\151\1\172\1\144\1\uffff\1"+
        "\144\1\156\1\172\1\uffff\1\172\1\163\1\141\1\155\1\145\1\164\1\uffff"+
        "\1\172\1\145\1\164\2\uffff\1\172\1\164\2\172\1\171\1\uffff\2\172"+
        "\1\uffff\1\151\2\uffff\1\172\2\uffff\1\157\1\uffff\1\156\1\163\1"+
        "\172\1\uffff";
    static final String DFA19_acceptS =
        "\16\uffff\1\21\1\22\1\23\1\24\1\25\2\uffff\1\27\1\30\1\31\5\uffff"+
        "\1\6\1\7\1\10\6\uffff\1\26\45\uffff\1\14\3\uffff\1\15\6\uffff\1"+
        "\20\3\uffff\1\2\1\3\5\uffff\1\13\2\uffff\1\4\1\uffff\1\11\1\16\1"+
        "\uffff\1\17\1\1\1\uffff\1\12\3\uffff\1\5";
    static final String DFA19_specialS =
        "\162\uffff}>";
    static final String[] DFA19_transitionS = {
            "\2\27\2\uffff\1\27\22\uffff\1\27\2\uffff\1\26\6\uffff\1\17\1"+
            "\20\1\uffff\1\21\1\25\1\26\1\23\11\24\1\16\6\uffff\16\22\1\7"+
            "\2\22\1\10\1\22\1\6\6\22\6\uffff\1\4\1\22\1\14\1\1\1\15\3\22"+
            "\1\12\5\22\1\5\2\22\1\13\1\3\1\22\1\11\1\2\4\22",
            "\1\30",
            "\1\31",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\40",
            "\1\42\11\uffff\1\41",
            "\1\43",
            "\1\44",
            "\1\45",
            "",
            "",
            "",
            "",
            "",
            "\1\25\1\uffff\12\25\13\uffff\1\25\37\uffff\1\25",
            "\1\25\1\uffff\12\47\13\uffff\1\25\37\uffff\1\25",
            "",
            "",
            "",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "",
            "",
            "",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\60\3\uffff\1\61",
            "\1\62",
            "\1\63",
            "",
            "\1\25\1\uffff\12\47\13\uffff\1\25\37\uffff\1\25",
            "\1\64",
            "\1\65",
            "\1\66\1\uffff\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "\1\126",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\130",
            "",
            "\1\131",
            "\1\132",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\143",
            "\1\144",
            "",
            "",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\146",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\151",
            "",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\1\154",
            "",
            "",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "",
            "\1\156",
            "",
            "\1\157",
            "\1\160",
            "\1\22\2\uffff\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            ""
    };

    static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
    static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
    static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
    static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
    static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
    static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
    static final short[][] DFA19_transition;

    static {
        int numStates = DFA19_transitionS.length;
        DFA19_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
        }
    }

    class DFA19 extends DFA {

        public DFA19(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 19;
            this.eot = DFA19_eot;
            this.eof = DFA19_eof;
            this.min = DFA19_min;
            this.max = DFA19_max;
            this.accept = DFA19_accept;
            this.special = DFA19_special;
            this.transition = DFA19_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( DISCOUNTTOK | VALUESTOK | STATESTOK | ACTIONSTOK | OBSERVATIONSTOK | TTOK | OTOK | RTOK | UNIFORMTOK | IDENTITYTOK | REWARDTOK | COSTTOK | STARTTOK | INCLUDETOK | EXCLUDETOK | RESETTOK | COLONTOK | ASTERICKTOK | PLUSTOK | MINUSTOK | STRING | INT | FLOAT | COMMENT | WS );";
        }
    }
 

}