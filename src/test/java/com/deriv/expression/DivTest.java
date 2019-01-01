package com.deriv.expression;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;
import static com.deriv.expression.Add.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Variable.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DivTest {
  @Test
  public void divTest() {
    // we want to make sure the stack doesn't overflow
    div(ln(x()), x());
  }

  @Test
  public void nestTest() {
    // 1 / (1 / x)
    Expression dv = div(multID(), div(multID(), x()));
    assertEquals(x(), dv);

    // 1 / (1 / (1 / x))
    Expression dv2 = div(multID(), div(multID(), div(multID(), x())));
    assertEquals(div(multID(), x()), dv2);
  }

  @Test
  public void simplifyConstantTest() {
    // 2 / 4
    Expression con = div(constant(2), constant(4));
    assertEquals(div(multID(), constant(2)), con);

    // 81 / 3
    Expression con2 = div(constant(81), constant(3));
    assertEquals(constant(27), con2);
  }

  @Test
  public void addTest() {
    Expression hey = add(constant(9), div(multID(), constant(4)), constant(3), constant(6));
    assertEquals(add(div(multID(), constant(4)), constant(18)), hey);
  }

  @Test
  public void evaluateTest() {
    // x / 3
    Expression ex = div(x(), constant(3));
    assertEquals(constant(27), ex.evaluate("x", 81.0));

    // (x + 1) / 3
    Expression ex2 = div(add(x(), multID()), constant(3));
    assertEquals(constant(27), ex2.evaluate("x", 80.0));

    // ln(x + 1) / x
    Expression ex3 = div(ln(add(x(), multID())), x());
    assertEquals(ln(constant(2)), ex3.evaluate("x", 1.0));
  }

  @Test
  public void differentiateTest() {
    // this be fixed by adding divs
//    // sin(x) / x
//    Expression ex = div(sin(x()), x());
//    Expression result = div(add(mult(cos(x()), x()), negate(sin(x()))), poly(x(), 2));
//    assertEquals(result, ex.differentiate("x"));
  }

  @Test
  public void denomTest() {
    System.out.println(add(div(multID(), x()), div(multID(), x())));
  }
}