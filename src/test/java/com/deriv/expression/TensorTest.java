package com.deriv.expression;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Power.poly;
import static com.deriv.expression.Variable.x;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TensorTest {
  @Test
  void ofTest() {
    // [[x], [x + 2 + x^2]]
    Expression ten = Tensor.of(
      Tensor.of(x()),
      Tensor.of(add(x(), constant(2), poly(x(), 2)))
    );
    System.out.println(ten);
    System.out.println(ten.evaluate(x(), constant(3)).get());
    System.out.println(ten.differentiate(x()).get());
  }

  @Test
  void getTest() {
    // [[x], [x + 2 + x^2]]
    Expression ten = Tensor.of(
      Tensor.of(x()),
      Tensor.of(add(x(), constant(2), poly(x(), 2)))
    );
    assertEquals(Tensor.of(x()), ten.asTensor().get(0));
    assertEquals(x(), ten.asTensor().getTensor(0).get(0));
    assertTrue(ten.asTensor().getTensor(0).isTensor());
  }

  @Test
  void depthTest() {
    // [[x], [x + 2 + x^2]]
    Expression ten = Tensor.of(
      Tensor.of(x()),
      Tensor.of(add(x(), constant(2), poly(x(), 2)))
    );
    assertEquals(2, ten.getDepth());

    // [1]
    Expression ten2 = Tensor.of(multID());
    assertEquals(1, ten2.getDepth());
  }

  @Test
  void equalsTest() {
    // [1]
    Expression ten = Tensor.of(multID());
    assertEquals(Tensor.of(multID()), ten);
    assertEquals(ten, ten);
    assertNotEquals(multID(), ten);
    assertEquals(ten.asTensor().getLines().hashCode(), ten.hashCode());
  }

  @Test
  void toStringTest() {
    // [1]
    Expression ten = Tensor.of(Tensor.of(multID()));
    assertEquals("[[1]]", ten.toString());
  }

  @Test
  void toLatexTest() {
    // [1]
    Expression ten = Tensor.of(Tensor.of(multID()));
    assertEquals(ten.toString(), ten.toLaTex());
  }
}
