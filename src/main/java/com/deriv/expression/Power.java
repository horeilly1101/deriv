package com.deriv.expression;

import com.deriv.expression.simplifier.PowerSimplifier;
import com.deriv.util.ThreadManager;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Constant.addID;
import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Log.ln;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Mult.negate;

/**
 * A Power represents an expression to the power of another expression.
 *
 * Data definition: a power is two Expressions (a base and an exponent).
 */
public class Power implements Expression {
  /**
   * The base of our power.
   */
  private final Expression _base;

  /**
   * The exponent of our power.
   */
  private final Expression _exponent;

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

  /**
   * Invert the input expression (i.e. raise it to the power of -1).
   * @param expr input expression
   * @return inverted expression
   */
  public static Expression inverse(Expression expr) {
    return poly(expr, -1);
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
    if (this._exponent.isNegative())
      return "1 / " + power(_base, negate(_exponent)).toString();

    return _base.toString() + " ^ " + _exponent.toString();
  }

  @Override
  public String toLaTex() {
    return this._exponent.isNegative()
             ? "\\frac{1}{" + power(_base, negate(_exponent)).toLaTex() + "}"
             : _base.toLaTex() + " ^{" + _exponent.toLaTex() + "}";
  }

  @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    Future<Optional<Expression>> futureExponent = ThreadManager.submitTask(() -> _exponent.evaluate(var, input));

    return _base.evaluate(var, input)
               .flatMap(ba -> ExpressionUtils.oGetFuture(futureExponent)
                       .flatMap(ex -> ba.equals(addID()) && ex.isNegative()
                               // make sure we're not dividing by zero
                               ? Optional.empty()
                               : Optional.of(power(ba, ex))));
  }

  @Override
  public Optional<Expression> differentiate(Variable var) {
    // I'm not using any of the cookie cutter power rules here. This is a more general approach to differentiating
    // powers. Take the derivative of f(x) ^ g(x) for arbitrary f, g and this is what you'll get.

    return mult(_exponent, ln(_base))
             .differentiate(var)
             .map(x -> mult(power(_base, _exponent), x));
  }

  @Override
  public Set<Variable> getVariables() {
    Set<Variable> varSet = _exponent.getVariables();
    varSet.addAll(_base.getVariables());
    return varSet;
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
      // is exponent 1?
      if (unExponent.equals(Constant.multID()))
        return unBase;


      // is base 1?
      if (unBase.equals(multID()))
        return multID();

      // is exponent 0?
      if (unExponent.equals(Constant.addID()))
        return multID();

      return new Power(unBase, unExponent);
    }
  }
}