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

class AddTest {

  @Test
  void addTest() {
    // we first want to make sure the stack doesn't overflow
    // this has been a recurring problem
    add(x(), e());
  }

  @Test
  void emptyListTest() {
    // we want to make sure this throws an exception
    assertThrows(RuntimeException.class, Add::add);
  }

  @Test
  void commutativityTest() {
    // x + sin(x)
    Expression ex = add(x(), sin(x()));
    // sin(x) + x
    Expression ex2 = add(sin(x()), x());
    assertEquals(ex, ex2);
  }

  @Test
  void nestingTest() {
    // x + (x + (x + (x + ln(x))))
    Expression ex = add(x(), add(x(), add(x(), add(x(), ln(x())))));
    assertEquals(add(mult(x(), constant(4)), ln(x())), ex);
  }

  @Test
  void constantTest() {
    // 2 + 3 + 4
    Expression ex = add(constant(2), constant(3), constant(4));
    assertEquals(constant(9), ex);

    // 0 + ln(x)
    Expression ex2 = add(addID(), ln(x()));
    assertEquals(ln(x()), ex2);

    // 1 + -1
    Expression ex3 = add(multID(), negate(multID()));
    assertEquals(addID(), ex3);
  }

  @Test
  void isNegativeTest() {
    // -1 + -cos(x)
    Expression ex = add(constant(-1), negate(cos(x())));
    assertTrue(ex.isNegative());

    // -ln(x) + -log(x)
    Expression ex2 = add(negate(ln(x())), negate(log(constant(10), x())));
    assertTrue(ex2.isNegative());

    // -1 + -cos(x) + sin(x)
    Expression ex3 = add(negate(multID()), negate(cos(x())), sin(x()));
    assertFalse(ex3.isNegative());
  }

  @Test
  void countTest() {
    // ln(x) + ln(x) + ln(x)
    Expression ex = add(ln(x()), ln(x()), ln(x()));
    assertEquals(mult(ln(x()), constant(3)), ex);
  }

  @Test
  void divAddTest() {
    // this shouldn't cause the stack to overflow
    // 1/2 + 1/3 + 1/4
    Expression hey = add(
        div(multID(), constant(2)),
        div(multID(), constant(3)),
        div(multID(), constant(4)));
    System.out.println(hey);
  }

  @Test
  void evaluateTest() {
    // x + x + 2
    Expression ex = add(x(), x(), constant(2));
    Optional<Expression> eval = ex.evaluate(x(), constant(2));
    assertTrue(eval.isPresent());
    assertEquals(constant(6), eval.get());

    // x + a + 3, where a is a constant
    Expression ex2 = add(x(), constant("a"), constant(3));
    Optional<Expression> eval2 = ex2.evaluate(x(), constant(3));
    assertTrue(eval2.isPresent());
    assertEquals(add(constant("a"), constant(6)), eval2.get());

    // x + 1/x
    // can't divide by 0!
    Expression ex3 = add(x(), poly(x(), -1));
    assertEquals(Optional.empty(), ex3.evaluate(x(), addID()));
  }

  @Test
  void derivativeTest() {
    // x + x + 2
    Expression ex = add(x(), x(), constant(2));
    assertEquals(constant(2), ex.differentiate(x()).get());

    // a * x + 3, where a is a constant
    Expression ex2 = add(mult(x(), constant("a")), constant(3));
    assertEquals(constant("a"), ex2.differentiate(x()).get());

    // x + ln(x)
    Expression ex3 = add(x(), ln(x()));
    assertEquals(add(multID(), poly(x(), -1)), ex3.differentiate(x()).get());

    // x + sin(x)
    Expression ex4 = add(x(), sin(x()));
    assertEquals(add(multID(), cos(x())), ex4.differentiate(x()).get());
  }

  @Test
  void toLatexTest() {
    // x + sin(x)
    Expression ex = add(x(), sin(x()));
    assertEquals("x + \\sin(x)", ex.toLaTex());
  }

//  @Test
//  void getVariablesTest() {
//
//  }
}
