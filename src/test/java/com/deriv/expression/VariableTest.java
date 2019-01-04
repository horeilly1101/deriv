package com.deriv.expression;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.deriv.expression.Mult.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Variable.*;
import static org.junit.jupiter.api.Assertions.*;

class VariableTest {

  @Test
  void eTest() {
    // should throw an exception
    assertThrows(RuntimeException.class, () -> var("e"));
  }

  @Test
  void evaluateTest() {
    // x
    Optional<Expression> ex = x().evaluate("x", 5.0);
    assertTrue(ex.isPresent());
    assertEquals(constant(5), ex.get());

    // y
    Optional<Expression> ex2 = var("y").evaluate("x", 4.0);
    assertTrue(ex2.isPresent());
    assertEquals(var("y"), ex2.get());
  }

  @Test
  void derivativeTest() {
    // x
    Expression xVar = x();
    assertEquals(multID(), xVar.differentiate("x"));
    assertEquals(addID(), xVar.differentiate("y"));

    // y
    Expression yVar = var("y");
    assertEquals(multID(), yVar.differentiate("y"));
    assertEquals(addID(), yVar.differentiate("x"));
  }

  @Test
  void multiDerivativeTest() {
    // x * y
    Expression ex = mult(x(), var("y"));
    assertEquals(x(), ex.differentiate("y"));
    assertEquals(var("y"), ex.differentiate("x"));
  }
}
