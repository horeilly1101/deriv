package com.deriv.expression.cmd;

import com.deriv.expression.step.ExpressionWrapper;
import com.deriv.util.ICmd;
import com.deriv.util.Tree;

/**
 * Command that allows us to keep track of the steps taken to compute a given
 * derivative.
 */
public interface IStepCmd extends ICmd {
  /**
   * Add a step to our storage.
   * @param cmd input step
   */
  void add(StepCmd cmd);

  /**
   * Getter method for the stored steps. Returns a tree, in an effort to maintain
   * semantic clarity.
   * @return steps
   */
  Tree<ExpressionWrapper> getSteps();
}
