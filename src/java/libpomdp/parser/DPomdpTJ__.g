lexer grammar DPomdpTJ;
@header {
    package libpomdp.parser;
}

DISCOUNTTOK : 'discount' ;
VALUESTOK : 'values' ;
STATESTOK : 'states' ;
ACTIONSTOK : 'actions' ;
OBSERVATIONSTOK : 'observations' ;
TTOK : 'T' ;
OTOK : 'O' ;
RTOK : 'R' ;
UNIFORMTOK : 'uniform' ;
IDENTITYTOK : 'identity' ;
REWARDTOK : 'reward' ;
COSTTOK : 'cost' ;
STARTTOK : 'start' ;
INCLUDETOK : 'include' ;
EXCLUDETOK : 'exclude' ;
RESETTOK : 'reset' ;
COLONTOK : ':' ;
ASTERICKTOK : '*' ;
PLUSTOK : '+' ;
MINUSTOK : '-' ;

// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g" 93
STRING  
    :   ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*
    ;

// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g" 97
INT     
    :   '0' | ('1'..'9') ('0'..'9')*
    ;

// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g" 101
FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g" 107
COMMENT
    :   '#' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;} // can also use skip()?
    ;

// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g" 113
WS  
    :   ( ' '
    |   '\t'
    |   '\r'
    |   '\n'
        ) {$channel=HIDDEN;}
    ;


// $ANTLR src "/home/maraya/inria/code/libpomdp/src/libpomdp/parser/java/DotPomdp.g" 122
fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
