package com.deriv.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.joining;

/**
 * Package-private utility class that allows us to reuse static functions among
 * different implementations of Expression.
 */
class ExpressionUtils {
  /**
   * Private constructor for ExpressionUtils, meant to enforce noninstantiability.
   */
  private ExpressionUtils() {
    throw new AssertionError();
  }

  /**
   * Helper methods that takes advantage of the linearity of add operations.
   * @param func linear function
   * @return aggregated result
   */
  static Optional<List<Expression>> linearityHelper(
    List<Expression> elements, Function<Expression, Optional<Expression>> func) {
    // combines terms
    return Optional.of(elements.parallelStream()
                         .map(func)
                         .filter(Optional::isPresent)
                         .map(Optional::get)
                         .collect(toList()))
             .flatMap(lst -> lst.size() == elements.size()
                               ? Optional.of(lst)
                               : Optional.empty());
  }

  /**
   * Return a shallow copy of a list. This is a simple function, but it allows us to make
   * the process of copying a list more readable.
   * @param lst input list
   * @param <T> type of object in input list
   * @return shallow copy of input list
   */
  static <T> List<T> shallowCopy(List<T> lst) {
    return new ArrayList<>(lst);
  }

  static <T> String mapAndJoin(List<T> inputList, Function<T, String> mapping, String delimiter) {
    return inputList.stream().map(mapping).collect(joining(delimiter));
  }
}