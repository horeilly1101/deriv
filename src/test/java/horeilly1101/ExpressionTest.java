package horeilly1101;

import org.junit.Test;

import static horeilly1101.Expression.Add.*;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Power.*;
import static horeilly1101.Expression.Variable.*;
import static horeilly1101.Expression.Trig.*;
import static horeilly1101.Expression.Log.*;
import static org.junit.Assert.assertEquals;

public class ExpressionTest {

  @Test
  public void multTest() {
    Expression mul = mult(x(), x(), constant(2.0));
    System.out.println(mul);
//    Expression ad = add(constant(2.0), constant(1.0));
  }

  @Test
  public void polyDerivativeTest() {
    // x ^ 2.0
    Expression pol = poly(x(), Constant.constant(2.0));
    // 2.0 * x
    Expression polExpected = mult(constant(2.0), x());
    assertEquals(polExpected, pol.differentiate("x"));

    // x ^ 2.0 + x + 1.0
    Expression pol2 = add(poly(x(), Constant.constant(2.0)), x(), constant(1.0));
    // 2.0 * x + 1
    Expression pol2Expected = add(mult(constant(2.0), x()), constant(1.0));
    assertEquals(pol2Expected, pol2.differentiate("x"));

    System.out.println(pol2Expected.evaluate("y", 1.0));
  }

  @Test
  public void multEqualityTest() {
    // We want to be sure that the ordering of the factors does not affect equality

    // x * 2.0
    Expression mul = mult(x(), constant(2.0));
    // 2.0 * x
    Expression mul2 = mult(constant(2.0), x());
    assertEquals(mul, mul2);
  }

  @Test
  public void addEqualityTest() {
    // We want to be sure that the ordering of the terms does not affect equality

    // x + 2.0
    Expression add = add(x(), constant(2.0));
    // 2.0 + x
    Expression add2 = add(constant(2.0), x());
    assertEquals(add, add2);
  }

  @Test
  public void constantTest() {
    System.out.println(mult(constant(2.0), constant(3.0)));
  }

  @Test
  public void logTest() {
    Expression lg = log(e(), x());
    System.out.println(lg.differentiate("x"));
  }
}