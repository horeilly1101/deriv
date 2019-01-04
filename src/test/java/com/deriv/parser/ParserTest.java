package com.deriv.parser;

import com.deriv.expression.Expression;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static com.deriv.expression.Add.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Log.ln;
import static com.deriv.expression.Mult.*;
import static com.deriv.expression.Variable.*;
import static com.deriv.expression.Trig.*;
import static com.deriv.parser.Parser.parse;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
  /**
   * This method tests (a) that the input can be parsed to an Expression
   * and (b) that the parsed expression is equal to the expected expression.
   */
  private static void testHelper(Expression expected, String input) {
    Optional<Expression> parsed = parse(input);
    assertTrue(parsed.isPresent());
    assertEquals(expected, parsed.get());
  }

  @Test
  void addTest() {
    String str = "sin(x) + 12";
    Expression ex = add(sin(x()), constant(12));
    testHelper(ex, str);

    String str2 = "1 + 12";
    Expression ex2 = constant(13);
    testHelper(ex2, str2);

    String str3 = "1 + 1 + 1 + 1 + 1";
    Expression ex3 = constant(5);
    testHelper(ex3, str3);

    String str4 = "5 + 0";
    Expression ex4 = constant(5);
    testHelper(ex4, str4);
  }

  @Test
  void subtractTest() {
    String str = "sin(x) - 12";
    Expression ex = add(sin(x()), constant(-12));
    testHelper(ex, str);

    String str2 = "1 - 12";
    Expression ex2 = constant(-11);
    testHelper(ex2, str2);

    String str3 = "1 - 1 - 1 - 1 - 1";
    Expression ex3 = constant(-3);
    testHelper(ex3, str3);

    String str4 = "5 - 0";
    Expression ex4 = constant(5);
    testHelper(ex4, str4);

    String str5 = "5 - (ln(x) + sin(x))";
    Expression ex5 = add(constant(5), negate(add(ln(x()), sin(x()))));
    testHelper(ex5, str5);
  }

  @Test
  void minusMinusTest() {
    String str = "sin(x) - -12";
    Expression ex = add(sin(x()), constant(12));
    testHelper(ex, str);

    String str2 = "1 - -12";
    Expression ex2 = constant(13);
    testHelper(ex2, str2);

    String str3 = "1 - -1 - -1 - -1 - -1";
    Expression ex3 = constant(5);
    testHelper(ex3, str3);

    String str4 = "5 - -0";
    Expression ex4 = constant(5);
    testHelper(ex4, str4);
  }

  @Test
  void plusMinusTest() {
    String str = "sin(x) + -12";
    Expression ex = add(sin(x()), constant(-12));
    testHelper(ex, str);

    String str2 = "1 + -12";
    Expression ex2 = constant(-11);
    testHelper(ex2, str2);

    String str3 = "1 + -1 + -1 + -1 + -1";
    Expression ex3 = constant(-3);
    testHelper(ex3, str3);

    String str4 = "5 + -0";
    Expression ex4 = constant(5);
    testHelper(ex4, str4);

    String str5 = "5 + -(ln(x) + sin(x))";
    Expression ex5 = add(constant(5), negate(add(ln(x()), sin(x()))));
    testHelper(ex5, str5);
  }

  @Test
  void multTest() {
    String str = "sin(x) * 12";
    Expression ex = mult(sin(x()), constant(12));
    testHelper(ex, str);

    String str2 = "1 * 12";
    Expression ex2 = constant(12);
    testHelper(ex2, str2);

    String str3 = "2 * 2 * 2 * 2 * 2";
    Expression ex3 = constant(32);
    testHelper(ex3, str3);

    String str4 = "5 * 0";
    Expression ex4 = addID();
    testHelper(ex4, str4);
  }

  @Test
  void parseTest() {
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

//    String str7 = "xlog(e, &)";
//    System.out.println(parse(str7));
  }

  @Test
  void thereAndBackAgainTest() {

  }
}