package com.deriv.expression.step;

import com.deriv.expression.Expression;

public class ExpressionWrapper {
  private Expression _expr;
  private Step _step;

  private ExpressionWrapper(Expression expression, Step step) {
    this._expr = expression;
    this._step = step;
  }

  @Override
  public String toString() {
    return "(" + _expr + ", " + _step + ")";
  }

  public static ExpressionWrapper of(Expression expression, Step step) {
    return new ExpressionWrapper(expression, step);
  }

  public Expression getExpression() {
    return _expr;
  }

  public Step getStep() {
    return _step;
  }
}