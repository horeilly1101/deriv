package com.deriv.expression;

import com.deriv.expression.cmd.ICacheCmd;
import com.deriv.expression.cmd.IStepCmd;
import com.deriv.expression.step.ExpressionWrapper;
import com.deriv.util.*;
import java.util.Optional;

import static com.deriv.expression.Constant.*;

/**
 * An Expression is the all-encompassing data structure that allows
 * us to differentiate arbitrary functions.
 */
public interface Expression extends Comparable<Expression> {
  /**
   * Plugs an input into the value of whatever variable is
   * in the function, and then evaluates the rest of the
   * expression.
   *
   * @param var -- a string variable name
   * @param input -- the number to be plugged into _var
   * @return Optional<Expression> solution
   */
  Optional<Expression> evaluate(Variable var, Expression input);

  /**
   * Takes the derivative of the given expression. This essentially functions as a template method
   * to ensure that we can cache computed derivatives, save the steps taken, and perform any other
   * necessary computations at each call.
   *
   * @param var -- a string variable name
   * @return Expression derivative
   */
  Expression differentiate(Variable var);

  /**
   * Takes the derivative of a function and store the result in a cache. If the derivative
   * has already been computed, this function returns the result in constant time.
   *
   * @param var -- a string variable name
   * @param cacheCmd -- our cache command
   * @return Expression derivative
   */
  Expression differentiate(Variable var, ICacheCmd cacheCmd);

  /**
   * Takes the derivative of a function and returns the resulting expression and the steps taken
   * to derive it.
   *
   * @param var input variable
   * @return resulting expression wrapper
   */
  Tuple2<Expression, Tree<ExpressionWrapper>> differentiateWithSteps(Variable var);

  /**
   * Takes the derivative of a function and store the result in a cache. If the derivative
   * has already been computed, this function returns the result in constant time.
   *
   * @param var input variable
   * @param cacheCmd our cache command
   * @param stepCmd our step command
   * @return differentiated expression
   */
  Expression differentiate(Variable var, ICacheCmd cacheCmd, IStepCmd stepCmd);

  /**
   * Returns a latex representation of an expression.
   *
   * @return latex representation.
   */
  String toLaTex();

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
    // TODO
    // constants should come first
    return this.toString().compareTo(ex.toString());
  }

  /*
   * Below is a list of type check functions. These allows us to
   * concisely check whether or not a given Expression is an instance
   * of a certain subclass.
   */

  /**
   * Checks whether or not a given expression is an instance of Mult.
   *
   * @return boolean
   */
  default Boolean isMult() {
    return this.getClass().equals(Mult.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Add.
   *
   * @return boolean
   */
  default Boolean isAdd() {
    return this.getClass().equals(Add.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Log.
   *
   * @return boolean
   */
  default Boolean isLog() {
    return this.getClass().equals(Log.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Power.
   *
   * @return boolean
   */
  default Boolean isPower() {
    return this.getClass().equals(Power.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Trig.
   *
   * @return boolean
   */
  default Boolean isTrig() {
    return this.getClass().equals(Trig.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Constant.
   *
   * @return boolean
   */
  default Boolean isConstant() {
    return this.getClass().equals(Constant.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Variable.
   *
   * @return boolean
   */
  default Boolean isVariable() {
    return this.getClass().equals(Variable.class);
  }

  /*
   * Below is a list of ype cast functions. These allow us to concisely
   * cast given Expression objects to certain subclasses.
   */

  /**
   * Cast a given Expression to a Mult.
   * @return Mult
   */
  default Mult asMult() {
    return (Mult) this;
  }

  /**
   * Cast a given Expression to a Add.
   * @return Add
   */
  default Add asAdd() {
    return (Add) this;
  }

  /**
   * Cast a given Expression to a Log.
   * @return Log
   */
  default Log asLog() {
    return (Log) this;
  }

  /**
   * Cast a given Expression to a Power.
   * @return Power
   */
  default Power asPower() {
    return (Power) this;
  }

  /**
   * Cast a given Expression to a Trig.
   * @return Trig
   */
  default Trig asTrig() {
    return (Trig) this;
  }

  /**
   * Cast a given Expression to a Constant.
   * @return Constant
   */
  default Constant asConstant() {
    return (Constant) this;
  }

  /**
   * Cast a given Expression to a Variable.
   * @return Variable
   */
  default Variable asVariable() {
    return (Variable) this;
  }

  /**
   * Cast a given Expression to a AExpression.
   * @return AExpresssion
   */
  default AExpression asAExpression() {
    return (AExpression) this;
  }

  /*
   * Each Expression is implicitly a Mult, a Div, an Add, and a Power,
   * so the following getter functions are justified.
   *
   * Of course, each of these methods is overridden in the necessary
   * implementations.
   */

  /**
   * Get the exponent of a given expression.
   * @return exponent
   */
  default Expression getExponent() {
    return multID();
  }

  /**
   * Get the base of a given expression.
   * @return base
   */
  default Expression getBase() {
    return this;
  }

  /**
   * Get the constant factor of a given expression.
   * @return constant factor
   */
  default Expression getConstantFactor() {
    return multID();
  }

  /**
   * Get the symbolic factors of a given expression.
   * @return symbolic factors
   */
  default Expression getSymbolicFactors() {
    return this;
  }

  /**
   * Get the numerator of a given expression.
   * @return numerator
   */
  default Expression getNumerator() {
    return this;
  }

  /**
   * Get the denominator of a given expression.
   * @return denominator
   */
  default Expression getDenominator() {
    return multID();
  }

  /**
   * Checks whether or not a given expression is multiplied by -1.
   * @return boolean
   */
  default Boolean isNegative() {
    return false;
  }
}