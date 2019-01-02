package com.deriv.expression;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Mult.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Variable.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VariableTest {

//  @Test(expected = RuntimeException.class)
//  public void eTest() {
//    // should throw an exception
//    var("e");
//  }

  @Test
  public void derivativeTest() {
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
  public void multiDerivativeTest() {
    // x * y
    Expression ex = mult(x(), var("y"));
    assertEquals(x(), ex.differentiate("y"));
    assertEquals(var("y"), ex.differentiate("x"));
  }
}
