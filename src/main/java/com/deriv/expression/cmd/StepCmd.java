package com.deriv.expression.cmd;

import com.deriv.expression.step.ExpressionWrapper;
import com.deriv.util.Tree;

public class StepCmd implements IStepCmd {
  private Tree<ExpressionWrapper> steps;

  public StepCmd(ExpressionWrapper tup) {
    this.steps = Tree.makeNode(tup);
  }

  public void add(StepCmd cmd) {
    steps.add(cmd.getSteps());
  }

  public Tree<ExpressionWrapper> getSteps() {
    return steps;
  }
}
