package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Add.*;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Log.ln;
import static horeilly1101.Expression.Mult.mult;
import static horeilly1101.Expression.Power.*;
import static horeilly1101.Expression.Div.*;
import static horeilly1101.Expression.Trig.sin;
import static horeilly1101.Expression.Variable.x;

public class DivTest {
  @Test
  public void divTest() {
    System.out.println(div(power(x(), constant(3)), ln(x())));

    System.out.println(div(multID(), div(multID(), e())));

    System.out.println(div(constant(2), constant(3)));

    System.out.println(div(mult(constant(4), constant(3)), constant(15)));
  }

  @Test
  public void differentiateTest() {
    System.out.println(div(poly(x(), 2), sin(x())).differentiate("x"));
  }
}
