package com.deriv.expression;

import java.util.Optional;
import java.util.Set;

import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Mult.*;

/**
 * A log is the logarithm of an Expression. (I should note that throughout the code
 * base, I use log(a, b) to refer to a logarithm with base a and result b.)
 *
 * Data definition: a log is two Expressions (a base and a result).
 */
public class Log implements Expression {
  /**
   * The base of the logarithm.
   */
  private Expression _base;

  /**
   * The result (input) of the logarithm.
   */
  private Expression _result;

  /**
   * Private constructor for a Log. Use one of the static constructors instead.
   */
  private Log(Expression _base, Expression result) {
    this._base = _base;
    this._result = result;
  }

  /**
   * Static constructor for a logarithm.
   * @param base of the log
   * @param result input of the log
   * @return new log
   */
  public static Expression log(Expression base, Expression result) {
    // future addition, simplify something like log(2, 4) or log(3, 27)
    // idea: just implement getBase() to output the root of a constant,
    // if it's a perfect root

    if (result.getBase().equals(base))
      return result.getExponent();

    return new Log(base, result);
  }

  /**
   * Static constructor for a natural logarithm.
   * @param result input
   * @return new natural log
   */
  public static Expression ln(Expression result) {
    return log(Constant.e(), result);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof Log))
      return false;

    Log log = (Log) o;
    return log._base.equals(_base) && log._result.equals(_result);
  }

  @Override
  public int hashCode() {
    return 31 * _base.hashCode() + _result.hashCode();
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

  @Override
  public Optional<Expression> evaluate(Variable var, Expression val) {
    return _base.evaluate(var, val)
               .flatMap(ba -> _result.evaluate(var, val)
                                  .flatMap(re -> re.isConstant() && re.asConstant().getVal() <= 0
                                                     ? Optional.empty()
                                                     : Optional.of(log(ba, re))));
  }

  @Override
  public Optional<Expression> differentiate(Variable var) {
    // calculate the derivative of log(g(x), f(x)) for arbitrary
    // g, f and this is what you'll get

    // if log is a natural log
    if (_base.equals(e())) {
      return _result.differentiate(var)
               .map(x -> mult(x, div(multID(), _result)));
    }

    // otherwise
    return div(ln(_result), ln(_base)).differentiate(var);
  }

  @Override
  public Set<Variable> getVariables() {
    Set<Variable> varSet = _result.getVariables();
    varSet.addAll(_base.getVariables());
    return varSet;
  }
}
