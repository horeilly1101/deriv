package com.deriv.expression;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Variable.*;
import static com.deriv.expression.Power.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogTest {

  @Test
  void logTest() {
    // check that the stack doesn't overflow
    log(x(), poly(x(), 2));
  }

  @Test
  void simplifyTest() {
    // log(x, x ^ 2)
    Expression lg = log(x(), poly(x(), 2));
    assertEquals(constant(2), lg);

    // log(ln(x), ln(x) ^ e())
    Expression lg2 = log(ln(x()), power(ln(x()), e()));
    assertEquals(e(), lg2);
  }

  @Test
  void evaluateTest() {
    // log(2, x)
    Expression lg = log(constant(2), x());

    // evaluate at 2
    Optional<Expression> eval = lg.evaluate(x(), constant(2));
    assertTrue(eval.isPresent());
    assertEquals(multID(), eval.get());

    // evaluate at 5
    Optional<Expression> eval2 = lg.evaluate(x(), constant(5));
    assertTrue(eval2.isPresent());
    assertEquals(log(constant(2), constant(5)), eval2.get());

    // evaluate at -1
    Optional<Expression> eval3 = lg.evaluate(x(), negate(multID()));
    assertFalse(eval3.isPresent());
  }

  @Test
  void differentiateTest() {
    // ln(x)
    Expression ln = ln(x());
    assertEquals(div(multID(), x()), ln.differentiate(x()).get());

    // log(e, x)
    Expression ln2 = log(e(), x());
    assertEquals(ln.differentiate(x()).get(), ln2.differentiate(x()).get());

    // log(2, x)
    Expression lg = log(constant(2), x());
    assertEquals(div(multID(), mult(ln(constant(2)), x())), lg.differentiate(x()).get());
  }

  @Test
  void toLatexTest() {
    // log(2, x)
    Expression lg = log(constant(2), x());
    assertEquals("\\log_{2} x", lg.toLaTex());

    // ln(x)
    Expression nat = ln(x());
    assertEquals("\\ln(x)", nat.toLaTex());
  }
}
