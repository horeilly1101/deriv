package com.deriv.expression;

import java.util.Optional;

/**
 * Empty for now.
 */
public class Tensor implements Expression {
  @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    return Optional.empty();
  }

  @Override
  public Optional<Expression> differentiate(Variable var) {
    return null;
  }

  @Override
  public String toLaTex() {
    return null;
  }
}
