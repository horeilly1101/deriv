package com.deriv.expression;

import java.util.Optional;
import java.util.Set;

import static com.deriv.expression.Constant.addID;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Add.add;
import static com.deriv.expression.Power.inverse;

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
   * @param var a string variable name
   * @param input the number to be plugged into _var
   * @return Optional<Expression> solution
   */
  Optional<Expression> evaluate(Variable var, Expression input);

  /**
   * Takes the derivative of the given expression. This essentially functions as a template method
   * to ensure that we can cache computed derivatives, save the steps taken, and perform any other
   * necessary computations at each call.
   *
   * @param var a string variable name
   * @return Expression derivative
   */
  Optional<Expression> differentiate(Variable var);

  /**
   * Method that returns a set of all the variables contained in the expression.
   * @return a set of variables contained in the expression
   */
  Set<Variable> getVariables();

  /**
   * Returns a latex representation of an expression.
   *
   * @return latex representation.
   */
  String toLaTex();

  /**
   * Skeleton, template method for differentiating an expression by another expression. This is possible through
   * repeated application of the chain rule from multivariate calculus.
   * @param wrt expression to differentiated with respect to.
   * @return optional of returned derivative
   */
  default Optional<Expression> differentiate(Expression wrt) {
    if (wrt.isTensor())
      return wrt.asTensor().differentiating(this); // differentiating by a tensor is handled separately

    Expression result = addID();

    for (Variable var : wrt.getVariables()) {
      Optional<Expression> firstDerivative = differentiate(var);
      Optional<Expression> secondDerivative = wrt.differentiate(var);

      // fail if either derivative isn't present
      if (!firstDerivative.isPresent() || !secondDerivative.isPresent())
        return Optional.empty();

      result = add(result, mult(firstDerivative.get(), inverse(secondDerivative.get())));
    }

    return Optional.of(result);

  }

  /**
   * This method compares an expression with a given object. This
   * is important, as it allows us to define an ordering on our
   * data structures. It also makes equality less strict. (i.e.
   * 2.0 * x should be equal to x * 2.0.)
   *
   * @param ex the expression to be compared
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
  default boolean isMult() {
    return this.getClass().equals(Mult.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Add.
   *
   * @return boolean
   */
  default boolean isAdd() {
    return this.getClass().equals(Add.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Log.
   *
   * @return boolean
   */
  default boolean isLog() {
    return this.getClass().equals(Log.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Power.
   *
   * @return boolean
   */
  default boolean isPower() {
    return this.getClass().equals(Power.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Trig.
   *
   * @return boolean
   */
  default boolean isTrig() {
    return this.getClass().equals(Trig.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Constant.
   *
   * @return boolean
   */
  default boolean isConstant() {
    return this.getClass().equals(Constant.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Variable.
   *
   * @return boolean
   */
  default boolean isVariable() {
    return this.getClass().equals(Variable.class);
  }

  /**
   * Checks whether or not a given expression is an instance of Tensor.
   *
   * @return boolean
   */
  default boolean isTensor() {
    return this.getClass().equals(Tensor.class);
  }

  /*
   * Below is a list of type cast functions. These allow us to concisely
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
   * Cast a given Expression to a Tensor.
   * @return Tensor
   */
  default Tensor asTensor() {
    return (Tensor) this;
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
  default boolean isNegative() {
    return false;
  }

  /**
   * Getter method for the depth of an expression. The depth of an expression
   * is how many expressions are composed in tensors within it.
   * @return depth
   */
  default int getDepth() {
    return 0;
  }
}