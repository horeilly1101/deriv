package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Log.*;
import static horeilly1101.Expression.Power.power;
import static horeilly1101.Expression.Variable.x;

public class LogTest {

  @Test
  public void logTest() {
    System.out.println(power(constant(5), x()).differentiate("x"));
  }
}
