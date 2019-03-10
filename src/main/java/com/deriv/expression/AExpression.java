package com.deriv.expression;

import com.deriv.expression.cmd.CacheCmd;
import com.deriv.expression.cmd.DerivativeCmd;
import com.deriv.util.Tuple;
import java.util.ArrayList;
import java.util.List;

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
    DerivativeCmd<Tuple<Expression, Variable>, Expression> cacheCmd = new CacheCmd();
    return derive(var, cacheCmd);
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