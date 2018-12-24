package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Add.add;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Mult.mult;
import static horeilly1101.Expression.Power.*;
import static horeilly1101.Expression.Variable.*;
import static horeilly1101.Expression.Log.*;
import static org.junit.Assert.assertEquals;


public class PowerTest {
  @Test
  public void powerTest() {
    // make sure everything runs properly
    power(x(), x());
  }

  @Test
  public void simplifyTest() {
    // 2.0 ^ 3.0
    Expression ex = power(constant(2.0), constant(3.0));
    assertEquals(constant(8.0), ex);

    // 3.0 ^ 1.0
    Expression ex2 = power(constant(3.0), multID());
    assertEquals(constant(3.0), ex2);

    // ln(x) ^ 0.0
    Expression ex3 = poly(ln(x()), addID());
    assertEquals(multID(), ex3);

    // ln(x) ^ 1.0
    Expression ex4 = poly(ln(x()), multID());
    assertEquals(ln(x()), ex4);
  }

  @Test
  public void evaluateTest() {
    // 5 ^ x
    Expression ex = exponential(constant(5.0), x());
    assertEquals(constant(125.0), ex.evaluate("x", 3.0));

    // x ^ 4
    Expression ex2 = poly(x(), constant(4.0));
    assertEquals(constant(16.0), ex2.evaluate("x", 2.0));

    // x ^ x
    Expression ex3 = power(x(), x());
    assertEquals(constant(27.0), ex3.evaluate("x", 3.0));
  }

  @Test
  public void differentiateTest() {
    // 5 ^ x
    Expression ex = exponential(constant(5.0), x());
    assertEquals(mult(power(constant(5.0), x()), ln(constant(5.0))), ex.differentiate("x"));

    // x ^ 4
    Expression ex2 = poly(x(), constant(4.0));
    assertEquals(mult(constant(4.0), poly(x(), constant(3.0))), ex2.differentiate("x"));

    // x ^ x
    Expression ex3 = power(x(), x());
    assertEquals(mult(add(multID(), ln(x())), power(x(), x())), ex3.differentiate("x"));
  }
}
