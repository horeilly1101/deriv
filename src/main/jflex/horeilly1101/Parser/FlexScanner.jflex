/* The code for our Expression tokenizer. */

package horeilly1101.Parser;

import horeilly1101.Parser.Scanner.*;

%%

/* Options and declarations */

%class FlexScanner
%unicode
%line
%column
%public
%type Token

Whitespace = [ \t\n]+
Number = [1-9][0-9]*(.[0-9]+)?
Variable = [a-zA-Z]
Trig = (sin|cos|tan|csc|sec|cot)

%%

/* Lexical Rules */

{ Trig }  { return new Token(TRIG, yytext()); }

{ Number } { return new Token(NUMBER, yytext()); }

{ Variable } { return new Token(VARIABLE, yytext()); }

{ Whitespace } { return new Token(WHITESPACE, yytext()); }

"(" { return new Token(LPAREN, yytext()); }

")" { return new Token(RPAREN, yytext()); }

"+" { return new Token(PLUS, yytext()); }

"-" { return new Token(MINUS, yytext()); }

"*" { return new Token(TIMES, yytext()); }

"/" { return new Token(DIVIDE, yytext()); }

. { return new Token(FAIL, yytext()); }