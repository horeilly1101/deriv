package com.deriv.expression;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Constant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantTest {

  @Test
  void derivativeTest() {
    // constants are tested pretty hard by the other unit tests,
    // so I'm just going to check their derivatives for the sake
    // of completeness

    Expression c = constant("c");
    assertEquals(addID(), c.differentiate("x"));

    Expression one = multID();
    assertEquals(addID(), one.differentiate("x"));
  }
}
