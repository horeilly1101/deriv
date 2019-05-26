package com.deriv.expression;

import com.deriv.expression.simplifier.PowerSimplifier;
import java.util.Optional;

import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;

/**
 * A Power represents an expression to the power of another expression.
 *
 * Data definition: a power is two Expressions (a base and an exponent).
 */
public class Power implements Expression {
  /**
   * The base of our power.
   */
  private Expression _base;

  /**
   * The exponent of our power.
   */
  private Expression _exponent;

  /**
   * Private constructor for a Power. Use one of the static constructors instead.
   */
  private Power(Expression _base, Expression _exponent) {
    this._base = _base;
    this._exponent = _exponent;
  }

  /**
   * Static constructor for a power (an expression to the power of an expression).
   * @param base expr
   * @param exponent expr
   * @return power
   */
  public static Expression power(Expression base, Expression exponent) {
    return new PowerSimplifierComplete(base, exponent).simplifyToExpression();
  }

  /**
   * Static constructor for a polynomial (an expression to the power of an int).
   * @param base expr
   * @param exponent int
   * @return polynomial
   */
  public static Expression poly(Expression base, int exponent) {
    return power(base, constant(exponent));
  }

  /**
   * Static constructor for an exponential (an int to the power of an expression).
   * @param base int
   * @param exponent expr
   * @return exponential
   */
  public static Expression exponential(int base, Expression exponent) {
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

  @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    return _base.evaluate(var, input)
               .flatMap(ba -> _exponent.evaluate(var, input)
                                // make sure we're not dividing by zero
                                 .flatMap(ex -> ba.equals(addID()) && ex.isNegative()
                                                 ? Optional.empty()
                                                 : Optional.of(power(ba, ex))));
  }

  @Override
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
   * @return boolean
   */
  boolean isSimplified() {
    return new PowerSimplifierComplete(_base, _exponent).isSimplified();
  }

  /**
   * Extension of PowerSimplifier that allows us to create Power objects.
   */
  private static class PowerSimplifierComplete extends PowerSimplifier {
    /**
     * Constructor for a PowerSimplifierComplete.
     * @param unBase base
     * @param unExponent exponent
     */
    private PowerSimplifierComplete(Expression unBase, Expression unExponent) {
      super(unBase, unExponent);
    }

    @Override
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