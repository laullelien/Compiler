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
PRINTLN: 'println';
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

// rules for int
fragment DIGIT: '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9';
fragment POSITIVE_DIGIT: '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9';
INT: '0' | POSITIVE_DIGIT DIGIT*;



