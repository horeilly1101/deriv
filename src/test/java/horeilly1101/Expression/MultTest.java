package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Variable.*;

public class MultTest {

  @Test
  public void multTest() {
    // we just want to make sure the stack doesn't overflow
    mult(x(), e());
  }


}
