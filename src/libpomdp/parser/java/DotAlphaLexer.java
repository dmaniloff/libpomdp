// $ANTLR 3.0.1 /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g 2010-09-29 15:22:42

    package libpomdp.parser.java;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class DotAlphaLexer extends Lexer {
    public static final int EXPONENT=5;
    public static final int WS=7;
    public static final int INT=4;
    public static final int FLOAT=6;
    public static final int T8=8;
    public static final int T9=9;
    public static final int Tokens=10;
    public static final int EOF=-1;
    public DotAlphaLexer() {;} 
    public DotAlphaLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g"; }

    // $ANTLR start T8
    public final void mT8() throws RecognitionException {
        try {
            int _type = T8;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:6:4: ( '+' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:6:6: '+'
            {
            match('+'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T8

    // $ANTLR start T9
    public final void mT9() throws RecognitionException {
        try {
            int _type = T9;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:7:4: ( '-' )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:7:6: '-'
            {
            match('-'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T9

    // $ANTLR start INT
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:64:5: ( '0' | ( '1' .. '9' ) ( '0' .. '9' )* )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='0') ) {
                alt2=1;
            }
            else if ( ((LA2_0>='1' && LA2_0<='9')) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("63:1: INT : ( '0' | ( '1' .. '9' ) ( '0' .. '9' )* );", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:64:9: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:64:15: ( '1' .. '9' ) ( '0' .. '9' )*
                    {
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:64:15: ( '1' .. '9' )
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:64:16: '1' .. '9'
                    {
                    matchRange('1','9'); 

                    }

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:64:26: ( '0' .. '9' )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( ((LA1_0>='0' && LA1_0<='9')) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:64:27: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop1;
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
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:68:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
            int alt9=3;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:68:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )?
                    {
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:68:9: ( '0' .. '9' )+
                    int cnt3=0;
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:68:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt3 >= 1 ) break loop3;
                                EarlyExitException eee =
                                    new EarlyExitException(3, input);
                                throw eee;
                        }
                        cnt3++;
                    } while (true);

                    match('.'); 
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:68:25: ( '0' .. '9' )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:68:26: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:68:37: ( EXPONENT )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0=='E'||LA5_0=='e') ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:68:37: EXPONENT
                            {
                            mEXPONENT(); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:69:9: '.' ( '0' .. '9' )+ ( EXPONENT )?
                    {
                    match('.'); 
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:69:13: ( '0' .. '9' )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:69:14: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt6 >= 1 ) break loop6;
                                EarlyExitException eee =
                                    new EarlyExitException(6, input);
                                throw eee;
                        }
                        cnt6++;
                    } while (true);

                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:69:25: ( EXPONENT )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0=='E'||LA7_0=='e') ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:69:25: EXPONENT
                            {
                            mEXPONENT(); 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:70:9: ( '0' .. '9' )+ EXPONENT
                    {
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:70:9: ( '0' .. '9' )+
                    int cnt8=0;
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:70:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

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

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:73:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:73:9: ( ' ' | '\\t' | '\\r' | '\\n' )
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
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:81:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:81:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:81:22: ( '+' | '-' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='+'||LA10_0=='-') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:
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

            // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:81:33: ( '0' .. '9' )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>='0' && LA11_0<='9')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:81:34: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end EXPONENT

    public void mTokens() throws RecognitionException {
        // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:1:8: ( T8 | T9 | INT | FLOAT | WS )
        int alt12=5;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:1:10: T8
                {
                mT8(); 

                }
                break;
            case 2 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:1:13: T9
                {
                mT9(); 

                }
                break;
            case 3 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:1:16: INT
                {
                mINT(); 

                }
                break;
            case 4 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:1:20: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 5 :
                // /home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g:1:26: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA9_eotS =
        "\5\uffff";
    static final String DFA9_eofS =
        "\5\uffff";
    static final String DFA9_minS =
        "\2\56\3\uffff";
    static final String DFA9_maxS =
        "\1\71\1\145\3\uffff";
    static final String DFA9_acceptS =
        "\2\uffff\1\2\1\3\1\1";
    static final String DFA9_specialS =
        "\5\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\4\1\uffff\12\1\13\uffff\1\3\37\uffff\1\3",
            "",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "67:1: FLOAT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT );";
        }
    }
    static final String DFA12_eotS =
        "\3\uffff\2\7\3\uffff\1\7";
    static final String DFA12_eofS =
        "\11\uffff";
    static final String DFA12_minS =
        "\1\11\2\uffff\2\56\3\uffff\1\56";
    static final String DFA12_maxS =
        "\1\71\2\uffff\2\145\3\uffff\1\145";
    static final String DFA12_acceptS =
        "\1\uffff\1\1\1\2\2\uffff\1\4\1\5\1\3\1\uffff";
    static final String DFA12_specialS =
        "\11\uffff}>";
    static final String[] DFA12_transitionS = {
            "\2\6\2\uffff\1\6\22\uffff\1\6\12\uffff\1\1\1\uffff\1\2\1\5\1"+
            "\uffff\1\3\11\4",
            "",
            "",
            "\1\5\1\uffff\12\5\13\uffff\1\5\37\uffff\1\5",
            "\1\5\1\uffff\12\10\13\uffff\1\5\37\uffff\1\5",
            "",
            "",
            "",
            "\1\5\1\uffff\12\10\13\uffff\1\5\37\uffff\1\5"
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T8 | T9 | INT | FLOAT | WS );";
        }
    }
 

}