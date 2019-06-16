package com.deriv.expression;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A Constant is a scalar constant. This class provides several static constructors to create common constants.
 * I recommend you use these instead of creating the constants yourself.
 *
 * Data definition: A constant is an int. Want to create a number that isn't rational? Give it a name and
 * make it a variable.
 */
public class Constant implements Expression {
  /**
   * Singleton instance of constant 0.
   */
  private static final Constant ADD_ID = new Constant(0);

  /**
   * Singleton instance of constant 1.
   */
  private static final Constant MULT_ID = new Constant(1);

  /**
   * Singleton instance of constant e.
   */
  private static final Variable E = new Variable("e");

  /**
   * Singleton instance of constant pi.
   */
  private static final Variable PI = new Variable("π");

  /**
   * The integer value of a constant.
   */
  private final int _val;

  /**
   * Private constructor for a constant. Use one of the static constructors instead.
   */
  private Constant(int val) {
    this._val = val;
  }

  /**
   * Static constructor for a constant that is explicitly an int.
   * @param val num
   * @return constant
   */
  public static Expression constant(int val) {
    return new Constant(val);
  }

  /**
   * Static constructor for an arbitrary constant, that is explicitly a String.
   * @param val name
   * @return constant
   */
  static Expression constant(String val) {
    // arbitrary constants are effectively variables
    return new Variable(val);
  }

  /**
   * Static constructor for the multiplicative id.
   * @return 1
   */
  public static Expression multID() {
    return MULT_ID;
  }

  /**
   * Static constructor for the additive id.
   * @return 0
   */
  public static Expression addID() {
    return ADD_ID;
  }

  /**
   * Static constructor for the number e.
   * @return e
   */
  public static Expression e() {
    return E;
  }

  /**
   * Static constructor for the number pi.
   * @return π
   */
  public static Expression pi() {
    return PI;
  }

  /**
   * Getter method for the value of a Constant.
   * @return val
   */
  public int getVal() {
    return _val;
  }

  @Override
  public Expression getConstantFactor() {
    return this;
  }

  @Override
  public Expression getSymbolicFactors() {
    return multID();
  }

  @Override
  public boolean isNegative() {
    return this.getVal() < 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof Constant))
      return false;

    Constant con = (Constant) o;
    return _val == con._val;
  }

  @Override
  public int hashCode() {
    return 31 * Integer.hashCode(_val) + 11;
  }

  @Override
  public String toString() {
    return Integer.toString(_val);
  }

  @Override
  public String toLaTex() {
    return toString();
  }

  @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    return Optional.of(this);
  }

  @Override
  public Optional<Expression> differentiate(Variable var) {
    return Optional.of(addID());
  }

  @Override
  public Set<Variable> getVariables() {
    return new HashSet<>();
  }
}