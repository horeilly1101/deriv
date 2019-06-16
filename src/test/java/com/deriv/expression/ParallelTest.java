package com.deriv.expression;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Power.exponential;
import static com.deriv.expression.Power.poly;
import static com.deriv.expression.Variable.x;
import static java.util.stream.Collectors.toList;
import static com.deriv.expression.Trig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
   * Helper method that creates a list of sine functions with linearly increasing
   * sums inside. (i.e. [sin(x), sin(x + 1), sin(x + 2), ...])
   * @param num number elements plus 1
   * @return list of sine functions
   */
  private List<Expression> sinList(int num) {
    return Stream.iterate(0, i -> i + 1)
             .map(i -> sin(add(x(), constant(i))))
             .limit(num).collect(toList());
  }

  /**
   * Helper method that creates a list of exponentials with a linearly increasing
   * base. (i.e. [1^x, 2^x, 3^x, ...])
   * @param num number elements plus 1
   * @return list of exponentials
   */
  private List<Expression> expoList(int num) {
    return Stream.iterate(1, i -> i + 1)
             .map(i -> exponential(i, x()))
             .limit(num)
             .collect(Collectors.toList());
  }

  /**
   * Helper method to compare parallel programs with their equivalent sequential programs.
   * It also asserts equality between the outputs of each program. Prints out results at end.
   * @param testName the name of the test
   * @param parProgram Runnable parallel program
   * @param seqProgram Runnable sequential program
   * @param <T> type param
   */
  private <T> void runComparison(String testName, Supplier<T> parProgram, Supplier<T> seqProgram) {
    // time the parallel program
    long startParallel = System.nanoTime();
    T parResult = parProgram.get();
    long endParallel = System.nanoTime();

    // time the sequential program
    long startSequential = System.nanoTime();
    T seqResult = seqProgram.get();
    long endSequential = System.nanoTime();

    // results should be the same -- checks for an error
    assertEquals(parResult, seqResult);

    // print results
    System.out.println("-- " + testName +  " --");
    System.out.println("parallel time:   " + (endParallel - startParallel) + " ms");
    System.out.println("sequential time: " + (endSequential - startSequential) + " ms");
    System.out.println();
  }

  /**
   * Sequential version of our Mult differentiate() algorithm.
   * @param factorList list of expressions
   * @param var with respect to
   * @return optional result
   */
  private static Optional<Expression> sequentialMultDerivative(List<Expression> factorList, Variable var) {
    // always compute product rule down the middle of the list of factors
    int mid = factorList.size() / 2;

    // compute derivatives
    Optional<Expression> firstDerivative = mult(factorList.subList(mid, factorList.size())).differentiate(var);
    Optional<Expression> secondDerivative = mult(factorList.subList(0, mid)).differentiate(var);

    // combine the derivatives together
    return firstDerivative
             .flatMap(x -> secondDerivative
                             .map(y ->
                                    add(
                                      mult(mult(factorList.subList(0, mid)), x),
                                      mult(y, mult(factorList.subList(mid, factorList.size())))
                                    )));
  }

//  @Test
  void addEvaluateTest() {
    Expression result = add(polyList(100_000));

    runComparison("addEvaluateTest",
      () -> ExpressionUtils.linearityHelper(
        result.asAdd().getTerms(),
        x -> x.evaluate(x(), multID()))
              .map(Add::add), // add the results

      () -> sequentialLinearityHelper(
        result.asAdd().getTerms(),
        x -> x.evaluate(x(), multID()))
              .map(Add::add)); // add the results
  }

//  @Test
  void addDerivativeTest() {
    Expression result = add(polyList(1_000));

    runComparison("addDerivativeTest",
      () -> ExpressionUtils.linearityHelper(
        result.asAdd().getTerms(),
        x -> x.differentiate(x())).map(Add::add),

      () -> sequentialLinearityHelper(
        result.asAdd().getTerms(),
        x -> x.differentiate(x())).map(Add::add));
  }

//  @Test
  void addDerivativeTest2() {
    Expression result = add(expoList(1_000));

    runComparison("addDerivativeTest2",
      () -> ExpressionUtils.linearityHelper(
        result.asAdd().getTerms(),
        x -> x.differentiate(x())).map(Add::add),

      () -> sequentialLinearityHelper(
        result.asAdd().getTerms(),
        x -> x.differentiate(x())).map(Add::add));
  }

//  @Test
  void multEvaluateTest() {
    Expression result = mult(sinList(10_000));

    runComparison("multEvaluateTest",
      () -> ExpressionUtils.linearityHelper(
        result.asMult().getFactors(),
        x -> x.evaluate(x(), multID())).map(Mult::mult),

      () -> sequentialLinearityHelper(
        result.asMult().getFactors(),
        x -> x.evaluate(x(), multID())).map(Mult::mult));
  }

//  @Test
  void multDerivativeTest() {
    Expression result = mult(sinList(1_000));

    runComparison("multDerivativeTest",
      () -> result.differentiate(x()), // parallel

      () -> sequentialMultDerivative(result.asMult().getFactors(), x()));
  }

//  @Test
  void multDerivativeTest2() {
    Expression result = mult(expoList(500));

    runComparison("multDerivativeTest2",
      () -> result.differentiate(x()), // parallel

      () -> sequentialMultDerivative(result.asMult().getFactors(), x()));
  }
}
