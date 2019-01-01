package horeilly1101.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java_cup.runtime.*;

public class Scanner {
  /**
   * Scans a String into a list of Token objects. We want to filter out
   * the WHITESPACE tokens and break the scanner if a FAIL token pops up.
   */
  static Optional<List<String>> read(String input) {
    FlexScanner flex = new FlexScanner(new StringReader(input));
    List<Symbol> tokens = new ArrayList<>();

    // iterate until loop breaks
    for (;;) {
      // default token is empty
      Symbol current;
      // we need to catch the exceptions
      try {
        current = flex.next_token();
      } catch (Exception e) {
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
}