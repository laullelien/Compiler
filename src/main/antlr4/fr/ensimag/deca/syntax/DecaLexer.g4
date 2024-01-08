lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules for strings & println.
fragment STRING_CAR: ~('"' | '\\' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')*? '"';
COMMENT: '//' .*? '\n' {skip();};
SEMI: ';';
PRINTX: 'printx';
PRINTLNX: 'printlnx';
PRINTLN: 'println';
PRINT: 'print';
OBRACE: '{';
CBRACE: '}';
OPARENT: '(';
CPARENT: ')';
COMMA: ',';
TO_SKIP:
        ('\n'
        | '\t'
        | ' '
        ){skip();};

// grammar for int
fragment DIGIT: '0' .. '9';
fragment POSITIVE_DIGIT: '1' .. '9';
INT: '0' | POSITIVE_DIGIT DIGIT*;

// grammar for float
fragment NUM: DIGIT+;
fragment SIGN: '+' | '-' ;
fragment SIGN_OR_EMPTY: SIGN | ;
fragment EXP: ('E' | 'e') SIGN_OR_EMPTY NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC: (DEC | DEC EXP) ('F' | 'f' | ) ;
fragment DIGITHEX: [0-9A-Fa-f]+;
fragment NUMHEX: DIGITHEX+;
fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN_OR_EMPTY? NUM ('F' | 'f' | ) ;
 FLOAT: FLOATDEC | FLOATHEX;

// grammar for bool

TRUE: 'true';
FALSE: 'false';

// grammar

PLUS : '+' ;
MINUS : '-' ;

// grammar for variables declaration

LETTER: 'a' .. 'z' + 'A' .. 'Z' ;
IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' + '_')* ;
