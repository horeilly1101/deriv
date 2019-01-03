package com.deriv.expression;

import java.util.Optional;

public class Constant implements Expression {
  private Integer val;

  /**
   * Instantiates a new Constant. Avoid using as much as possible! Use the
   * easy constructor below instead.
   *
   * Data definition: A constant is a double.
   */
  private Constant(Integer val) {
    this.val = val;
  }

  /**
   * Use this to create new constants. This method is for constants
   * that are explicitly numbers.
   */
  public static Constant constant(Integer val) {
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

  Integer getVal() {
    return val;
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
  public Boolean isNegative() {
    return this.getVal() < 0;
  }

  static Constant multID() {
    return constant(1);
  }

  static Constant addID() {
    return constant(0);
  }

  public static Expression e() {
    return new Variable("e");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Constant)) {
      return false;
    }

    Constant con = (Constant) o;
    return con.val.equals(this.val);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 11;
  }

  @Override
  public String toString() {
    return val.toString();
  }

  public Optional<Expression> evaluate(String var, Double input) {
    return Optional.of(this);
  }

  public Expression differentiate(String var) {
    return addID();
  }
}