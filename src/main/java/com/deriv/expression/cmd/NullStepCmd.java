package com.deriv.expression.cmd;

import com.deriv.expression.AExpression;
import com.deriv.expression.Expression;
import com.deriv.util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class NullStepCmd implements IStepCmd {
  public NullStepCmd() {
    // no op
  }

  public void addStep(AExpression.Step step, Expression expression) {
    // no op
  }

  public List<Tuple<AExpression.Step, Expression>> getSteps() {
    return new ArrayList<>();
  }
}
