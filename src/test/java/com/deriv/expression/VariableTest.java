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
  void piTest() {
    // should throw an exception
    assertThrows(RuntimeException.class, () -> var("π"));
  }

  @Test
  void constructorTest() {
    Expression x_2 = x(2);
    assertEquals("x_2", x_2.toString());

    Expression x_1_2_3 = x(1, 2, 3);
    assertEquals("x_1_2_3", x_1_2_3.toString());
  }

  @Test
  void evaluateTest() {
    // x
    Optional<Expression> ex = x().evaluate(x(), constant(5));
    assertTrue(ex.isPresent());
    assertEquals(constant(5), ex.get());

    // y
    Optional<Expression> ex2 = var("y").evaluate(x(), constant(4));
    assertTrue(ex2.isPresent());
    assertEquals(var("y"), ex2.get());
  }

  @Test
  void derivativeTest() {
    // x
    Expression xVar = x();
    assertEquals(multID(), xVar.differentiate(x()).get());
    assertEquals(addID(), xVar.differentiate(var("y").asVariable()).get());

    // y
    Expression yVar = var("y");
    assertEquals(multID(), yVar.differentiate(var("y").asVariable()).get());
    assertEquals(addID(), yVar.differentiate(x()).get());
  }

  @Test
  void multiDerivativeTest() {
    // x * y
    Expression ex = mult(x(), var("y"));
    assertEquals(x(), ex.differentiate(var("y").asVariable()).get());
    assertEquals(var("y"), ex.differentiate(x()).get());
  }

  @Test
  void toLatexTest() {
    // y
    Expression y = var("y");
    assertEquals(y.toString(), y.toLaTex());
  }
}
