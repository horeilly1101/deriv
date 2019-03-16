package com.deriv.expression;

import com.deriv.expression.cmd.CacheCmd;
import com.deriv.expression.cmd.DerivativeCmd;
import com.deriv.util.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
   * The step-expression tuples used to differentiate a given Expression.
   */
  private List<Tuple<Step, Expression>> steps = new ArrayList<>();

  /**
   * Map that keeps track of derivatives that have already been calculated.
   */
  private ConcurrentHashMap<Tuple<Expression, Variable>, Expression> dCache = new ConcurrentHashMap<>();

  /**
   * Enumerations of all currently supported steps.
   */
  public enum Step {
    LINEARITY, LOG_RULE, PRODUCT_RULE, POWER_RULE, SIN, COS, TAN,
    CSC, SEC, COT, VARIABLE_RULE, CONSTANT_RULE
  }

  /**
   * Appends a step to the right of the list of step-expression tuples.
   *
   * @param step the step enum
   * @return the input expression, with its steps list mutated
   */
  public Expression addStep(Step step, Expression ex) {

    steps.add(Tuple.of(step, ex));
    return this;
  }

  @Override
  public Expression differentiate(Variable var) {
    // create the cache
    DerivativeCmd<Tuple<Expression, Variable>, Expression> cacheCmd = new CacheCmd();

    // compute the desired derivative
    Expression derivative = deriveCache(var, cacheCmd);

    // add all of the computed derivatives to the cache
    dCache.putAll(cacheCmd.getStorage());
    return derivative;
  }

  /**
   * Method that computes a derivative, then adds it to the cache.
   *
   * @param var input variable
   * @param cacheCmd our cache command
   * @return resulting Expression
   */
  public Expression deriveCache(Variable var, DerivativeCmd<Tuple<Expression, Variable>, Expression> cacheCmd) {
    // these operations are already constant time, so we won't waste space caching them
    if (this instanceof Variable || this instanceof Constant) {
      return derive(var, cacheCmd);
    }

    // construct tuple
    Tuple<Expression, Variable> key = Tuple.of(this, var);

    // compute and store derivative
    return cacheCmd.computeIfAbsent(key,
                    x -> x.getFirstItem() // compute derivative
                      .derive(x.getSecondItem(), cacheCmd));
  }

  /**
   * Appends a step to the left of the list of step-expression tuples.
   *
   * @param step the step enum
   * @return the input expression, with its steps list mutated
   */
  public Expression addStepLeft(Step step, Expression ex) {
    steps.add(0, Tuple.of(step, ex));
    return this;
  }

  /**
   * Appends a given list of step-expression tuples to the left of
   * the original list of step-expression tuples.
   *
   * @param otherSteps the given list of steps
   * @return the input expression, with its steps list mutated
   */
  public Expression extendSteps(List<Tuple<Step, Expression>> otherSteps) {
    steps.addAll(otherSteps);
    return this;
  }

  @Override
  public ConcurrentHashMap<Tuple<Expression, Variable>, Expression> getCache() {
    return dCache;
  }

  /**
   * Gets the steps list.
   *
   * @return steps list.
   */
  public List<Tuple<Step, Expression>> getSteps() {
    return steps;
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