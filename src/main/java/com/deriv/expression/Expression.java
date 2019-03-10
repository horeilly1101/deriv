package com.deriv.expression;

import com.deriv.expression.cmd.DerivativeCmd;
import com.deriv.util.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.deriv.expression.AExpression.*;

import static com.deriv.expression.Constant.*;

/**
 * An Expression is the all-encompassing data structure that allows
 * us to differentiate arbitrary functions.
 *
 * This interface is implemented by AExpression which is extended by:
 * - Mult
 * - Add
 * - Log
 * - Power
 * - Trig
 * - Constant
 * - Variable
 *
 * Questions about how it all works? Email horeilly1101@gmail.com
 */
public interface Expression extends Comparable<Expression>, Composable<Expression> {
  /**
   * Plugs an input into the value of whatever variable is
   * in the function, and then evaluates the rest of the
   * expression.
   *
   * @param var -- a string variable name
   * @param input -- the number to be plugged into var
   * @return Optional<Expression> solution
   */
  Optional<Expression> evaluate(Variable var, Expression input);

  /**
   * Takes the derivative of the given expression.
   *
   * @param var -- a string variable name
   * @return Expression derivative
   */
  Expression differentiate(Variable var);

  /**
   * Takes the derivative of a function and store the result in a cache. If the derivative
   * has already been computed, this function returns the result in constant time.
   *
   * @param var input variable
   * @param cacheCmd our cache command
   * @return differentiated expression
   */
  Expression deriveCache(Variable var, DerivativeCmd<Tuple<Expression, Variable>, Expression> cacheCmd);

  /**
   * Take the derivative of a function. This should only be called by deriveCache.
   *
   * @param var input variable
   * @param cache our cache command
   * @return differentiated expression
   */
  Expression derive(Variable var, DerivativeCmd<Tuple<Expression, Variable>, Expression> cache);

  /**
   * Gets the cache computed from the given expression.
   *
   * @return concurrent hashmap cache.
   */
  ConcurrentHashMap<Tuple<Expression, Variable>, Expression> getCache();

  /**
   * Gets the steps taken to differentiate the given expression.
   *
   * @return the steps taken to differentiate the given expression.
   */
  List<Tuple<Step, Expression>> getSteps();

  /**
   * Adds the given Step and Expression to the Expression's step list.
   *
   * @return the resulting expression
   */
  Expression addStep(Step step, Expression expression);

  /**
   * Adds the given Step and Expression to the left side of the Expression's step list.
   *
   * @param step -- the step to be added
   * @param expression -- the expression the step was used on
   * @return the resulting expression
   */
  Expression addStepLeft(Step step, Expression expression);

  /**
   * Adds the given list of Tuples to the Expression's step list.
   *
   * @param otherSteps -- the list of steps to be extened on the existing steps
   * @return the resulting expression
   */
  Expression extendSteps(List<Tuple<Step, Expression>> otherSteps);

  /**
   * This method compares an expression with a given object. This
   * is important, as it allows us to define an ordering on our
   * data structures. It also makes equality less strict. (i.e.
   * 2.0 * x should be equal to x * 2.0.)
   *
   * @param ex -- the expression to be compared
   * @return an integer value relating the expressions
   */
  default int compareTo(Expression ex) {
    // constants should come first
    return this.toString().compareTo(ex.toString());
  }

  /*
   * Type check functions
   */

  default Boolean isMult() {
    return this.getClass().equals(Mult.class);
  }

  default Boolean isAdd() {
    return this.getClass().equals(Add.class);
  }

  default Boolean isLog() {
    return this.getClass().equals(Log.class);
  }

  default Boolean isPower() {
    return this.getClass().equals(Power.class);
  }

  default Boolean isTrig() {
    return this.getClass().equals(Trig.class);
  }

  default Boolean isConstant() {
    return this.getClass().equals(Constant.class);
  }

  default Boolean isVariable() {
    return this.getClass().equals(Variable.class);
  }

  /*
   * Type cast functions
   */

  default Mult asMult() {
    return (Mult) this;
  }

  default Add asAdd() {
    return (Add) this;
  }

  default Log asLog() {
    return (Log) this;
  }

  default Power asPower() {
    return (Power) this;
  }

  default Trig asTrig() {
    return (Trig) this;
  }

  default Constant asConstant() {
    return (Constant) this;
  }

  default Variable asVariable() {
    return (Variable) this;
  }

  /*
   * Each Expression is implicitly a Mult, a Div, an Add, and a Power,
   * so the following getter functions are justified.
   *
   * Of course, each of these methods is overridden in the necessary
   * implementations.
   */

  // TODO make abstract!
  default Expression getExponent() {
    return multID();
  }

  default Expression getBase() {
    return this;
  }

  default Expression getConstantFactor() {
    return multID();
  }

  default Expression getSymbolicFactors() {
    return this;
  }

  default Expression getNumerator() {
    return this;
  }

  default Expression getDenominator() {
    return multID();
  }

  default Boolean isNegative() {
    return false;
  }
}