package com.deriv.expression;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Variable.x;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantTest {

  @Test
  void derivativeTest() {
    // constants are tested pretty hard by the other unit tests,
    // so I'm just going to check their derivatives for the sake
    // of completeness

    Expression c = constant("c");
    assertEquals(addID(), c.differentiate(x()).get());

    Expression one = multID();
    assertEquals(addID(), one.differentiate(x()).get());

    Expression e = e();
    assertEquals(addID(), e.differentiate(x()).get());

    Expression pi = pi();
    assertEquals(addID(), pi.differentiate(x()).get());
  }

  @Test
  void toLatexTest() {
    //5
    Expression con = constant(5);
    assertEquals(con.toString(), con.toLaTex());
  }
}
