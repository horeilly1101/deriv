package com.deriv.expression.cmd;

import com.deriv.expression.step.ExpressionWrapper;
import com.deriv.util.Tree;

public class NullStepCmd implements IStepCmd {
  public NullStepCmd() {
    // no op
  }

  @Override
  public void add(StepCmd cmd) {
    // no op
  }

  @Override
  public Tree<ExpressionWrapper> getSteps() {
    throw new RuntimeException("Called for steps on a null step command!");
  }
}
