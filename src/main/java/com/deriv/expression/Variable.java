package com.deriv.expression;

import com.deriv.expression.cmd.ICacheCmd;
import com.deriv.expression.cmd.IStepCmd;
import java.util.Optional;

public class Variable extends AExpression {
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
  Variable(String _var) {
    this._var = _var;
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
    return new Variable("x");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Variable)) {
      return false;
    }

    Variable var = (Variable) o;
    return var._var.equals(this._var);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 12;
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

  public Expression computeDerivative(Variable wrt, ICacheCmd cacheCmd, IStepCmd stepCmd) {
    return wrt.equals(this)
             ? Constant.multID()
             : Constant.addID();
  }

  public Step getDerivativeStep() {
    return Step.VARIABLE_RULE;
  }

}