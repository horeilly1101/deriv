package com.deriv.expression;

import com.deriv.expression.cmd.ICacheCmd;
import com.deriv.expression.cmd.IStepCmd;
import java.util.Optional;

import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Mult.*;

public class Log extends AExpression {
  private Expression _base;
  private Expression _result;

  /**
   * Instantiates a Log object. Avoid using as much as possible! Instead, use
   * the easy constructor down below. (I should note that throughout the code
   * _base, I use <b>log(a, b)</b> to refer to a logarithm with _base a and result b.)
   *
   * Data definition: a log is two Expressions (a _base and a result)
   */
  private Log(Expression _base, Expression result) {
    this._base = _base;
    this._result = result;
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
    return log._base.equals(this._base) && log._result.equals(this._result);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 8;
  }

  @Override
  public String toString() {
    if ((this._result.isAdd() || this._result.isMult()) && this._base.equals(e())) {
      return "ln" + this._result.toString();
    }

    return (_base.equals(e()))
               ? "ln(" + _result.toString() + ")"
               : "log(" + _base.toString() + ", " + _result.toString() + ")";
  }

  @Override
  public String toLaTex() {
    return (_base.equals(e()))
             ? "\\ln(" + _result.toLaTex() + ")"
             : "\\log_{" + _base.toLaTex() + "} " + _result.toLaTex();
  }

  public Optional<Expression> evaluate(Variable var, Expression val) {
    return _base.evaluate(var, val)
               .flatMap(ba -> _result.evaluate(var, val)
                                  .flatMap(re -> re.isConstant() && re.asConstant().getVal() <= 0
                                                     ? Optional.empty()
                                                     : Optional.of(log(ba, re))));
  }

  public Expression computeDerivative(Variable var, ICacheCmd cacheCmd, IStepCmd stepCmd) {
    // calculate the derivative of log(g(x), f(x)) for arbitrary
    // g, f and this is what you'll get

    // if log is a natural log
    if (_base.equals(e())) {
      return mult(
        _result.differentiate(var, cacheCmd, stepCmd),
        div(multID(), _result));
    }

   return div(ln(_result), ln(_base)).differentiate(var, cacheCmd, stepCmd);
  }

  public Step getDerivativeStep() {
    return Step.LOG_RULE;
  }
}
