package com.deriv.parser;

import com.deriv.expression.Expression;
import com.deriv.expression.Variable;
import java_cup.runtime.Symbol;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class Parser {
  private String inputString;

  public Parser(String inputString) {
    this.inputString = inputString;
  }

  /**
   * Scans a String into a list of the symbols of the tokens. This method
   * only really exists for debugging and unit testing.
   */
  Optional<List<Integer>> read() {
    FlexScanner flex = new FlexScanner(new StringReader(inputString));
    List<Symbol> tokens = new ArrayList<>();

    // iterate until loop breaks
    for (;;) {
      // default token is empty
      Symbol current;

      // we need to catch the exception (but this should never happen)
      try {
        current = flex.next_token();
      } catch (IOException e) {
        return Optional.empty();
      }

      // scanner returns an error when there's an error
      if (current.sym == sym.error)
        return Optional.empty();

      // the scanner returns EOF when finished
      if (current.sym == sym.EOF)
        break;

      tokens.add(current);
    }

    return Optional.of(tokens.stream().map(x -> x.sym).collect(toList()));
  }

  /**
   * Parses a String into an Optional of Expression.
   * Note: The version of CupParser I use is deprecated. However, after looking through the
   * code, I can find no problem with it. It does exactly what I need it to do.
   */
  @SuppressWarnings("deprecation") // not a problem
  public Optional<Expression> parse() {
    // initialize the parser
    CupParser parser = new CupParser(new FlexScanner(new StringReader(inputString)));

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

  /**
   * Parses a String into an Optional of Variable.
   */
  public Optional<Variable> parseVariable() {
    return parse().flatMap(x -> x.isVariable() ? Optional.of(x.asVariable()) : Optional.empty());
  }
}