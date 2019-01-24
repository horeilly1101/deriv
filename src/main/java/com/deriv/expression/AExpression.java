package com.deriv.expression;

import com.deriv.util.Tuple;
import java.util.ArrayList;
import java.util.List;

public abstract class AExpression implements Expression {
  private List<Tuple<Step, Expression>> steps = new ArrayList<>();

  public enum Step {
    LINEARITY, LOG_RULE, PRODUCT_RULE, POWER_RULE, SIN, COS, TAN,
    CSC, SEC, COT, VARIABLE_RULE, CONSTANT_RULE
  }

  public Expression addStep(Step step, Expression expression) {
    steps.add(Tuple.of(step, expression));
    return this;
  }

  public Expression addStepLeft(Step step, Expression expression) {
    steps.add(0, Tuple.of(step, expression));
    return this;
  }

  public Expression extendSteps(List<Tuple<Step, Expression>> otherSteps) {
    steps.addAll(otherSteps);
    return this;
  }

  public List<Tuple<Step, Expression>> getSteps() {
    return steps;
  }
}