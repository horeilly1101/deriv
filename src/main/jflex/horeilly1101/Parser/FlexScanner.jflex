/* The code for our Expression tokenizer. */

package horeilly1101.Parser;

import java_cup.runtime.*;

%%

/* Options and declarations */

%class FlexScanner
%unicode
%line
%column
%public
%cup

%{
    StringBuffer string = new StringBuffer();

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

Whitespace  = [ \t\n]+
Number      = [1-9][0-9]*(.[0-9]+)?
Variable    = [a-zA-Z]
Trig        = (sin|cos|tan|csc|sec|cot)
Log         = (log|ln)

%%

/* Lexical Rules */

{ Trig }        { return symbol(sym.TRIG); }

{ Number }      { return symbol(sym.NUMBER); }

{ Variable }    { return symbol(sym.VARIABLE); }

{ Log }         { return symbol(sym.LOG); }

"sqrt"          { return symbol(sym.SQRT); }

{ Whitespace }  { /* ignore */ }

"("             { return symbol(sym.LPAREN); }

")"             { return symbol(sym.RPAREN); }

"+"             { return symbol(sym.PLUS); }

"-"             { return symbol(sym.MINUS); }

"*"             { return symbol(sym.TIMES); }

"/"             { return symbol(sym.DIVIDE); }

"^"             { return symbol(sym.CARROT); }

.               { throw new RuntimeException("Illegal character <"+ yytext()+">"); }