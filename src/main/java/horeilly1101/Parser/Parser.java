package horeilly1101.Parser;

import horeilly1101.Expression.Expression;
import java_cup.runtime.Symbol;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Parser {
  /**
   * Scans a String into a list of the names of the tokens. This method
   * only really exists for debugging.
   */
  static Optional<List<String>> read(String input) {
    FlexScanner flex = new FlexScanner(new StringReader(input));
    List<Symbol> tokens = new ArrayList<>();

    // iterate until loop breaks
    for (;;) {
      // default token is empty
      Symbol current;
      // we need to catch the exception
      // (but this should never happen)
      try {
        current = flex.next_token();
      } catch (IOException e) {
        System.out.println(e.toString());
        return Optional.empty();
      }

      // scanner returns an error when there's an error
      if (current.sym == sym.error) {
        return Optional.empty();
      }

      // the scanner returns 0 when finished
      if (current.sym == 0) {
        break;
      }

      tokens.add(current);
    }

    return Optional.of(tokens.stream()
                           .map(x -> sym.terminalNames[x.sym])
                           .collect(Collectors.toList()));
  }

  /**
   * Parses a String into an Optional of Expression.
   */
  public static Optional<Expression> parse(String input) {
    // initialize the parser
    CupParser parser = new CupParser(new FlexScanner(new StringReader(input)));
    try {
      // attempt to parse
      Object result = parser.parse().value;

      // if result can be cast to Expression, cast it
      return result instanceof Expression
                 ? Optional.of((Expression) result)
                 : Optional.empty();

    } catch (Exception e) {
      // handle exceptions
      return Optional.empty();
    }
  }
}