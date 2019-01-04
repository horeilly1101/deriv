package com.deriv.parser;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.deriv.parser.Parser.read;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScannerTest {
  /**
   * This method tests (a) that the input can be parsed to an Expression
   * and (b) that the parsed expression is equal to the expected expression.
   */
  private static void testHelper(List<Integer> expected, String input) {
    Optional<List<Integer>> parsed = read(input);
    assertTrue(parsed.isPresent());
    assertEquals(expected, parsed.get());
  }

  /**
   * This method allows to quickly, and functionally, construct a list
   * of Integers.
   */
  private static List<Integer> listof(Integer... input) {
    return Stream.of(input).collect(Collectors.toList());
  }

  @Test
  void whiteSpaceTest() {
    String str = "   ";
    List<Integer> ex = listof();
    testHelper(ex, str);
  }

  @Test
  void addTest() {
    String str = "5 + x";
    List<Integer> ex = listof(sym.NUMBER, sym.PLUS, sym.VARIABLE);
    testHelper(ex, str);

    String str2 = "5 + + x";
    List<Integer> ex2 = listof(sym.NUMBER, sym.PLUS, sym.PLUS, sym.VARIABLE);
    testHelper(ex2, str2);
  }

  @Test
  void minusTest() {
    String str = "5 - x";
    List<Integer> ex = listof(sym.NUMBER, sym.MINUS, sym.VARIABLE);
    testHelper(ex, str);

    String str2 = "5 - - x";
    List<Integer> ex2 = listof(sym.NUMBER, sym.MINUS, sym.MINUS, sym.VARIABLE);
    testHelper(ex2, str2);

    String str3 = "5 -- x";
    testHelper(ex2, str3);
  }

  @Test
  void multTest() {
    String str = "5 * x";
    List<Integer> ex = listof(sym.NUMBER, sym.TIMES, sym.VARIABLE);
    testHelper(ex, str);

    String str2 = "5 * * x";
    List<Integer> ex2 = listof(sym.NUMBER, sym.TIMES, sym.TIMES, sym.VARIABLE);
    testHelper(ex2, str2);
  }

  @Test
  void divideTest() {
    String str = "5 / x";
    List<Integer> ex = listof(sym.NUMBER, sym.DIVIDE, sym.VARIABLE);
    testHelper(ex, str);

    String str2 = "5 / / x";
    List<Integer> ex2 = listof(sym.NUMBER, sym.DIVIDE, sym.DIVIDE, sym.VARIABLE);
    testHelper(ex2, str2);
  }

  @Test
  void carrotTest() {
    String str = "5 ^ x";
    List<Integer> ex = listof(sym.NUMBER, sym.CARROT, sym.VARIABLE);
    testHelper(ex, str);

    String str2 = "5 ^ ^ x";
    List<Integer> ex2 = listof(sym.NUMBER, sym.CARROT, sym.DIVIDE, sym.VARIABLE);
    testHelper(ex2, str2);
  }

  @Test
  void sqrtTest() {
    String str = "sqrt(x)";
    List<Integer> ex = listof(sym.SQRT, sym.LPAREN, sym.VARIABLE, sym.RPAREN);
    testHelper(ex, str);
  }

  @Test
  void logTest() {
    String str = "log(5, x)";
    List<Integer> ex = listof(sym.LOG, sym.LPAREN, sym.NUMBER, sym.COMMA, sym.VARIABLE, sym.RPAREN);
    testHelper(ex, str);

    String str2 = "log(4)";
    List<Integer> ex2 = listof(sym.LOG, sym.LPAREN, sym.NUMBER, sym.RPAREN);
    testHelper(ex2, str2);

    String str3 = "ln(x)";
    List<Integer> ex3 = listof(sym.LN, sym.LPAREN, sym.VARIABLE, sym.RPAREN);
    testHelper(ex3, str3);
  }

  @Test
  void bracketTest() {
    String str = "[]";
    List<Integer> ex = listof(sym.LBRACK, sym.RBRACK);
    testHelper(ex, str);
  }

  @Test
  void trigTest() {
    String str = "sin(x)";
    List<Integer> ex = listof(sym.TRIG, sym.LPAREN, sym.VARIABLE, sym.RPAREN);
    testHelper(ex, str);

    String str2 = "cos(x)";
    List<Integer> ex2 = listof(sym.TRIG, sym.LPAREN, sym.VARIABLE, sym.RPAREN);
    testHelper(ex2, str2);

    String str3 = "tan(x)";
    List<Integer> ex3 = listof(sym.TRIG, sym.LPAREN, sym.VARIABLE, sym.RPAREN);
    testHelper(ex3, str3);

    String str4 = "csc(x)";
    List<Integer> ex4 = listof(sym.TRIG, sym.LPAREN, sym.VARIABLE, sym.RPAREN);
    testHelper(ex4, str4);

    String str5 = "sec(x)";
    List<Integer> ex5 = listof(sym.TRIG, sym.LPAREN, sym.VARIABLE, sym.RPAREN);
    testHelper(ex5, str5);

    String str6 = "cot(x)";
    List<Integer> ex6 = listof(sym.TRIG, sym.LPAREN, sym.VARIABLE, sym.RPAREN);
    testHelper(ex6, str6);
  }

  @Test
  void numberTest() {
    String str = "4.5";
    List<Integer> ex = listof(sym.NUMBER);
    testHelper(ex, str);

    String str2 = "53";
    testHelper(ex, str2);

    String str3 = "34.9";
    testHelper(ex, str3);

    String str4 = "0.432525";
    testHelper(ex, str4);

    String str5 = "0";
    testHelper(ex, str5);
  }

  @Test
  void breakTest() {

  }
}
