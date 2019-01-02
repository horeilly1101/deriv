/* The code for our Expression tokenizer. */

package com.deriv.parser;

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
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

Whitespace  = [ \t\n]+
Number      = 0|[1-9][0-9]*
Variable    = [a-zA-Z]
Trig        = (sin|cos|tan|csc|sec|cot)

%%

/* Lexical Rules */

{ Trig }        { return symbol(sym.TRIG, yytext()); }

{ Number }      { return symbol(sym.NUMBER, new Integer(yytext())); }

"e"             { return symbol(sym.ECON); }

{ Variable }    { return symbol(sym.VARIABLE, yytext()); }

"log"           { return symbol(sym.LOG); }

"ln"            { return symbol(sym.LN); }

"sqrt"          { return symbol(sym.SQRT); }

{ Whitespace }  { /* ignore */ }

"("             { return symbol(sym.LPAREN); }

")"             { return symbol(sym.RPAREN); }

"["             { return symbol(sym.LBRACK); }

"]"             { return symbol(sym.RBRACK); }

"+"             { return symbol(sym.PLUS); }

"-"             { return symbol(sym.MINUS); }

"*"             { return symbol(sym.TIMES); }

"/"             { return symbol(sym.DIVIDE); }

"^"             { return symbol(sym.CARROT); }

","             { return symbol(sym.COMMA); }

.               { return symbol(sym.error); }