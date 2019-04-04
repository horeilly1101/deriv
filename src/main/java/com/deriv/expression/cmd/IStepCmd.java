package com.deriv.expression.cmd;

import com.deriv.expression.AExpression.*;
import com.deriv.expression.Expression;
import com.deriv.util.ICmd;
import com.deriv.util.Tuple;

import java.util.List;

public interface IStepCmd extends ICmd {
  void addStep(Step step, Expression expression);

  List<Tuple<Step, Expression>> getSteps();
}
