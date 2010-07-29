/** ------------------------------------------------------------------------- *
 * libpomdp
 * ========
 * File: dotalpha.g
 * Description: 
 * Copyright (c) 2009, 2010 Diego Maniloff 
 * W3: http://www.cs.uic.edu/~dmanilof
 --------------------------------------------------------------------------- */

grammar dotalpha;

/*------------------------------------------------------------------
 * LEXER INITIALIZATIONS
 *------------------------------------------------------------------*/
@lexer::header {
    package libpomdp.general.java;
}

/*------------------------------------------------------------------
 * PARSER INITIALIZATIONS
 *------------------------------------------------------------------*/
@header {
    package libpomdp.general.java;
}

@members {
    // main method
    public static void main(String[] args) throws Exception {
        dotalphaLexer lex = new dotalphaLexer(new ANTLRFileStream(args[0]));
       	CommonTokenStream tokens = new CommonTokenStream(lex);
        dotalphaParser parser = new dotalphaParser(tokens);

        try {
            parser.dotalpha();
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
}

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

INT     
    :   '0' | ('1'..'9') ('0'..'9')*
    ;

FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
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
dotalpha
    :           
        (
            action_decl 
            {
                // each action seen is added to the list
                actions.add($action_decl.a);
            }
            alpha_vector
            {
                // each full alpha vector seen is converted to an array and
                // added to the matrix
                alphas.add($alpha_vector.v.toArray(new Double[1]));
            }
            )+
    ;

action_decl returns [int a]
    :   INT
        {$a = Integer.parseInt($INT.text);}
    ;

alpha_vector returns [ArrayList<Double> v = new ArrayList<Double>()]
    :   (optional_sign FLOAT
        {
            $v.add($optional_sign.s * Double.parseDouble($FLOAT.text));
        }
        )+
        
    ;

optional_sign returns [int s]
    : '+'
        {$s = 1;}
    | '-'
        {$s = -1;}
    |  /* empty */
        {$s = 1;}
    ;
