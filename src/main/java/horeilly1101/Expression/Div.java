package horeilly1101.Expression;

import static horeilly1101.Expression.Add.add;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Power.*;

public class Div implements Expression {
  private Expression numerator;
  private Expression denominator;

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
    if (denominator.isDiv()) {
      Div den = denominator.asDiv();
      return div(mult(numerator, den.denominator), den.numerator);
    }

    // add a check to simplify constants

    // add a check to simplify adds in numerator

    // let mult do the rest of the simplifying
    return mult(numerator, poly(denominator, constant(-1.0)));
  }

  public Expression getNumerator() {
    return numerator;
  }

  public Expression getDenominator() {
    return denominator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Div)) {
      return false;
    }

    Div di = (Div) o;
    return di.numerator.equals(this.numerator) && di.denominator.equals(this.denominator);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 7;
  }

  @Override
  public String toString() {
    return numerator.toString() + " / " + denominator.toString();
  }

  public Expression evaluate(String var, Double input) {
    // multiplies factors together
    return div(numerator.evaluate(var, input), denominator.evaluate(var, input));
  }

  public Expression differentiate(String var) {
    // quotient rule
    return div(
              add(
                  mult(
                      numerator.differentiate(var),
                      denominator),
                  negate(
                      mult(
                          numerator,
                          denominator.differentiate(var)))),

              poly(denominator, constant(2.0)));
  }
}
