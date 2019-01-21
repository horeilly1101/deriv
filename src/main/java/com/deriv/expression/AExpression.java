package com.deriv.expression;

import java.util.ArrayList;
import java.util.List;

public abstract class AExpression implements Expression {
  enum Step {
    LINEARITY, LOG_RULE, PRODUCT_RULE, POWER_RULE, TRIG_RULE, VARIABLE_RULE, CONSTANT_RULE
  }

  class Tuple {
    private final Step step;
    private final Expression expression;

    private Tuple(Step step, Expression expression) {
      this.step = step;
      this.expression = expression;
    }

    Step getStep() {
      return step;
    }

    Expression getExpression() {
      return expression;
    }

    Tuple of(Step step, Expression expression) {
      return new Tuple(step, expression);
    }
  }

  List<Tuple> steps = new ArrayList<>();
}
