package com.deriv.expression;

import com.deriv.util.ThreadManager;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Constant.e;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Mult.div;
import static com.deriv.expression.ExpressionUtils.oGetFuture;

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
  private final Expression _base;

  /**
   * The result (input) of the logarithm.
   */
  private final Expression _result;

  /**
   * Private constructor for a Log. Use one of the static constructors instead.
   */
  private Log(Expression base, Expression result) {
    this._base = base;
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
    if (_base.equals(e()))
      return "\\ln(" + _result.toLaTex() + ")";

    return "\\log_{" + _base.toLaTex() + "} " + _result.toLaTex();
  }

  @Override
  public Optional<Expression> evaluate(Variable var, Expression val) {
    Future<Optional<Expression>> futureBase = ThreadManager.submitTask(() -> _result.evaluate(var, val));

    return _base.evaluate(var, val)
               .flatMap(ba -> oGetFuture(futureBase)
                       .flatMap(re -> re.isConstant() && re.asConstant().getVal() <= 0
                               ? Optional.empty()
                               : Optional.of(log(ba, re))));
  }

  @Override
  public Optional<Expression> differentiate(Variable var) {
    // calculate the derivative of log(g(x), f(x)) for arbitrary
    // g, f and this is what you'll get

    if (_base.equals(e())) // if log is a natural log
      return _result.differentiate(var).map(x -> mult(x, div(multID(), _result)));

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
