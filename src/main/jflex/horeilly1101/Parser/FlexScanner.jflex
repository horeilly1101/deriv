/* The code for our Expression tokenizer. */

package horeilly1101.Parser;

import static horeilly1101.Parser.Scanner.SymbolType.*;

%%

/* Options and declarations */

%class FlexScanner
%unicode
%line
%column
%public
%type Token

Whitespace  = [ \t\n]+
Number      = [1-9][0-9]*(.[0-9]+)?
Variable    = [a-zA-Z]
Trig        = (sin|cos|tan|csc|sec|cot)
Log         = (log|ln)

%%

/* Lexical Rules */

{ Trig }        { return new Token(TRIG, yytext()); }

{ Number }      { return new Token(NUMBER, yytext()); }

{ Variable }    { return new Token(VARIABLE, yytext()); }

{ Log }         { return new Token(LOG, yytext()); }

"sqrt"          { return new Token(SQRT, ""); }

{ Whitespace }  { return new Token(WHITESPACE, ""); }

"("             { return new Token(LPAREN, ""); }

")"             { return new Token(RPAREN, ""); }

"+"             { return new Token(PLUS, ""); }

"-"             { return new Token(MINUS, ""); }

"*"             { return new Token(TIMES, ""); }

"/"             { return new Token(DIVIDE, ""); }

"^"             { return new Token(CARROT, ""); }

.               { return new Token(FAIL, ""); }