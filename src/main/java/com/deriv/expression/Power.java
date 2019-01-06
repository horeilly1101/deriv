package com.deriv.expression;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;

public class Power implements Expression {
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
    return (new PowerSimplifier(base, exponent)).simplify().toExpression();
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
               ? "(1 / " + power(base, negate(exponent)) + ")"
               : "(" + base.toString() + " ^ " + exponent.toString() + ")";
  }

  public Optional<Expression> evaluate(String var, Double input) {
    return base.evaluate(var, input)
               .flatMap(ba -> exponent.evaluate(var, input)
                                  // make sure we're not dividing by zero
                                  .flatMap(ex -> ba.equals(addID()) && ex.isNegative()
                                                     ? Optional.empty()
                                                     : Optional.of(power(ba, ex))));
  }

  public Expression differentiate(String var) {
    // I'm not using any of the cookie cutter power rules here.
    // This is a more general approach to differentiating powers.
    // Take the derivative of f(x) ^ g(x) for arbitrary f, g and
    // this is what you'll get.

    return Mult.mult(
        power(base, exponent),
        mult(exponent, ln(base))
            .differentiate(var));
  }

  /**
   * Tests whether or not a Power is simplified, for testing purposes.
   */
  Boolean isSimplified() {
    return (new PowerSimplifier(base, exponent)).isSimplified();
  }

  /*
  Private, static class to help us simplify power instantiations.
   */
  private static class PowerSimplifier implements Simplifier {
    private Expression unBase;
    private Expression unExponent;

    PowerSimplifier(Expression unBase, Expression unExponent) {
      this.unBase = unBase;
      this.unExponent = unExponent;
    }

    /**
     * Is the PowerSimplifier instance simplified? See the comments in @PowerSimplifier.simplify()
     * for more detailed descriptions of what the boolean declarations mean.
     */
    public Boolean isSimplified() {
      return !((unBase.isConstant() && unExponent.isConstant() && unExponent.asConstant().isNegative())
                 || (unBase.isConstant() && unExponent.isConstant() && !unExponent.asConstant().isNegative())
                 || unBase.isMult()
                 || unBase.isPower());
    }

    /**
     * Simplifies an a PowerSimplifier object.
     */
    public Simplifier simplify() {
      // is the whole expression a denominator constant?
      if (unBase.isConstant() && unExponent.isConstant() && unExponent.asConstant().isNegative()) {
        return new PowerSimplifier(power(unBase, negate(unExponent)), constant(-1));
      }

      // is the whole expression a constant?
      if (unBase.isConstant() && unExponent.isConstant() && !unExponent.asConstant().isNegative()) {
        // this ugly stack of code just takes the base to the specified power and casts it
        // to an integer
        return new PowerSimplifier(constant((int)
                            Math.round(
                                Math.pow(
                                    unBase.asConstant().getVal(),
                                    unExponent.asConstant().getVal()))), multID());
      }

      // is the base a mult?
      if (unBase.isMult()) {
        return new PowerSimplifier(mult(unBase.asMult().getFactors().stream()
                        .map(x -> power(x, unExponent)).collect(Collectors.toList())), multID());
      }

      // is the base a power?
      if (unBase.isPower()) {
        return new PowerSimplifier(unBase.getBase(), mult(unExponent, unBase.getExponent()));
      }

      return this;
    }

    /**
     * Converts a PowerSimplifier object to an Expression.
     */
    public Expression toExpression() {
      // is exponent 1?
      if (unExponent.equals(Constant.multID())) {
        return unBase;
      }

      // is base 1?
      if (unBase.equals(multID())) {
        return multID();
      }

      // is it 0?
      if (unExponent.equals(Constant.addID())) {
        return multID();
      }

      return new Power(unBase, unExponent);
    }
  }
}