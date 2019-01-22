package com.deriv.expression;

import java.util.List;
import java.util.Optional;

import static com.deriv.expression.Constant.*;
import static com.deriv.expression.AExpression.*;

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
public interface Expression extends Comparable {
  /**
   * Plugs an input into the value of whatever variable is
   * in the function, and then evaluates the rest of the
   * expression.
   *
   * @param var -- a string variable name
   * @param input -- the number to be plugged into var
   * @return Optional<Expression> solution
   */
  Optional<Expression> evaluate(String var, Expression input);
  // future: should be able to evaluate at any expression value, not string

  /**
   * Takes the derivative of the given expression.
   *
   * @param var -- a string variable name
   * @return Expression derivative
   */
  Expression differentiate(String var);

  /**
   * @return the steps taken to differentiate the given expression.
   */
  List<Tuple> getSteps();

  /**
   * Adds the given Step and Expression to the Expression's step list.
   */
  Expression addStep(Step step, Expression expression);

  /**
   * Adds the given Step and Expression to the left side of the Expression's step list.
   */
  Expression addStepLeft(Step step, Expression expression);

  /**
   * Adds the given list of Tuples to the Expression's step list.
   */
  Expression extendSteps(List<Tuple> otherSteps);

  /**
   * This method compares an expression with a given object. This
   * is important, as it allows us to define an ordering on our
   * data structures. It also makes equality less strict. (i.e.
   * 2.0 * x should be equal to x * 2.0.)
   */
  default int compareTo(Object o) {
    // constants should come first
    return this.toString().compareTo(o.toString());
  }

  /*
  Type check functions
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
  Type cast functions
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
  Each Expression is implicitly a Mult, a Div, an Add, and a Power,
  so the following getter functions are justified.

  Of course, each of these methods is overridden in the necessary
  implementations.
   */

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