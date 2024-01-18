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

// grammar for while instruction

WHILE: 'while';

// grammer for if branch

IF: 'if';
ELSE: 'else';

// grammar for print instructions

PRINTX: 'printx';
PRINTLNX: 'printlnx';
PRINTLN: 'println';
PRINT: 'print';

// grammar for int

fragment DIGIT: '0' .. '9';
fragment POSITIVE_DIGIT: '1' .. '9';
INT: '0' | POSITIVE_DIGIT DIGIT*;

// grammar for bool

TRUE: 'true';
FALSE: 'false';

// grammar for null

NULL: 'null';

// grammar for return

RETURN: 'return';

// grammar for readInt

READINT : 'readInt';
READFLOAT : 'readFloat';

// grammar for classes

CLASS: 'class';
EXTENDS: 'extends';
NEW: 'new';
PROTECTED: 'protected';

// grammar for asm instruction

ASM: 'asm';

// grammar for identifiers

fragment LETTER: 'a' .. 'z' | 'A' .. 'Z' ;

// grammar for include

fragment FILENAME: (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE: ('#include' (' ')* '"' FILENAME '"'){doInclude(getText());};

// grammar for identifiers

IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' + '_')* ;

// Deca lexer rules for strings & println.
fragment EOL: '\\n' ;
fragment STRING_CAR: ~('"' | '\\' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')*? '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';
COMMENT: '//' .*? '\n' {skip();};
STARCOMMENT : '/*' .*? '*/' {skip();};
SEMI: ';';
OBRACE: '{';
CBRACE: '}';
OPARENT: '(';
CPARENT: ')';
EQUALS: '=';
COMMA: ',';
TO_SKIP:
        ('\n'
        | '\t'
        | ' '
        ){skip();};

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

// grammar for comparaisons

EQEQ: '==';
NEQ: '!=';
GEQ: '>=';
LEQ: '<=';
GT: '>';
LT: '<';

// grammar for binary boolean operators

OR: '||';
AND: '&&';

// grammar for unary boolean operator

EXCLAM: '!';

// Grammaire des opérations

PLUS : '+' ;
MINUS : '-' ;
TIMES : '*' ;
SLASH : '/' ;
PERCENT : '%' ;
