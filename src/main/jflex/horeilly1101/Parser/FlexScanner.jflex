/* The code for our Expression tokenizer. */

package horeilly1101.Parser;

import horeilly1101.Parser.Token.*;

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

{ Trig }  { return new Token("Trig", yytext()); }

{ Number } { return new Token("Number", yytext()); }

{ Variable } { return new Token("Variable", yytext()); }

{ Whitespace } { return new Token("Whitespace", yytext()); }

"(" { return new Token("LParen", yytext()); }

")" { return new Token("RParen", yytext()); }

"+" { return new Token("Plus", yytext()); }

"-" { return new Token("Minus", yytext()); }

"*" { return new Token("Times", yytext()); }

"/" { return new Token("Divide", yytext()); }

. { return new Token("Fail", yytext()); }