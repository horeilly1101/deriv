package horeilly1101;

import com.sun.tools.javac.util.List;
//import org.junit.Test;

import static horeilly1101.Expression.Add.*;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Power.*;
import static horeilly1101.Expression.Variable.*;

public class ExpressionTest {

  public static void main(String[] args) {
    // x ^ 2.0
    Expression pol = poly(var(), 2.0);
    // 1.0
    Expression con1 = constant(1.0);
    // x
    Expression con2 = var();
    // x ^ 2.0 + x
    Expression term1 = mult(List.of(pol, con2));
    // 1.0
    Expression term2 = mult(List.of(con1));

    // x ^ 2.0 * x + 1.0
    Expression expr = add(List.of(term1, term2));

    // calculate expression
    System.out.println("expr EVAL " + expr.evaluate(2.0));
    System.out.println("expr STRING " + expr.toString());

    // calculate derivative
    System.out.println("deriv EVAL " + expr.differentiate().evaluate(2.0));
    System.out.println("deriv STRING " + expr.differentiate());

    Expression cos = Trig.cos(Variable.var());
    System.out.println(cos.differentiate());

    System.out.println(term2.getClass().equals(Constant.class));

    Expression ex = add(
        var(),
        var(),
        poly(var(), 2.0),
        poly(var(), 2.0),
        poly(mult(Constant.constant(4.0), var()), 2.0));

    System.out.println("deriv " + ex);
  }
}