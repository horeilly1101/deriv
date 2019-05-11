package com.deriv.expression.cmd;

import com.deriv.expression.step.ExpressionWrapper;
import com.deriv.util.Tree;

/**
 * Concrete implementation of an IStepCmd that allows us to
 * keep track of derivative steps.
 */
public class StepCmd implements IStepCmd {
  /**
   * A tree to store steps.
   */
  private Tree<ExpressionWrapper> steps;

  /**
   * Public constructor for a step command.
   * @param tup input
   */
  public StepCmd(ExpressionWrapper tup) {
    this.steps = Tree.makeNode(tup);
  }

  @Override
  public void add(StepCmd cmd) {
    steps.add(cmd.getSteps());
  }

  public Tree<ExpressionWrapper> getSteps() {
    return steps;
  }
}
