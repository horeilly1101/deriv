package com.deriv.calculator;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Constant.multID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
  @Test
  void differentiateTest() {
    Calculator calc = new Calculator();
    assertEquals(multID(), calc.differentiate("x", "x").get());
  }

  @Test
  void evaluateTest() {
    Calculator calc = new Calculator();
    assertEquals(constant(-2), calc.evaluate("x + 2x - 5x", "x", "1").get());
  }

  @Test
  void readmeTest() {
    // Instantiate a Calculator object
    Calculator calc = new Calculator();

    // Example 1
    assertEquals(
      "(2 * x)",
      calc.differentiate("x^2", "x").get().toString()
    );

    // Example 2
    assertEquals(
      "8",
      calc.evaluate("3x + 2", "x", "2").get().toString()
    );

    // Example 3
    assertEquals(
      "(sin(x) / x + (cos(x) * ln(x)))",
      calc.differentiate("sin(x)ln(x)", "x").get().toString()
    );

    // Example 4
    assertEquals(
      "sin(x ^ 2)",
      calc.evaluate("sin(x)", "x", "x^2").get().toString()
    );
  }
}
