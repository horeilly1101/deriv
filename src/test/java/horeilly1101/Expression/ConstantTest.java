package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Constant.*;
import static org.junit.Assert.assertEquals;

public class ConstantTest {

  @Test
  public void derivativeTest() {
    // constants are tested pretty hard by the other unit tests,
    // so I'm just going to check their derivatives for the sake
    // of completeness

    Expression c = constant("c");
    assertEquals(addID(), c.differentiate("x"));

    Expression one = multID();
    assertEquals(addID(), one.differentiate("x"));
  }
}
