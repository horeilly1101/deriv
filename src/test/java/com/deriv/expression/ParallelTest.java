package com.deriv.expression;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Power.poly;
import static com.deriv.expression.Variable.x;
import static java.util.stream.Collectors.toList;

class ParallelTest {
  /**
   * Helper methods that takes advantage of the linearity of add operations.
   * @param func linear function
   * @return aggregated result
   */
  private static Optional<List<Expression>> sequentialLinearityHelper(
    List<Expression> elements, Function<Expression, Optional<Expression>> func) {
    // combines terms
    return Optional.of(elements.stream()
                         .map(func)
                         .filter(Optional::isPresent)
                         .map(Optional::get)
                         .collect(toList()))
             .flatMap(lst -> lst.size() == elements.size()
                               ? Optional.of(lst)
                               : Optional.empty());
  }

  @Test
  void addEvaluateTest() {
    List<Expression> polySum = Stream.iterate(0, i -> i + 1)
                              .map(num -> poly(x(), num))
                              .limit(100000)
                              .collect(Collectors.toList());

    Expression result = add(polySum);

    long startParallel = System.nanoTime();
    ExpressionUtils.linearityHelper(result.asAdd().getTerms(), x -> x.evaluate(x().asVariable(), multID()));
    long endParallel = System.nanoTime();

    long startSequential = System.nanoTime();
    sequentialLinearityHelper(result.asAdd().getTerms(), x -> x.evaluate(x().asVariable(), multID()));
    long endSequential = System.nanoTime();

    System.out.println("-- addEvaluateTest --");
    System.out.println("parallel time:   " + (endParallel - startParallel) + " ms");
    System.out.println("sequential time: " + (endSequential - startSequential) + " ms");
    System.out.println();
  }

  @Test
  void addDerivativeTest() {
    List<Expression> polySum = Stream.iterate(0, i -> i + 1)
                                 .map(num -> poly(x(), num))
                                 .limit(10000)
                                 .collect(Collectors.toList());

    Expression result = add(polySum);

    long startParallel = System.nanoTime();
    ExpressionUtils.linearityHelper(result.asAdd().getTerms(), x -> x.differentiate(x().asVariable()));
    long endParallel = System.nanoTime();

    long startSequential = System.nanoTime();
    sequentialLinearityHelper(result.asAdd().getTerms(), x -> x.differentiate(x().asVariable()));
    long endSequential = System.nanoTime();

    System.out.println("-- addDerivativeTest --");
    System.out.println("parallel time:   " + (endParallel - startParallel) + " ms");
    System.out.println("sequential time: " + (endSequential - startSequential) + " ms");
    System.out.println();
  }
}
