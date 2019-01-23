package com.deriv.calculator;

import org.junit.jupiter.api.Test;

public class CalculatorTest {
  @Test
  void differentiateTest() {
    Calculator calc = new Calculator("x + 2x - 5x");
    System.out.println(calc.evaluate("x", "9").toOExpression());
  }
}
