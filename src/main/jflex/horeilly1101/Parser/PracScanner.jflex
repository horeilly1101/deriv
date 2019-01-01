/* The code for our Expression tokenizer. */

package horeilly1101.Parser;

import java_cup.runtime.*;

%%

/* Options and declarations */

%class PracScanner
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
Number      = 0|[1-9][0-9]*

%%

/* Lexical Rules */

{ Number }      { return symbol(sym.NUMBER, new Integer(yytext())); }

{ Whitespace }  { /* ignore */ }

"("             { return symbol(sym.LPAREN); }

")"             { return symbol(sym.RPAREN); }

"+"             { return symbol(sym.PLUS); }

"-"             { return symbol(sym.MINUS); }

"*"             { return symbol(sym.TIMES); }

";"             { return symbol(sym.SEMI); }

.               { throw new RuntimeException("Illegal character <"+ yytext()+">"); }