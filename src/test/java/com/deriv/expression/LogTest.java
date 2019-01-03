package com.deriv.expression;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Variable.*;
import static com.deriv.expression.Power.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogTest {

  @Test
  public void logTest() {
    // check that the stack doesn't overflow
    log(x(), poly(x(), 2));
  }

  @Test
  public void simplifyTest() {
    // log(x, x ^ 2)
    Expression lg = log(x(), poly(x(), 2));
    assertEquals(constant(2), lg);

    // log(ln(x), ln(x) ^ e())
    Expression lg2 = log(ln(x()), power(ln(x()), e()));
    assertEquals(e(), lg2);
  }

  @Test
  public void evaluateTest() {
    // log(2, x)
    Expression lg = log(constant(2), x());
    assertEquals(multID(), lg.evaluate("x", 2.0).get());
    assertEquals(log(constant(2), constant(5)), lg.evaluate("x", 5.0).get());
  }

  @Test
  public void differentiateTest() {
    // ln(x)
    Expression ln = ln(x());
    assertEquals(div(multID(), x()), ln.differentiate("x"));

    // log(e, x)
    Expression ln2 = log(e(), x());
    assertEquals(ln.differentiate("x"), ln2.differentiate("x"));

    // log(2, x)
    Expression lg = log(constant(2), x());
    assertEquals(div(multID(), mult(ln(constant(2)), x())), lg.differentiate("x"));
  }
}
