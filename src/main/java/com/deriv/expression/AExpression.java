package com.deriv.expression;

import com.deriv.expression.cmd.*;
import com.deriv.util.Tuple;

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
  /**
   * Enumerations of all currently supported steps.
   */
  public enum Step {
    LINEARITY, LOG_RULE, PRODUCT_RULE, POWER_RULE, SIN, COS, TAN,
    CSC, SEC, COT, VARIABLE_RULE, CONSTANT_RULE
  }

  @Override
  public Expression differentiate(Variable var) {
    return differentiate(var, new NullCacheCmd(), new NullStepCmd());
  }

  /**
   * Method that computes a derivative, then adds it to the cache.
   *
   * @param var input variable
   * @param cacheCmd our cache command
   * @return resulting Expression
   */
  public Expression differentiate(Variable var, ICacheCmd cacheCmd, IStepCmd stepCmd) {
    // these operations are already constant time, so we won't waste space caching them
    if (this instanceof Variable || this instanceof Constant) {
      return computeDerivative(var, cacheCmd, stepCmd);
    }

    // add step
    stepCmd.addStep(getDerivativeStep(), this);

    // construct tuple
    Tuple<Expression, Variable> key = Tuple.of(this, var);

    // compute and store derivative
    return cacheCmd.computeIfAbsent(key,
                    x -> x.getFirstItem() // compute derivative
                      .computeDerivative(x.getSecondItem(), cacheCmd, stepCmd));
  }

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