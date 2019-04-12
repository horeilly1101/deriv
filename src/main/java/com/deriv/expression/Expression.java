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
  Tree<ExpressionWrapper> differentiateWithSteps(Variable var);

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