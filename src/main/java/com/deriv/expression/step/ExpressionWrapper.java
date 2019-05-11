package com.deriv.expression.step;

import com.deriv.expression.Expression;

/**
 * Wrapper class that allows us to store an expression and the
 * necessary derivative step in the same place.
 */
public class ExpressionWrapper {
  /**
   * Stored expression.
   */
  private Expression _expr;

  /**
   * Stored step.
   */
  private Step _step;

  /**
   * Private constructor for an Expression Wrapper. User the static constructor
   * instead.
   * @param expression input
   * @param step input
   */
  private ExpressionWrapper(Expression expression, Step step) {
    this._expr = expression;
    this._step = step;
  }

  @Override
  public String toString() {
    return "(" + _expr + ", " + _step + ")";
  }


  /**
   * Public, static constructor for an ExpressionWrapper.
   * @param expression input
   * @param step input
   * @return newly constructed ExpressionWrapper
   */
  public static ExpressionWrapper of(Expression expression, Step step) {
    return new ExpressionWrapper(expression, step);
  }

  /**
   * Getter method for an Expression.
   * @return stored expression
   */
  public Expression getExpression() {
    return _expr;
  }

  /**
   * Getter method for a step.
   * @return stored step
   */
  public Step getStep() {
    return _step;
  }
}