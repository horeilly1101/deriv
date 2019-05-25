package com.deriv.expression;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class Variable implements Expression {
  /**
   * Singleton instance of variable x.
   */
  private static Expression X = new Variable("x");

  /**
   * String that represents the variable. (e.g. x)
   */
  private String _var;

  /**
   * This method is only package-private (because I want to use it
   * to create the constant e in Constant), but you still should
   * avoid using it to instantiate Variable objects. Use the easy
   * constructor below instead.
   *
   * Data definition: a variable is a string name (e.g. "x", "y",
   * etc.).
   */
  Variable(String var) {
    this._var = var;
  }

  /**
   * Use this method to instantiate a Variable object. You can't create
   * a variable named "e" because that's a really important constant in
   * calculus, and we don't want to create any problems.
   */
  public static Expression var(String var) {
    if (var.equals("e")) {
      throw new RuntimeException("Variable can't be named e.");
    }

    return new Variable(var);
  }

  public static Expression x() {
    return X;
  }

  public static Expression x(Integer... nums) {
    return var("x" + Arrays.stream(nums)
                       .map(Objects::toString)
                       .reduce("", (a, b) -> a +  "_" + b));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof Variable))
      return false;


    Variable var = (Variable) o;
    return var._var.equals(this._var);
  }

  @Override
  public int hashCode() {
    return _var.hashCode() + 12;
  }

  @Override
  public String toString() {
    return _var;
  }

  @Override
  public String toLaTex() {
    return _var;
  }

  public Optional<Expression> evaluate(Variable var, Expression input) {
    // update later
    return var.equals(this)
               ? Optional.of(input)
               : Optional.of(this);
  }

  public Optional<Expression> differentiate(Variable wrt) {
    return Optional.of(wrt.equals(this)
                         ? Constant.multID()
                         : Constant.addID());
  }

}