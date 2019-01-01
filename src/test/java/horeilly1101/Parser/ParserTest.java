package horeilly1101.Parser;

import org.junit.Test;

import static horeilly1101.Parser.Parser.*;
import static horeilly1101.Parser.Scanner.*;

public class ParserTest {
  @Test
  public void parseTest() {
    String str = "sin(x) + 12";
    System.out.println(parse(str));

    String str1 = "1 + 12";
    System.out.println(parse(str1));

    String str2 = "(x + 5)sin(x)";
    System.out.println(parse(str2));
  }
}