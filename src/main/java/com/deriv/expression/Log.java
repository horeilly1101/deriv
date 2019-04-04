package com.deriv.expression;

import com.deriv.expression.cmd.ICacheCmd;
import com.deriv.expression.cmd.IStepCmd;
import java.util.Optional;

import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Mult.*;

public class Log extends AExpression {
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
  public static Expression log(Expression base, Expression result) {
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
  public static Expression ln(Expression result) {
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
    if ((this.result.isAdd() || this.result.isMult()) && this.base.equals(e())) {
      return "ln" + this.result.toString();
    }

    return (base.equals(e()))
               ? "ln(" + result.toString() + ")"
               : "log(" + base.toString() + ", " + result.toString() + ")";
  }

  @Override
  public String toLaTex() {
    return (base.equals(e()))
             ? "\\ln(" + result.toLaTex() + ")"
             : "\\log_{" + base.toLaTex() + "} " + result.toLaTex();
  }

  public Optional<Expression> evaluate(Variable var, Expression val) {
    return base.evaluate(var, val)
               .flatMap(ba -> result.evaluate(var, val)
                                  .flatMap(re -> re.isConstant() && re.asConstant().getVal() <= 0
                                                     ? Optional.empty()
                                                     : Optional.of(log(ba, re))));
  }

  public Expression computeDerivative(Variable var, ICacheCmd cacheCmd, IStepCmd stepCmd) {
    // calculate the derivative of log(g(x), f(x)) for arbitrary
    // g, f and this is what you'll get

    // if log is a natural log
    if (base.equals(e())) {
      return mult(
        result.differentiate(var, cacheCmd, stepCmd),
        div(multID(), result));
    }

   return div(ln(result), ln(base)).differentiate(var, cacheCmd, stepCmd);
  }

  public Step getDerivativeStep() {
    return Step.LOG_RULE;
  }
}
