package horeilly1101.Parser;

import horeilly1101.Expression.Expression;

import java.io.StringReader;
import java.util.Optional;

public class Parser {
  public static Optional<Expression> parse(String input) {
    CupParser parser = new CupParser(new FlexScanner(new StringReader(input)));
    try {
      Object result = parser.parse().value;
      return result instanceof Expression
                 ? Optional.of((Expression) result)
                 : Optional.empty();
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
