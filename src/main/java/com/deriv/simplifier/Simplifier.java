package com.deriv.simplifier;

import com.deriv.expression.Expression;

/**
 * A simplifier is used to simplify the construction of an Expression
 * object.
 *
 * For example, suppose we want to create an Add object. We're
 * going to need x + 3x = 2x + 2x = 4x for our construction to make
 * any sense. A simplifier will help us does this.
 */
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
