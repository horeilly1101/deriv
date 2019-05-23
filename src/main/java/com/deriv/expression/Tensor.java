package com.deriv.expression;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class Tensor implements Expression {
  private List<Expression> _lines;

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
  public Integer getDepth() {
    return 1 + _lines.get(0).getDepth();
  }

  private Optional<Expression> linearityHelper(Function<Expression, Optional<Expression>> func) {
    // combines terms
    return Optional.of(_lines.parallelStream()
                         .map(func)
                         .filter(Optional::isPresent)
                         .map(Optional::get)
                         .collect(toList()))
             .flatMap(lst -> lst.size() == _lines.size()
                               ? Optional.of(of(lst))
                               : Optional.empty());
  }

  @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    return linearityHelper(x -> x.evaluate(var, input));
  }

  @Override
  public Optional<Expression> differentiate(Variable var) {
    return linearityHelper(x -> x.differentiate(var));
  }

  @Override
  public String toLaTex() {
    return toString(); // TODO
  }
}