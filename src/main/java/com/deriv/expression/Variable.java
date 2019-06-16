package com.deriv.expression;

import java.util.Optional;
import java.util.Set;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * A variable is a scalar variable.
 *
 * Data definition: a variable is a string name (e.g. "x", "y", etc.).
 */
public class Variable implements Expression {
  /**
   * Singleton instance of variable x.
   */
  private static final Variable X = new Variable("x");

  /**
   * String that represents the variable. (e.g. x)
   */
  private final String _var;

  /**
   * Package-private constructor for a variable. Use one of the static
   * constructors instead.
   */
  Variable(String var) {
    this._var = var;
  }

  /**
   * Static constructor for a Variable. If you pass "e" or "π" as arguments to
   * this function, it will throw an error, as those are reserved variable names.
   * @param var variable name
   * @return variable
   */
  public static Expression var(String var) {
    if (var.equals("e") || var.equals("π"))
      throw new RuntimeException("Variable can't be named e or π.");

    return new Variable(var);
  }

  /**
   * Static constructor for a variable with name "x".
   * @return variable
   */
  public static Variable x() {
    return X;
  }

  /**
   * Static constructor for a variable with a name of the form "x_i1_..._in".
   * @param nums indices
   * @return variable
   */
  public static Expression x(Integer... nums) {
    String varSubscript = Arrays.stream(nums).map(Objects::toString)
                            .reduce("", (a, b) -> a + "_" + b);
    return var("x" + varSubscript);
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
    return toString();
  }

  @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    return Optional.of(var.equals(this) ? input : this);
  }

  @Override
  public Optional<Expression> differentiate(Variable wrt) {
    return Optional.of(wrt.equals(this) ? Constant.multID() : Constant.addID());
  }

  @Override
  public Set<Variable> getVariables() {
    return Stream.of(this).collect(toSet());
  }
}