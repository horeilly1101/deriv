/* The code for our Expression tokenizer. */

package horeilly1101.Parser;

%%

/* Options and declarations */

Whitespace = [ \t\n]+
Number = [1-9][0-9]*(.[0-9]+)?
Letter = [a-zA-Z]
Trig = (sin|cos|tan|csc|sec|cot)

%%

/* Lexical Rules */

