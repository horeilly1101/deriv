package horeilly1101.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Scanner {

  public static List<Token> read(String input) {
    FlexScanner flex = new FlexScanner(new StringReader(input));
    List<Token> tokens = new ArrayList<>();

    for (;;) {
      Token current = new Token("Fail", "excpetion");
      try {
        current = flex.yylex();
      } catch (IOException e) {
        System.out.println(e);
      }

      if (current == null || current.name == "Fail") {
        break;
      }

      if (current.name != "Whitespace") {
        tokens.add(current);
      }
    }

    return tokens;
  }

  public static void main(String[] args) {
    System.out.println("hey");
    String str = "12 + 34 * sin(1)";

    System.out.println(read(str));
  }
}