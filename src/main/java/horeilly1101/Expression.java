package horeilly1101;

import com.sun.istack.internal.NotNull;

import java.util.*;
import java.util.function.*;

import static java.util.stream.Collectors.toList;
import static horeilly1101.SimplifyExpression.*;

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

  default Mult asMult() {
    return (Mult) this;
  }

  default Add asAdd() {
    return (Add) this;
  }

  default Power asPower() {
    return (Power) this;
  }

  default Constant asConstant() {
    return (Constant) this;
  }

  default Expression getExponent() {
    return Constant.multID();
  }

  default Expression getBase() {
    return this;
  }
}