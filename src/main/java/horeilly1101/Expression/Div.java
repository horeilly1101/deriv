package horeilly1101.Expression;

import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Power.*;

public class Div {
  Expression numerator;
  Expression denominator;

  /**
   * Instantiates a Div object. Avoid using as much as possible! Use the
   * easy constructor below instead.
   *
   * Data definition: A div is two expressions (a numerator and a denominator).
   */
  Div(Expression numerator, Expression denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  public Expression div(Expression numerator, Expression denominator) {
    if

    return mult(numerator, poly(denominator, constant(-1.0)));
  }
}
