package com.deriv.expression;

import java.util.Optional;

/**
 * A Constant is a scalar constant.
 *
 * Data definition: A constant is a integer.
 */
public class Constant implements Expression {
  /**
   * Singleton instance of constant 0.
   */
  private static Expression ADD_ID = constant(0);

  /**
   * Singleton instance of constant 1.
   */
  private static Expression MULT_ID = constant(1);

  /**
   * Singleton instance of constant e.
   */
  private static Expression E = new Variable("e");

  /**
   * The integer value of a constant.
   */
  private int _val;

  /**
   * Instantiates a new Constant. Avoid using as much as possible! Use the
   * easy constructor below instead.
   */
  private Constant(int val) {
    this._val = val;
  }

  /**
   * Use this to create new constants. This method is for constants
   * that are explicitly numbers.
   */
  public static Expression constant(Integer val) {
    return new Constant(val);
  }

  /**
   * Use this to create new constants. This allows you to create
   * arbitrary constants.
   */
  static Expression constant(String val) {
    // yeah, yeah I know it says variable
    return new Variable(val);
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
}