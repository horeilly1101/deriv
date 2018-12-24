package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Log.ln;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Variable.*;
import static horeilly1101.Expression.Power.*;

public class MultTest {

  @Test
  public void multTest() {
    // we just want to make sure the stack doesn't overflow
    // this has been a recurring problem
    mult(x(), e());

    Expression mul = mult(x(), x());
    System.out.println(mul.evaluate("x", 2.0));
  }
}
