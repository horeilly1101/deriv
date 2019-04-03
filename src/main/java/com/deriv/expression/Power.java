package com.deriv.expression;

import com.deriv.expression.cmd.DerivativeCmd;
import com.deriv.simplifier.PowerSimplifier;
import com.deriv.util.Tuple;

import java.util.Optional;

import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;

public class Power extends AExpression {
  private Expression base;
  private Expression exponent;

  /**
   * Instantiates a Power object. Avoid using as much as possible! Use the easy constructor
   * instead. (A power is the more general form of a polynomial and an exponential.)
   *
   * Data definition: a power is two Expressions (a base and an exponent).
   */
  Power(Expression base, Expression exponent) {
    this.base = base;
    this.exponent = exponent;
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
    return exponent;
  }

  @Override
  public Expression getBase() {
    return base;
  }

  @Override
  public Expression getNumerator() {
    return exponent.isNegative() ? multID() : this;
  }

  @Override
  public Expression getDenominator() {
    return exponent.isNegative() ? poly(this, -1) : multID();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Power)) {
      return false;
    }

    Power pow = (Power) o;
    return pow.base.equals(this.base) && pow.exponent.equals(this.exponent);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 12;
  }

  @Override
  public String toString() {
    return this.exponent.isNegative()
               ? "1 / " + power(base, negate(exponent)).toString()
               : base.toString() + " ^ " + exponent.toString();
  }

  @Override
  public String toLaTex() {
    System.out.println("hefadfadfd");
    return this.exponent.isNegative()
             ? "\\frac{1}{" + power(base, negate(exponent)).toLaTex() + "}"
             : base.toLaTex() + " ^{" + exponent.toLaTex() + "}";
  }

  public Optional<Expression> evaluate(Variable var, Expression input) {
    return base.evaluate(var, input)
               .flatMap(ba -> exponent.evaluate(var, input)
                                  // make sure we're not dividing by zero
                                  .flatMap(ex -> ba.equals(addID()) && ex.isNegative()
                                                     ? Optional.empty()
                                                     : Optional.of(power(ba, ex))));
  }

  public Expression computeDerivative(Variable var, DerivativeCmd<Tuple<Expression, Variable>, Expression> cache) {
    // I'm not using any of the cookie cutter power rules here.
    // This is a more general approach to differentiating powers.
    // Take the derivative of f(x) ^ g(x) for arbitrary f, g and
    // this is what you'll get.
    Expression firstDerivative = mult(exponent, ln(base))
                                   .deriveCache(var, cache);

    return Mult.mult(
        power(base, exponent),
        firstDerivative)
             .addStep(Step.POWER_RULE, this)
             .extendSteps(firstDerivative.getSteps());
  }

  /**
   * Tests whether or not a Power is simplified, for testing purposes.
   */
  Boolean isSimplified() {
    return new PowerSimplifierComplete(base, exponent).isSimplified();
  }

  private static class PowerSimplifierComplete extends PowerSimplifier {
    PowerSimplifierComplete(Expression unBase, Expression unExponent) {
      super(unBase, unExponent);
    }

    public Expression toExpression() {
      // is exponent 1?
      if (unExponent.equals(Constant.multID())) {
        return unBase;
      }

      // is base 1?
      if (unBase.equals(multID())) {
        return multID();
      }

      // is exponent 0?
      if (unExponent.equals(Constant.addID())) {
        return multID();
      }

      return new Power(unBase, unExponent);
    }
  }
}