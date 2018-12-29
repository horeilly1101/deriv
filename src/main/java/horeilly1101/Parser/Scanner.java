package horeilly1101.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Scanner {

  static List<Token> read(String input) {
    FlexScanner flex = new FlexScanner(new StringReader(input));
    List<Token> tokens = new ArrayList<>();

    // iterate until loop breaks
    for (;;) {
      // default token is a fail
      Token current = new Token(SymbolType.FAIL, "exception");
      // we need to catch the exception
      // (that's never actually going to be thrown)
      try {
        current = flex.yylex();
      } catch (IOException e) {
        System.out.println(e);
      }

      if (current == null || current.name.equals(SymbolType.FAIL)) {
        break;
      }

      // filter out whitespaces
      if (!current.name.equals(SymbolType.WHITESPACE)) {
        tokens.add(current);
      }
    }

    return tokens;
  }

  enum SymbolType {
    TRIG, NUMBER, VARIABLE, WHITESPACE, LPAREN, RPAREN,
    PLUS, MINUS, TIMES, DIVIDE, FAIL
  }
}