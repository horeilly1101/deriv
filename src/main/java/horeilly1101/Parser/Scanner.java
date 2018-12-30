package horeilly1101.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scanner {
  /**
   * Scans a String into a list of Token objects. We want to filter out
   * the WHITESPACE tokens and break the scanner if a FAIL token pops up.
   */
  static Optional<List<Token>> read(String input) {
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

      if (current == null) {
        break;
      }

      if (current.name.equals(SymbolType.FAIL)) {
        return Optional.empty();
      }

      // filter out whitespaces
      if (!current.name.equals(SymbolType.WHITESPACE)) {
        tokens.add(current);
      }
    }

    return Optional.of(tokens);
  }

  enum SymbolType {
    TRIG, NUMBER, VARIABLE, LOG, SQRT, WHITESPACE, LPAREN, RPAREN,
    PLUS, MINUS, TIMES, DIVIDE, CARROT,  FAIL
  }
}