package com.deriv.parser;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ScannerTest {
  /**
   * This method tests (a) that the input can be parsed to an Expression
   * and (b) that the parsed expression is equal to the expected expression.
   */
  private static void testHelper(List<Integer> expected, String input) {
    Optional<List<Integer>> parsed = new Parser(input).read();
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

    String str2 = "  \t  \n  \t\t  ";
    testHelper(ex, str2);
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
    List<Integer> ex2 = listof(sym.NUMBER, sym.CARROT, sym.CARROT, sym.VARIABLE);
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
    String str = "53";
    List<Integer> ex = listof(sym.NUMBER);
    testHelper(ex, str);

    String str2 = "432525";
    testHelper(ex, str2);

    String str3 = "0";
    testHelper(ex, str3);
  }

  @Test
  void variableTest() {
    String str = "si";
    List<Integer> ex = listof(sym.VARIABLE, sym.VARIABLE);
    testHelper(ex, str);

    String str2 = "co";
    testHelper(ex, str2);

    String str3 = "ta";
    testHelper(ex, str3);

    String str4 = "cs";
    testHelper(ex, str4);

    String str5 = "s";
    List<Integer> ex5 = listof(sym.VARIABLE);
    testHelper(ex5, str5);

    String str6 = "l";
    List<Integer> ex6 = listof(sym.VARIABLE);
    testHelper(ex6, str6);

    String str7 = "lo";
    testHelper(ex, str7);

    String str8 = "sqr";
    List<Integer> ex8 = listof(sym.VARIABLE, sym.VARIABLE, sym.VARIABLE);
    testHelper(ex8, str8);
  }

  @Test
  void breakTest() {
    String str = " ^ + &";
    assertFalse(new Parser(str).read().isPresent());

    String str2 = "2.34";
    assertFalse(new Parser(str2).read().isPresent());

    // returns two numbers
//    String str3 = "034";
//    System.out.println(new Parser(str3).read().get());
  }
}
