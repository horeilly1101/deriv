package com.deriv.expression.cmd;

import com.deriv.expression.step.ExpressionWrapper;
import com.deriv.util.ICmd;
import com.deriv.util.Tree;

public interface IStepCmd extends ICmd {
  void add(StepCmd cmd);

  Tree<ExpressionWrapper> getSteps();
}
