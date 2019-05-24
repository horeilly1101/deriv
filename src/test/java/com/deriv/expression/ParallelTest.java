package com.deriv.expression;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Power.poly;
import static com.deriv.expression.Variable.x;
import static java.util.stream.Collectors.toList;
import static com.deriv.expression.Trig.*;

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

  /**
   * Helper method that creates a list of polynomials with a linearly increasing
   * exponent. (i.e. [1, x, x^2, x^3, ...])
   * @param num number elements plus 1
   * @return list of polynomials
   */
  private List<Expression> polyList(int num) {
    return Stream.iterate(0, i -> i + 1)
             .map(i -> poly(x(), i))
             .limit(num)
             .collect(Collectors.toList());
  }

  /**
   * Helper method to compare parallel programs with their equivalent sequential programs.
   * Prints out results.
   * @param testName the name of the test
   * @param parProgram Runnable parallel program
   * @param seqProgram Runnable sequential program
   */
  private void runComparison(String testName, Runnable parProgram, Runnable seqProgram) {
    // time the parallel program
    long startParallel = System.nanoTime();
    parProgram.run();
    long endParallel = System.nanoTime();

    // time the sequential program
    long startSequential = System.nanoTime();
    seqProgram.run();
    long endSequential = System.nanoTime();

    // print results
    System.out.println("-- " + testName +  " --");
    System.out.println("parallel time:   " + (endParallel - startParallel) + " ms");
    System.out.println("sequential time: " + (endSequential - startSequential) + " ms");
    System.out.println();
  }

  @Test
  void addEvaluateTest() {
    Expression result = add(polyList(100_000));

    runComparison("addEvaluateTest",
      () -> ExpressionUtils.linearityHelper(
        result.asAdd().getTerms(),
        x -> x.evaluate(x().asVariable(), multID()))
              .map(Add::add), // add the results

      () -> sequentialLinearityHelper(
        result.asAdd().getTerms(),
        x -> x.evaluate(x().asVariable(), multID()))
              .map(Add::add)); // add the results
  }

  @Test
  void addDerivativeTest() {
    Expression result = add(polyList(10_000));

    runComparison("addDerivativeTest",
      () -> ExpressionUtils.linearityHelper(
        result.asAdd().getTerms(),
        x -> x.differentiate(x().asVariable())),

      () -> sequentialLinearityHelper(
        result.asAdd().getTerms(),
        x -> x.differentiate(x().asVariable())));
  }

  @Test
  void multEvaluateTest() {
    Expression result = mult(Stream.iterate(0, i -> i + 1)
                               .map(i -> sin(add(x(), constant(i))))
                               .limit(10_000).collect(toList()));

    runComparison("multEvaluateTest",
      () -> ExpressionUtils.linearityHelper(
        result.asMult().getFactors(),
        x -> x.evaluate(x().asVariable(), multID())),

      () -> sequentialLinearityHelper(
        result.asMult().getFactors(),
        x -> x.evaluate(x().asVariable(), multID())));
  }
}
