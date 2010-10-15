lexer grammar DotAlpha;
@header {
    package libpomdp.parser;
}

T8 : '+' ;
T9 : '-' ;

// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g" 63
INT     
    :   '0' | ('1'..'9') ('0'..'9')*
    ;

// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g" 67
FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g" 73
WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotAlpha.g" 80
fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
