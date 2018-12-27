package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Div.div;
import static horeilly1101.Expression.Log.*;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Power.*;
import static horeilly1101.Expression.Trig.*;
import static horeilly1101.Expression.Variable.*;
import static org.junit.Assert.assertEquals;

public class TrigTest {

  @Test
  public void trigTest() {
    // we want to make sure everything is instantiated without exceptions
    sin(x());
    cos(x());
    tan(x());
    csc(x());
    sec(x());
    cot(x());
  }

  @Test
  public void evaluateTest() {
    // sin(x)
    Expression si = sin(x());
    assertEquals(sin(multID()), si.evaluate("x", 1.0));

    // cos(x)
    Expression co = cos(x());
    assertEquals(cos(constant(2)), co.evaluate("x", 2.0));

    // tan(x)
    Expression ta = tan(x());
    assertEquals(tan(constant(2)), ta.evaluate("x", 2.0));

    // csc(x)
    Expression cs = csc(x());
    assertEquals(csc(constant(2)), cs.evaluate("x", 2.0));

    // sec(x)
    Expression se = sec(x());
    assertEquals(sec(constant(2)), se.evaluate("x", 2.0));

    // cot(x)
    Expression cota = cot(x());
    assertEquals(cot(constant(2)), cota.evaluate("x", 2.0));
  }

  @Test
  public void basicDifferentiateTest() {
    // we're just checking the derivatives that are hard coded in
    // sin(x)
    Expression si = sin(x());
    assertEquals(cos(x()), si.differentiate("x"));

    // cos(x)
    Expression co = cos(x());
    assertEquals(negate(sin(x())), co.differentiate("x"));

    // tan(x)
    Expression ta = tan(x());
    assertEquals(poly(sec(x()), 2), ta.differentiate("x"));

    // csc(x)
    Expression cs = csc(x());
    assertEquals(negate(mult(cot(x()), csc(x()))), cs.differentiate("x"));

    // sec(x)
    Expression se = sec(x());
    assertEquals(mult(tan(x()), sec(x())), se.differentiate("x"));

    // cot(x)
    Expression cota = cot(x());
    assertEquals(negate(poly(csc(x()), 2)), cota.differentiate("x"));
  }

  @Test
  public void chainDifferentiateTest() {
    // we'll check out some chain rules
    // sin(ln(x))
    Expression si = sin(ln(x()));
    assertEquals(div(cos(ln(x())), x()), si.differentiate("x"));

    // cos(sin(x))
    Expression co = cos(sin(x()));
    assertEquals(negate(mult(cos(x()), sin(sin(x())))), co.differentiate("x"));
  }
}
