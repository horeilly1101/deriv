package horeilly1101.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Mult.*;

public class Add implements Expression {
  private List<Expression> terms;

  /**
   * Instantiates an Add. Avoid using as much as possible! Use the easy constructor
   * instead.
   *
   * Data definition: an add is a list of Expressions (terms). This is analogous to
   * the terms in an expression.
   */
  private Add(List<Expression> terms) {
    this.terms = terms;
  }

  static Expression add(List<Expression> terms) {
    if (terms.isEmpty()) {
      throw new RuntimeException("don't instantiate an expr with an empty list!");
    } else {
      List<Expression> simplified = terms.stream()
                                        .sorted().collect(toList());
      return simplified.size() > 1 ? new Add(simplified) : simplified.get(0);
    }
  }

  static Expression add(Expression... terms) {
    return add(Arrays.asList(terms));
  }

  public List<Expression> getTerms() {
    return terms;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Add)) {
      return false;
    }

    Add ad = (Add) o;
    return ad.terms.equals(this.terms);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 9;
  }

  @Override
  public String toString() {
    return terms.isEmpty()
               ? ""
               // a little clunky, but it gets the job done
               : "[" + terms.get(0).toString()
                     + terms.subList(1, terms.size()).stream()
                           .map(Expression::toString)
                           .reduce("", (a, b) -> a + " + " + b)
                     + "]";
  }

  public Expression evaluate(String var, Double input) {
    // adds terms together
    return add(terms.stream()
                   .map(x -> x.evaluate(var, input))
                   .collect(toList()));
  }

  public Expression differentiate(String var) {
    // linearity of differentiation
    return add(terms.stream()
                   .map(x -> x.differentiate(var))
                   .collect(toList()));
  }

  /*
  Private, static methods to help simplify instantiated objects
   */

//  static List<Expression> simplify(List<Expression> terms) {
//    return terms.size() > 1 ? simplifyTerms(simplifyConstantTerms(withoutNesting(terms))) : terms;
//  }

//  /**
//   * This method simplifies a list of terms by ensuring terms
//   * are taken to the proper constants. (e.g. we want to write x + x
//   * as 2.0 * x.)
//   *
//   * @return List<Expression> simplified
//   */
//  private static List<Expression> simplifyTerms(List<Expression> terms) {
//    HashMap<Expression, List<Double>> powerMap = new HashMap<>();
//
//    for (Expression term : terms) {
//      if (powerMap.containsKey(term.getRemainingFactors())) {
//        List<Double> newList = powerMap.get(term.getRemainingFactors());
//        newList.add(term.getConstantFactor().getVal());
//        powerMap.replace(term.getRemainingFactors(), newList);
//      } else {
//        List<Double> newList = new ArrayList<>();
//        newList.add(term.getConstantFactor().getVal());
//        powerMap.put(term.getRemainingFactors(), newList);
//      }
//    }
//
//    // add up the constants
//    return powerMap.keySet().stream()
//               .map(key -> mult(
//                   key,
//                   constant(powerMap.get(key).stream()
//                                                    .reduce(0.0, (a, b) -> a + b))))
//               .collect(toList());
//  }

  /**
   * This method simplifies a list of factors to get rid of extraneous
   * constant factors. (e.g. adding 0.0)
   *
   * @return List<Expression> simplified
   */
  private static List<Expression> simplifyConstantTerms(List<Expression> factors) {
    // keep track of constants' values
    List<Expression> noConstants = new ArrayList<>();
    Double constants = 0.0;

    for (Expression factor : factors) {
      if (factor.isConstant()) {
        // checked cast
        constants += factor.asConstant().getVal();
      } else {
        noConstants.add(factor);
      }
    }

    // multiplicative identity?
    if (constants == 0.0 && noConstants.isEmpty()) {
      noConstants.add(constant(0.0));
      // zero?
    } else if (constants != 0.0) {
      noConstants.add(constant(constants));
    }

    return noConstants;
  }

  /**
   * This method simplifies a list of terms by taking advantage of
   * the associativity of addition. (i.e. a Mult object multiplied
   * by a Mult object should not yield a Mult object of two Mult objects.
   * It should yield a Mult object of whatever was in the original objects,
   * flatmapped together.)
   *
   * @return List<Expression> simplified
   */
  private static List<Expression> withoutNesting(List<Expression> terms) {
    List<Expression> newList = new ArrayList<>();

    for (Expression term : terms) {
      if (term.isAdd()) {
        // checked cast
        newList.addAll(term.asAdd().getTerms());
      } else {
        newList.add(term);
      }
    }

    return newList;
  }
}
