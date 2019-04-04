package com.deriv.expression.cmd;

import com.deriv.expression.AExpression.*;
import com.deriv.expression.Expression;
import com.deriv.util.Tuple;
import java.util.ArrayList;
import java.util.List;

public class StepCmd implements IStepCmd {
  private List<Tuple<Step, Expression>> steps;

  public StepCmd() {
    this.steps = new ArrayList<>();
  }

  public void addStep(Step step, Expression expression) {
    this.steps.add(Tuple.of(step, expression));
  }

  public List<Tuple<Step, Expression>> getSteps() {
    return steps;
  }
}
