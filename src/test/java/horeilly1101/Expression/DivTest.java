package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Add.*;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Log.ln;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Variable.x;
import static junit.framework.TestCase.assertEquals;

public class DivTest {
  @Test
  public void divTest() {
    // we want to make sure the stack doesn't overflow
    div(ln(x()), x());
  }

  @Test
  public void nestTest() {
    // 1 / (1 / x)
    Expression dv = div(multID(), div(multID(), x()));
    assertEquals(x(), dv);

    // 1 / (1 / (1 / x))
    Expression dv2 = div(multID(), div(multID(), div(multID(), x())));
    assertEquals(div(multID(), x()), dv2);
  }

  @Test
  public void simplifyConstantTest() {
    // 2 / 4
    Expression con = div(constant(2), constant(4));
    assertEquals(div(multID(), constant(2)), con);

    // 81 / 3
    Expression con2 = div(constant(81), constant(3));
    assertEquals(constant(27), con2);
  }

  @Test
  public void evaluateTest() {
    // x / 3
    Expression ex = div(x(), constant(3));
    assertEquals(constant(27), ex.evaluate("x", 81.0));

    // (x + 1) / 3
    Expression ex2 = div(add(x(), multID()), constant(3));
    assertEquals(constant(27), ex2.evaluate("x", 80.0));

    // ln(x + 1) / x
    Expression ex3 = div(ln(add(x(), multID())), x());
    assertEquals(ln(constant(2)), ex3.evaluate("x", 1.0));
  }

  @Test
  public void differentiateTest() {
    // this be fixed by adding divs
//    // sin(x) / x
//    Expression ex = div(sin(x()), x());
//    Expression result = div(add(mult(cos(x()), x()), negate(sin(x()))), poly(x(), 2));
//    assertEquals(result, ex.differentiate("x"));
  }

  @Test
  public void denomTest() {
    System.out.println(add(div(multID(), x()), div(multID(), x())));
  }
}
