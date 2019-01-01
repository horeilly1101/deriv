package horeilly1101.PracParser;

import org.junit.Test;

import java.io.StringReader;

public class PracParserTest {
  @Test
  public void parseTest() {
    Parser parser = new Parser(new PracScanner(new StringReader("4 + 9 - 3 + -1 +7;")));
    try {
      System.out.println(parser.parse().value);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
