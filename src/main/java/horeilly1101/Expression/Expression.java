package horeilly1101.Expression;

import static horeilly1101.Expression.Constant.*;

/**
 * An Expression is the all-encompassing data structure that allows
 * us to differentiate arbitrary functions.
 *
 * This interface is implemented by:
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
   * @return Expression solution
   */
  // future: should be able to evaluate with an Expression
  // e.g. evaluate at e
  Expression evaluate(String var, Double input);

  /**
   * Takes the derivative of the given expression.
   *
   * @param var -- a string variable name
   * @return Expression derivative
   */
  Expression differentiate(String var);

  /**
   * This method compares an expression with a given object. This
   * is important, as it allows us to define an ordering on our
   * data structures. It also makes equality less strict. (i.e.
   * 2.0 * x should be equal to x * 2.0.)
   */
  default int compareTo(Object o) {
    // constants shouldcome first

    // polynomials go in decreasing order of exponent
    if (this.isPower() || o.getClass().equals(Power.class)) {
      return this.getExponent().compareTo(((Expression) o).getExponent());
    }

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

  default Constant getConstantTerm() {
    return Constant.addID();
  }

  default Expression getSymbolicTerms() {
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