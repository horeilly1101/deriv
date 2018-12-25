package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Log.ln;
import static horeilly1101.Expression.Power.*;
import static horeilly1101.Expression.Div.*;
import static horeilly1101.Expression.Variable.x;

public class DivTest {
  @Test
  public void divTest() {
    System.out.println(div(power(x(), constant(3)), ln(x())));

    System.out.println(div(multID(), div(multID(), e())));
  }
}
