package com.deriv.expression;

import java.util.ArrayList;
import java.util.List;

public abstract class AExpression implements Expression {
  public enum Step {
    LINEARITY, LOG_RULE, PRODUCT_RULE, POWER_RULE, SIN, COS, TAN,
    CSC, SEC, COT, VARIABLE_RULE, CONSTANT_RULE
  }

  public static class Tuple {
    private final Step step;
    private final Expression expression;

    private Tuple(Step step, Expression expression) {
      this.step = step;
      this.expression = expression;
    }

    @Override
    public String toString() {
      return "[" + step.toString() + ", " + expression.toString() + "]";
    }

    static Tuple of(Step step, Expression expression) {
      return new Tuple(step, expression);
    }
  }

  public Expression addStep(Step step, Expression expression) {
    steps.add(Tuple.of(step, expression));
    return this;
  }

  public Expression addStepLeft(Step step, Expression expression) {
    steps.add(0, Tuple.of(step, expression));
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
