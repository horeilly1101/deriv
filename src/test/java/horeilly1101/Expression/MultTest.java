package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Log.*;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Add.*;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Trig.*;
import static horeilly1101.Expression.Variable.*;
import static horeilly1101.Expression.Power.*;
import static org.junit.Assert.assertEquals;

public class MultTest {

  @Test
  public void multTest() {
    // we first want to make sure the stack doesn't overflow
    // this has been a recurring problem
    mult(x(), e());
  }

  @Test(expected = RuntimeException.class)
  public void emptyListTest() {
    // we want to make sure this throws an exception
    mult();
  }

  @Test
  public void commutativityTest() {
    // x * sin(x)
    Expression ex = mult(x(), sin(x()));
    // sin(x) * x
    Expression ex2 = mult(sin(x()), x());
    assertEquals(ex, ex2);
  }

  @Test
  public void nestingTest() {
    // x * (x * (x * (x * ln(x))))
    Expression ex = mult(x(), mult(x(), mult(x(), mult(x(), ln(x())))));
    assertEquals(mult(poly(x(), constant(4.0)), ln(x())), ex);
  }

  @Test
  public void constantTest() {
    // 2 * 3 * 4
    Expression ex = mult(constant(2.0), constant(3.0), constant(4.0));
    assertEquals(constant(24.0), ex);

    // 0 * ln(x)
    Expression ex2 = mult(addID(), ln(x()));
    assertEquals(addID(), ex2);
  }

  @Test
  public void exponentTest() {
    // ln(x) * ln(x) * ln(x)
    Expression ex = mult(ln(x()), ln(x()), ln(x()));
    assertEquals(power(ln(x()), constant(3.0)), ex);
  }

  @Test
  public void evaluateTest() {
    // x * x * 2
    Expression ex = mult(x(), x(), constant(2.0));
    assertEquals(constant(8.0), ex.evaluate("x", 2.0));

    // x * a * 3, where a is a constant
    Expression ex2 = mult(x(), constant("a"), constant(3.0));
    assertEquals(mult(constant("a"), constant(9.0)), ex2.evaluate("x", 3.0));
  }

  @Test
  public void derivativeTest() {
    // x * x * 2
    Expression ex = mult(x(), x(), constant(2.0));
    assertEquals(mult(constant(4.0), x()), ex.differentiate("x"));

    // x * a * 3, where a is a constant
    Expression ex2 = mult(x(), constant("a"), constant(3.0));
    assertEquals(mult(constant("a"), constant(3.0)), ex2.differentiate("x"));

    // x * ln(x)
    Expression ex3 = mult(x(), ln(x()));
    assertEquals(add(multID(), ln(x())), ex3.differentiate("x"));

    // x * sin(x)
    Expression ex4 = mult(x(), sin(x()));
    assertEquals(add(sin(x()), mult(x(), cos(x()))), ex4.differentiate("x"));
  }
}
