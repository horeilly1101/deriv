package com.deriv.expression;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Power.poly;
import static com.deriv.expression.Variable.x;

public class TensorTest {
  @Test
  void ofTest() {
    Expression ten1 = Tensor.of(Tensor.of(x()), Tensor.of(add(x(), constant(2), poly(x(), 2))));
    System.out.println(ten1);
    System.out.println(ten1.evaluate(x().asVariable(), constant(3)).get());
    System.out.println(ten1.differentiate(x().asVariable()).get());
  }
}
