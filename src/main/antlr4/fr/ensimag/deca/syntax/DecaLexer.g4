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

// Deca lexer rules.
DUMMY_TOKEN: .;
/*OBRACE: '{'; // A FAIRE : Règle bidon qui reconnait tous les caractères.
CBRACE: '}';
DEFAULT: . ;*/
