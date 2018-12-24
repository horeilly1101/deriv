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

public class AddTest {

  @Test
  public void addTest() {
    // we first want to make sure the stack doesn't overflow
    // this has been a recurring problem
    add(x(), e());
  }

  @Test(expected = RuntimeException.class)
  public void emptyListTest() {
    // we want to make sure this throws an exception
    add();
  }

  @Test
  public void commutativityTest() {
    // x + sin(x)
    Expression ex = add(x(), sin(x()));
    // sin(x) + x
    Expression ex2 = add(sin(x()), x());
    assertEquals(ex, ex2);
  }

  @Test
  public void nestingTest() {
    // x + (x + (x + (x + ln(x))))
    Expression ex = add(x(), add(x(), add(x(), add(x(), ln(x())))));
    assertEquals(add(mult(x(), constant(4.0)), ln(x())), ex);
  }

  @Test
  public void constantTest() {
    // 2 + 3 + 4
    Expression ex = add(constant(2.0), constant(3.0), constant(4.0));
    assertEquals(constant(9.0), ex);

    // 0 + ln(x)
    Expression ex2 = add(addID(), ln(x()));
    assertEquals(ln(x()), ex2);
  }

  @Test
  public void counntTest() {
    // ln(x) + ln(x) + ln(x)
    Expression ex = add(ln(x()), ln(x()), ln(x()));
    assertEquals(mult(ln(x()), constant(3.0)), ex);
  }

  @Test
  public void evaluateTest() {
    // x + x + 2
    Expression ex = add(x(), x(), constant(2.0));
    assertEquals(constant(6.0), ex.evaluate("x", 2.0));

    // x + a + 3, where a is a constant
    Expression ex2 = add(x(), constant("a"), constant(3.0));
    assertEquals(add(constant("a"), constant(6.0)), ex2.evaluate("x", 3.0));
  }

  @Test
  public void derivativeTest() {
    // x + x + 2
    Expression ex = add(x(), x(), constant(2.0));
    assertEquals(constant(2.0), ex.differentiate("x"));

    // a * x + 3, where a is a constant
    Expression ex2 = add(mult(x(), constant("a")), constant(3.0));
    assertEquals(constant("a"), ex2.differentiate("x"));

    // x + ln(x)
    Expression ex3 = add(x(), ln(x()));
    assertEquals(add(multID(), poly(x(), constant(-1.0))), ex3.differentiate("x"));

    // x + sin(x)
    Expression ex4 = add(x(), sin(x()));
    assertEquals(add(multID(), cos(x())), ex4.differentiate("x"));
  }
}
