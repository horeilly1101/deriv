package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Constant.constant;
import static horeilly1101.Expression.Constant.multID;
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
    Expression si = sin(x());
    assertEquals(sin(multID()), si.evaluate("x", 1.0));

    Expression co = cos(x());
    assertEquals(cos(constant(2)), co.evaluate("x", 2.0));

    Expression ta = tan(x());
    assertEquals(tan(constant(2)), ta.evaluate("x", 2.0));

    Expression cs = csc(x());
    assertEquals(csc(constant(2)), cs.evaluate("x", 2.0));

    Expression se = sec(x());
    assertEquals(sec(constant(2)), se.evaluate("x", 2.0));

    Expression cota = cot(x());
    assertEquals(cot(constant(2)), cota.evaluate("x", 2.0));
  }

  @Test
  public void differentiateTest() {
//    Expression si = sin(x());
//    assertEquals(sin(multID()), si.evaluate("x", 1.0));
//
//    Expression co = cos(x());
//    assertEquals(cos(constant(2.0)), co.evaluate("x", 2.0));
//
//    Expression ta = tan(x());
//    assertEquals(tan(constant(2.0)), ta.evaluate("x", 2.0));
//
//    Expression cs = csc(x());
//    assertEquals(csc(constant(2.0)), cs.evaluate("x", 2.0));
//
//    Expression se = sec(x());
//    assertEquals(sec(constant(2.0)), se.evaluate("x", 2.0));
//
//    Expression cota = cot(x());
//    assertEquals(cot(constant(2.0)), cota.evaluate("x", 2.0));
  }
}
