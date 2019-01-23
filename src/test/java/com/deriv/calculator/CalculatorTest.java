package com.deriv.calculator;

import org.junit.jupiter.api.Test;

public class CalculatorTest {
  @Test
  void differentiateTest() {
    Calculator calc = new Calculator("1 / x");
    System.out.println(calc.differentiate("x").toOExpression().get());
  }

  @Test
  void evaluateTest() {
    Calculator calc = new Calculator("x + 2x - 5x");
    System.out.println(calc.evaluate("x", "1").toOExpression().get());
  }

  @Test
  void derivativeStepsTest() {
    Calculator calc = new Calculator("1 / x");
    System.out.println(calc.derivativeSteps("x").get());
  }
}
