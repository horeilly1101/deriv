package com.deriv.expression;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;
import static com.deriv.expression.Add.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Trig.*;
import static com.deriv.expression.Variable.*;
import static com.deriv.expression.Power.*;
import static org.junit.jupiter.api.Assertions.*;

class MultTest {

  @Test
  void multTest() {
    // we first want to make sure the stack doesn't overflow
    // this has been a recurring problem
    mult(x(), e());
  }

  @Test
  void emptyListTest() {
    // we want to make sure this throws an exception
    assertThrows(RuntimeException.class, Mult::mult);
  }

  @Test
  void commutativityTest() {
    // x * sin(x)
    Expression ex = mult(x(), sin(x()));
    // sin(x) * x
    Expression ex2 = mult(sin(x()), x());
    assertEquals(ex, ex2);
  }

  @Test
  void nestingTest() {
    // x * (x * (x * (x * ln(x))))
    Expression ex = mult(x(), mult(x(), mult(x(), mult(x(), ln(x())))));
    assertEquals(mult(poly(x(), 4), ln(x())), ex);
  }

  @Test
  void constantTest() {
    // 2 * 3 * 4
    Expression ex = mult(constant(2), constant(3), constant(4));
    assertEquals(constant(24), ex);

    // 0 * ln(x)
    Expression ex2 = mult(addID(), ln(x()));
    assertEquals(addID(), ex2);
  }

  @Test
  void exponentTest() {
    // ln(x) * ln(x) * ln(x)
    Expression ex = mult(ln(x()), ln(x()), ln(x()));
    assertEquals(power(ln(x()), constant(3)), ex);
  }

  @Test
  void isNegativeTest() {
    // -x
    Expression ex = mult(constant(-1), x());
    assertTrue(ex.isNegative());

    // -2 / x ^ 2
    Expression ex2 = mult(constant(-2), poly(x(), -2));
    assertTrue(ex2.isNegative());

    // xln(x)
    Expression ex3 = mult(x(), ln(x()));
    assertFalse(ex3.isNegative());
  }

  @Test
  void negateTest() {
    // -x
    Expression ex = negate(x());
    assertTrue(ex.isNegative());
  }

  @Test
  void distributeTest() {
    // 2(x + ln(x))
    Expression ex = mult(constant(2), add(x(), ln(x())));
    assertEquals(add(mult(constant(2), x()), mult(constant(2), ln(x()))), ex);
  }

  @Test
  void evaluateTest() {
    // x * x * 2
    Expression ex = mult(x(), x(), constant(2));
    Optional<Expression> eval = ex.evaluate(x(), constant(2));
    assertTrue(eval.isPresent());
    assertEquals(constant(8), eval.get());

    // x * a * 3, where a is a constant
    Expression ex2 = mult(x(), constant("a"), constant(3));
    Optional<Expression> eval2 = ex2.evaluate(x(), constant(3));
    assertTrue(eval2.isPresent());
    assertEquals(mult(constant("a"), constant(9)), eval2.get());

    // 3 / x
    Expression ex3 = div(constant(3), x());
    Optional<Expression> eval3 = ex3.evaluate(x(), addID());
    assertFalse(eval3.isPresent());
  }

  @Test
  void derivativeTest() {
    // x * x * 2
    Expression ex = mult(x(), x(), constant(2));
    assertEquals(mult(constant(4), x()), ex.differentiate(x()).get());

    // x * a * 3, where a is a constant
    Expression ex2 = mult(x(), constant("a"), constant(3));
    assertEquals(mult(constant("a"), constant(3)), ex2.differentiate(x()).get());

    // x * ln(x)
    Expression ex3 = mult(x(), ln(x()));
    assertEquals(add(multID(), ln(x())), ex3.differentiate(x()).get());

    // x * sin(x)
    Expression ex4 = mult(x(), sin(x()));
    assertEquals(add(sin(x()), mult(x(), cos(x()))), ex4.differentiate(x()).get());
  }

  @Test
  void stringTest() {
    // x / ln(x)
    Expression ex = mult(x(), poly(ln(x()), -1));
    System.out.println(ex);
  }

  @Test
  void toLatexTest() {
    // x * sinx(x)
    Expression mul = mult(x(), sin(x()));
    assertEquals("\\sin(x)x", mul.toLaTex());
  }
}
