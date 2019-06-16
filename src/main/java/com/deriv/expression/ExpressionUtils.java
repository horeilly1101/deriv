package com.deriv.expression;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

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
  static <T> Optional<List<T>> linearityHelper(List<? extends T> elements, Function<? super T, Optional<T>> func) {
    List<T> newList = new ArrayList<>();

    for (T element : elements) {
      Optional<? extends T> oMapped = func.apply(element);
      if (!oMapped.isPresent())
        return Optional.empty();
      newList.add(oMapped.get());
    }

    return Optional.of(newList);
  }

  /**
   * Return a shallow copy of a list. This is a simple function, but it allows us to make
   * the process of copying a list more readable.
   *
   * @param lst input list
   * @param <T> type of object in input list
   * @return shallow copy of input list
   */
  static <T> List<T> shallowCopy(List<? extends T> lst) {
    return new ArrayList<>(lst);
  }

  /**
   * Map the items in a collection to a string, and then join them with a delimiter into a single string.
   *
   * @param inputCollection input collection
   * @param mapping a function from the items in the collection to a string
   * @param delimiter the string to separate each item with
   * @param <T> the type of item in the collection
   * @return resulting string
   */
  static <T> String mapAndJoin(Collection<T> inputCollection, Function<T, String> mapping, String delimiter) {
    return inputCollection.stream().map(mapping).collect(joining(delimiter));
  }

  static <T> Optional<T> oGetFuture(Future<Optional<T>> futureOptional) {
    try {
      return futureOptional.get();
    } catch (InterruptedException | ExecutionException e){
      return Optional.empty();
    }
  }
}