package com.deriv.expression;

import com.deriv.expression.cmd.*;
import com.deriv.expression.step.ExpressionWrapper;
import com.deriv.expression.step.Step;
import com.deriv.util.Tree;
import com.deriv.util.Tuple2;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.addID;
import static com.deriv.expression.Mult.mult;

/**
 * Abstract class that implements Expression and is extended by:
 * - Mult
 * - Add
 * - Log
 * - Power
 * - Trig
 * - Constant
 * - Variable
 *
 * This class exists to let us maintain a list of the operations used to
 * differentiate expressions.
 */
public abstract class AExpression implements Expression {

  @Override
  public Expression differentiate(Variable var) {
    return differentiate(var, new NullCacheCmd(), new NullStepCmd());
  }

  @Override
  public Expression differentiate(Variable var, ICacheCmd cacheCmd) {
    return differentiate(var, cacheCmd, new NullStepCmd());
  }

  @Override
  public Expression differentiate(Variable var, ICacheCmd cacheCmd, IStepCmd stepCmd) {
    // create a new step
    StepCmd newCmd = new StepCmd(ExpressionWrapper.of(this, this.getDerivativeStep()));

    // construct tuple
    Tuple2<Expression, Variable> key = Tuple2.of(this, var);

    // compute and store derivative
    Expression result = cacheCmd.computeIfAbsent(key,
      x -> x.getItem1()
             .asAExpression()
             .computeDerivative(x.getItem2(), cacheCmd, newCmd)); // compute derivative

    //  add all subsequent steps
    stepCmd.add(newCmd);

    return result;
  }

  @Override
  public Tuple2<Expression, Tree<ExpressionWrapper>> differentiateWithSteps(Variable var) {
    // create a new step command
    StepCmd stepCmd = new StepCmd(ExpressionWrapper.of(this, this.getDerivativeStep()));

    // differentate the expression and store steps
    Expression result = this.computeDerivative(var, new NullCacheCmd(), stepCmd);

    // return a tree of expression wrappers
    return Tuple2.of(result, stepCmd.getSteps());
  }

  /**
   * Take the derivative of a function. This should only be called by "differentiate". And every attempt
   * to differentiate a function in an implementation of this method should use "differentiate".
   *
   * @param var input variable
   * @param cacheCmd our cache command
   * @param stepCmd our step command
   * @return differentiated expression
   */
  abstract Expression computeDerivative(Variable var, ICacheCmd cacheCmd, IStepCmd stepCmd);

  /**
   * Get the step needed to differentiate a given Expression implementation. (e.g. a polynomial would return
   * Step.POWER_RULE.)
   *
   * @return desired step.
   */
  abstract Step getDerivativeStep();

  @Override
  public Expression times(Expression input) {
    return mult(this, input);
  }

  @Override
  public Expression plus(Expression input) {
    return add(this, input);
  }

  @Override
  public Expression getAddID() {
    return addID();
  }
}