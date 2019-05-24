package com.deriv.expression;

import com.deriv.expression.simplifier.PowerSimplifier;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;

public class Power implements Expression {
  private Expression _base;
  private Expression _exponent;

  /**
   * Instantiates a Power object. Avoid using as much as possible! Use the easy constructor
   * instead. (A power is the more general form of a polynomial and an exponential.)
   *
   * Data definition: a power is two Expressions (a _base and an _exponent).
   */
  Power(Expression _base, Expression _exponent) {
    this._base = _base;
    this._exponent = _exponent;
  }

  /**
   * Use this to instantiate a power.
   */
  public static Expression power(Expression base, Expression exponent) {
    return new PowerSimplifierComplete(base, exponent).simplifyToExpression();
  }

  public static Expression poly(Expression base, Integer exponent) {
    return power(base, constant(exponent));
  }

  public static Expression exponential(Integer base, Expression exponent) {
    return power(constant(base), exponent);
  }

  @Override
  public Expression getExponent() {
    return _exponent;
  }

  @Override
  public Expression getBase() {
    return _base;
  }

  @Override
  public Expression getNumerator() {
    return _exponent.isNegative() ? multID() : this;
  }

  @Override
  public Expression getDenominator() {
    return _exponent.isNegative() ? poly(this, -1) : multID();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof Power))
      return false;

    Power pow = (Power) o;
    return pow._base.equals(_base) && pow._exponent.equals(_exponent);
  }

  @Override
  public int hashCode() {
    return 31 * _base.hashCode() + _exponent.hashCode();
  }

  @Override
  public String toString() {
    return this._exponent.isNegative()
               ? "1 / " + power(_base, negate(_exponent)).toString()
               : _base.toString() + " ^ " + _exponent.toString();
  }

  @Override
  public String toLaTex() {
    return this._exponent.isNegative()
             ? "\\frac{1}{" + power(_base, negate(_exponent)).toLaTex() + "}"
             : _base.toLaTex() + " ^{" + _exponent.toLaTex() + "}";
  }

  public Optional<Expression> evaluate(Variable var, Expression input) {
    /*
     * Note: the commented out code below runs the two derivatives in parallel.
     * This is a cool idea, but the overhead required just doesn't make it worth it.
     */

//     ExecutorService executorService = Executors.newCachedThreadPool();
//     Future<Optional<Expression>> exponentEval = executorService
//                                                    .submit(() -> _exponent.evaluate(var, input));

    return _base.evaluate(var, input)
               .flatMap(ba -> _exponent.evaluate(var, input)
                                // make sure we're not dividing by zero
                                 .flatMap(ex -> ba.equals(addID()) && ex.isNegative()
                                                 ? Optional.empty()
                                                 : Optional.of(power(ba, ex))));
  }

  public Optional<Expression> differentiate(Variable var) {
    // I'm not using any of the cookie cutter power rules here.
    // This is a more general approach to differentiating powers.
    // Take the derivative of f(x) ^ g(x) for arbitrary f, g and
    // this is what you'll get.

    return mult(_exponent, ln(_base))
             .differentiate(var)
             .map(x -> mult(power(_base, _exponent), x));
  }

  /**
   * Tests whether or not a Power is simplified, for  testing purposes.
   */
  Boolean isSimplified() {
    return new PowerSimplifierComplete(_base, _exponent).isSimplified();
  }

  private static class PowerSimplifierComplete extends PowerSimplifier {
    PowerSimplifierComplete(Expression unBase, Expression unExponent) {
      super(unBase, unExponent);
    }

    public Expression toExpression() {
      // is _exponent 1?
      if (unExponent.equals(Constant.multID())) {
        return unBase;
      }

      // is _base 1?
      if (unBase.equals(multID())) {
        return multID();
      }

      // is _exponent 0?
      if (unExponent.equals(Constant.addID())) {
        return multID();
      }

      return new Power(unBase, unExponent);
    }
  }
}