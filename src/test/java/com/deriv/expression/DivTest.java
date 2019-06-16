package com.deriv.expression;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;
import static com.deriv.expression.Add.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Variable.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is an extension of Div test that specifically deals with
 * testing the div method.
 */
class DivTest {
  @Test
  void divTest() {
    // we want to make sure the stack doesn't overflow
    div(ln(x()), x());
  }

  @Test
  void nestTest() {
    // 1 / (1 / x)
    Expression dv = div(multID(), div(multID(), x()));
    assertEquals(x(), dv);

    // 1 / (1 / (1 / x))
    Expression dv2 = div(multID(), div(multID(), div(multID(), x())));
    assertEquals(div(multID(), x()), dv2);
  }

  @Test
  void simplifyConstantTest() {
    // 2 / 4
    Expression con = div(constant(2), constant(4));
    assertEquals(div(multID(), constant(2)), con);

    // 81 / 3
    Expression con2 = div(constant(81), constant(3));
    assertEquals(constant(27), con2);
  }

  @Test
  void addTest() {
    Expression hey = add(constant(9), div(multID(), constant(4)), constant(3), constant(6));
    assertEquals(add(div(multID(), constant(4)), constant(18)), hey);
  }

  @Test
  void evaluateTest() {
    // x / 3
    Expression ex = div(x(), constant(3));
    assertEquals(constant(27), ex.evaluate(x(), constant(81)).get());

    // (x + 1) / 3
    Expression ex2 = div(add(x(), multID()), constant(3));
    assertEquals(constant(27), ex2.evaluate(x(), constant(80)).get());

    // ln(x + 1) / x
    Expression ex3 = div(ln(add(x(), multID())), x());
    assertEquals(ln(constant(2)), ex3.evaluate(x(), multID()).get());
  }

  @Test
  void differentiateTest() {
    // this be fixed by adding divs
//    // sin(x) / x
//    Expression ex = div(sin(x()), x());
//    Expression result = div(add(mult(cos(x()), x()), negate(sin(x()))), poly(x(), 2));
//    assertEquals(result, ex.differentiate("x"));
  }

//  @Test
//  public void denomTest() {
//    System.out.println(div(multID(), constant(4)));
//  }
}
