package horeilly1101.Expression;

import com.sun.istack.internal.NotNull;

/**
 * An Expression is the all-encompassing data structure that allows
 * us to differentiate arbitrary functions.
 */
interface Expression extends Comparable {
  /**
   * Plugs an input into the value of whatever variable is
   * in the function, and then evaluates the rest of the
   * expression.
   *
   * @return Expression solution
   */
  Expression evaluate(String var, Double input);

  /**
   * Takes the derivative of the given expression.
   *
   * @return Expression derivative
   */
  Expression differentiate(String var);

  /**
   * This method compares an expression with a given object. This
   * is important, as it allows us to define an ordering on our
   * data structures. It also makes equality less strict. (i.e.
   * 2.0 * x should be equal to x * 2.0.)
   */
  default int compareTo(@NotNull Object o) {
    // constants come first
    if (this.getClass().equals(Constant.class)) {
      return -1;
    } else if (o.getClass().equals(Constant.class)) {
      return 1;
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

//  default Expression getExponent() {
//    return Constant.multID();
//  }
//
//  default Expression getBase() {
//    return this;
//  }
}