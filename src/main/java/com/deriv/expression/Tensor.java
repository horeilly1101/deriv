package com.deriv.expression;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.deriv.expression.ExpressionUtils.shallowCopy;
import static java.util.stream.Collectors.toList;

/**
 * WARNING: This class is in development and is not fully supported. Use at your
 * own risk.
 *
 * A Tensor represents several expressions, organized together in a mathematical tensor.
 *
 * Data definition: a Tensor is a list of expressions, representing the "rows" or "elements"
 * of the tensor.
 */
class Tensor implements Expression {
  /**
   * A list of expressions, representing the lines in the tensor.
   */
  private final List<Expression> _lines;

  /**
   * Private constructor for a Tensor.
   * @param lines input
   */
  private Tensor(List<Expression> lines) {
    this._lines = lines;
  }

  /**
   * Static method that returns true if the input list can form a valid
   * tensor, false otherwise.
   * @param lines input
   * @return boolean
   */
  private static boolean isValid(List<? extends Expression> lines) {
    // it doesn't make sense to have an empty tensor
    if (lines.size() < 1)
      return false;

    // we need each line in the tensor to have the same depth
    int depth = lines.get(0).getDepth();
    for (Expression line : lines)
      if (line.getDepth() != depth)
        return false;


    return true;
  }

  /**
   * Static constructor for a tensor. Each expression in lines must have
   * the same depth, otherwise an excpetion will be thrown at runtime.
   * @param lines input
   * @return tensor
   */
  public static Expression of(Expression... lines) {
    return of(Arrays.asList(lines));
  }

  /**
   * Static constructor for a tensor. Each expression in lines must have
   * the same depth, otherwise an excpetion will be thrown at runtime.
   * @param lines input
   * @return tensor
   */
  public static Expression of(List<? extends Expression> lines) {
    if(!isValid(lines))
      throw new RuntimeException("Each Expression must have the same depth!");

    return new Tensor(shallowCopy(lines));
  }

  /**
   * Method to get the ith "row" of the tensor.
   * @param idx index
   * @return expression
   */
  public Expression get(int idx) {
    return _lines.get(idx);
  }

  /**
   * Method to get the ith "row" of the tensor. Casts the result to a Tensor.
   * @param idx index
   * @return expression
   */
  public Tensor getTensor(int idx) {
    return get(idx).asTensor();
  }

  /**
   * Getter method for _lines.
   * @return _lines
   */
  List<Expression> getLines() {
    return shallowCopy(_lines); // copy the list, just in case
  }

  @Override
  public String toString() {
    return _lines.stream()
             .map(Object::toString) // call toString on each line
             .collect(toList())
             .toString(); // convert result to string
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

  /**
   * Method that differentiates an expression with respect to a tensor.
   * @param expr being differentiated
   * @return optional of resulting  derivative
   */
  public Optional<Expression> differentiating(Expression expr) {
    return ExpressionUtils.linearityHelper(_lines, expr::differentiate).map(Tensor::of);
  }


  @Override
  public Set<Variable> getVariables() {
    return _lines.stream().flatMap(x -> x.getVariables().stream()).collect(Collectors.toSet());
  }

  @Override
  public String toLaTex() {
    return toString(); // TODO
  }
}