package com.deriv.expression;

public interface Simplifier {
  /**
   * Returns whether or not a simplifier is simplified.
   */
  Boolean isSimplified();

  /**
   * Simplifies a simplifier.
   */
  Simplifier simplify();

  /**
   * Converts a simplifier to an Expression.
   */
  Expression toExpression();
}
