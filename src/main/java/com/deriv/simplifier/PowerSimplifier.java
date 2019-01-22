package com.deriv.simplifier;

import com.deriv.expression.Expression;
import java.util.stream.Collectors;

import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Mult.negate;
import static com.deriv.expression.Power.power;

/**
  Private, static class to help us simplify power instantiations.
 */
public abstract class PowerSimplifier implements Simplifier {
  protected Expression unBase;
  protected Expression unExponent;

  public PowerSimplifier(Expression unBase, Expression unExponent) {
    this.unBase = unBase;
    this.unExponent = unExponent;
  }

  /**
   * Converts a PowerSimplifier object to an Expression.
   */
  public abstract Expression toExpression();

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
      this.unBase = power(unBase, negate(unExponent));
      this.unExponent = constant(-1);

      return this;
    }

    // is the whole expression a constant?
    if (unBase.isConstant() && unExponent.isConstant() && !unExponent.isNegative()) {
      // this ugly stack of code just takes the base to the specified power and casts it
      // to an integer
      this.unBase = constant((int) Math.round(Math.pow(
                                                  unBase.asConstant().getVal(),
                                                  unExponent.asConstant().getVal())));
      this.unExponent = multID();

      return this;
    }

    // is the base a mult?
    if (unBase.isMult()) {
      this.unBase = mult(unBase.asMult().getFactors().stream()
                                        .map(x -> power(x, unExponent)).collect(Collectors.toList()));
      this.unExponent = multID();

      return this;
    }

    // is the base a power?
    if (unBase.isPower()) {
      Expression beforeBase = unBase;
      this.unBase = unBase.getBase();
      this.unExponent = mult(unExponent, beforeBase.getExponent());

      return this;
    }

    return this;
  }
}
