package com.deriv.expression;

import com.deriv.expression.cmd.ICacheCmd;
import com.deriv.expression.cmd.IStepCmd;
import com.deriv.expression.step.ExpressionWrapper;
import com.deriv.util.Tree;
import com.deriv.util.Tuple2;

import java.util.Optional;

/**
 * Empty for now.
 */
public class Tensor implements Expression {
  @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    return Optional.empty();
  }

  @Override
  public Expression differentiate(Variable var) {
    return null;
  }

  @Override
  public Expression differentiate(Variable var, ICacheCmd cacheCmd) {
    return null;
  }

  @Override
  public Tuple2<Expression, Tree<ExpressionWrapper>> differentiateWithSteps(Variable var) {
    return null;
  }

  @Override
  public Expression differentiate(Variable var, ICacheCmd cacheCmd, IStepCmd stepCmd) {
    return null;
  }

  @Override
  public String toLaTex() {
    return null;
  }
}
