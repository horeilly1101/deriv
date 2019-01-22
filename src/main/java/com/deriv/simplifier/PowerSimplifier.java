package com.deriv.simplifier;

import com.deriv.expression.Expression;
import java.util.stream.Collectors;

import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Mult.negate;
import static com.deriv.expression.Power.power;

/*
  Private, static class to help us simplify power instantiations.
   */
public class PowerSimplifier implements Simplifier {
  protected Expression unBase;
  protected Expression unExponent;

  public PowerSimplifier(Expression unBase, Expression unExponent) {
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
    throw new RuntimeException("This method is overridden in the Power class!");
  }
}
