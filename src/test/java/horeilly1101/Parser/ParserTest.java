package horeilly1101.Parser;

import org.junit.Test;

import static horeilly1101.Parser.Parser.*;

public class ParserTest {
  @Test
  public void parseTest() {
    String str = "sin(x) + 12";
    System.out.println(parse(str));

    String str1 = "1 + 12";
    System.out.println(parse(str1));

    String str2 = "(x + 5)sin(x)";
    System.out.println(parse(str2));

    String str3 = "x ^ 5 + 7x^2 - x + 9";
    System.out.println(parse(str3));

    String str4 = "xx";
    System.out.println(parse(str4));

    String str5 = "xlog(5, x)";
    System.out.println(parse(str5));

    String str6 = "xlog(e, x)";
    System.out.println(parse(str6));

    String str7 = "xlog(e, &)";
    System.out.println(parse(str7));
  }
}