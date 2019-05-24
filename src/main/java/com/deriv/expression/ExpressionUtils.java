package com.deriv.expression;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

class ExpressionUtils {
  /**
   * Private constructor for ExpressionUtils, meant to enforce noninstantiability.
   * Inspired by Effective Java 3rd Edition.
   */
  private ExpressionUtils() {
    throw new AssertionError();
  }

  /**
   * Generic, functional way to catch an error and return a value from a Future.
   * @param futureResult input future
   * @param <T> type parameter
   * @return result
   */
  static <T> Optional<T> safeFutureGet(Future<T> futureResult) {
    try {
      return Optional.ofNullable(futureResult.get());
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
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
}