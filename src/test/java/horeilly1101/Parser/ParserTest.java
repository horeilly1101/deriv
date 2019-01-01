package horeilly1101.Parser;

import org.junit.Test;

import java.io.StringReader;

public class ParserTest {
  @Test
  public void parseTest() {
    Parser parser = new Parser(new FlexScanner(new StringReader("(x ^ y) ^ z")));
    try {
      System.out.println(parser.parse().value);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
