package com.deriv.expression;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * A Tensor represents several expressions, organized together in a mathematical tensor.
 *
 * Data definition: a Tensor is a list of expressions, representing the "rows" or "elements"
 * of the tensor.
 */
class Tensor implements Expression {
  /**
   * A list of expressions, representing the lines in the tensor.
   */
  private List<Expression> _lines;

  /**
   * Private constructor for a Tensor.
   * @param lines input
   */
  private Tensor(List<Expression> lines) {
    this._lines = lines;
  }

  private static boolean isValid(List<Expression> lines) {
    return lines.stream().map(Expression::getDepth)
             .reduce((a, b) -> a.equals(b) ? a : -1)
             .map(x -> !x.equals(-1))
             .orElse(false)
             && lines.size() != 0;
  }

  public static Expression of(Expression... lines) {
    return of(Arrays.asList(lines));
  }

  public static Expression of(List<Expression> lines) {
    if(!isValid(lines)) {
      throw new RuntimeException("Each Expression must have the same depth!");
    }

    return new Tensor(lines);
  }

  @Override
  public String toString() {
    return _lines.stream().map(Object::toString).collect(toList()).toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof Tensor))
      return false;

    Tensor tensor = (Tensor) o;
    return _lines.equals(tensor._lines);
  }

  @Override
  public int hashCode() {
    return _lines.hashCode();
  }

  @Override
  public int getDepth() {
    return 1 + _lines.get(0).getDepth();
  }

  @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    return ExpressionUtils.linearityHelper(_lines, x -> x.evaluate(var, input)).map(Tensor::of);
  }

  @Override
  public Optional<Expression> differentiate(Variable var) {
    return ExpressionUtils.linearityHelper(_lines, x -> x.differentiate(var)).map(Tensor::of);
  }

  @Override
  public String toLaTex() {
    return toString(); // TODO
  }
}