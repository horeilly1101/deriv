package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Mult.mult;
import static horeilly1101.Expression.Variable.*;
import static org.junit.Assert.assertEquals;

public class VariableTest {

  @Test(expected = RuntimeException.class)
  public void eTest() {
    // should throw an exception
    var("e");
  }

  @Test
  public void derivativeTest() {
    // x
    Expression xVar = x();
    assertEquals(multID(), xVar.differentiate("x"));
    assertEquals(addID(), xVar.differentiate("y"));

    // y
    Expression yVar = var("y");
    assertEquals(multID(), yVar.differentiate("y"));
    assertEquals(addID(), yVar.differentiate("x"));
  }

  @Test
  public void multiDerivativeTest() {
    // x * y
    Expression ex = mult(x(), var("y"));
    assertEquals(x(), ex.differentiate("y"));
    assertEquals(var("y"), ex.differentiate("x"));
  }
}
