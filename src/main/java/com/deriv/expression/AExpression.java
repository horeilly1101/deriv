package com.deriv.expression;

import java.util.ArrayList;
import java.util.List;

abstract class AExpression implements Expression {
  enum Step {
    LINEARITY, LOG_RULE, PRODUCT_RULE, POWER_RULE, SIN, COS, TAN,
    CSC, SEC, COT, VARIABLE_RULE, CONSTANT_RULE
  }

  static class Tuple {
    private final Step step;
    private final Expression expression;

    private Tuple(Step step, Expression expression) {
      this.step = step;
      this.expression = expression;
    }

    @Override
    public String toString() {
      return "(" + step.toString() + ", " + expression.toString() + ")";
    }

    Step getStep() {
      return step;
    }

    Expression getExpression() {
      return expression;
    }

    static Tuple of(Step step, Expression expression) {
      return new Tuple(step, expression);
    }
  }

  public Expression addStep(Step step, Expression expression) {
    steps.add(Tuple.of(step, expression));
    return this;
  }

  public Expression extendSteps(List<Tuple> otherSteps) {
    steps.addAll(otherSteps);
    return this;
  }

  private List<Tuple> steps = new ArrayList<>();

  public List<Tuple> getSteps() {
    return steps;
  }
}
