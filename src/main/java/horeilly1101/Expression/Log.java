package horeilly1101.Expression;

import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Add.*;
//import static horeilly1101.Expression.Div.div;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Power.*;

public class Log implements Expression {
  private Expression base;
  private Expression result;

  /**
   * Instantiates a Log object. Avoid using as much as possible! Instead, use
   * the easy constructor down below. (I should note that throughout the code
   * base, I use <b>log(a, b)</b> to refer to a logarithm with base a and result b.)
   *
   * Data definition: a log is two Expressions (a base and a result)
   */
  private Log(Expression base, Expression result) {
    this.base = base;
    this.result = result;
  }

  /**
   * Use this method to instantiate a Log object.
   */
  static Expression log(Expression base, Expression result) {
    // future addition, simplify something like log(2, 4) or log(3, 27)
    // idea: just implement getBase() to output the root of a constant,
    // if it's a perfect root

    if (result.getBase().equals(base)) {
      return result.getExponent();
    }

    return new Log(base, result);
  }

  /**
   * Instantiate a natural logarithm.
   */
  static Expression ln(Expression result) {
    return log(Constant.e(), result);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Log)) {
      return false;
    }

    Log log = (Log) o;
    return log.base.equals(this.base) && log.result.equals(this.result);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 8;
  }

  @Override
  public String toString() {
    return (base.equals(e()))
               ? "ln(" + result.toString() + ")"
               : "log(" + base.toString() + ", " + result.toString() + ")";
  }

  public Expression evaluate(String var, Double val) {
    return log(base.evaluate(var, val), result.evaluate(var, val));
  }

  public Expression differentiate(String var) {
    // calculate the derivative of log(g(x), f(x)) for arbitrary
    // g, f and this is what you'll get
    return base.equals(e())
               // derivative of natural log
               ? mult(result.differentiate(var), div(multID(), result))
               : div(ln(result), ln(base)).differentiate(var);
  }
}
