package com.deriv.expression;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Mult.*;

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
    // important check
    // mostly for debugging
    if (terms.size() < 2) {
      throw new RuntimeException("Add was created with less than two factors!");
    }

    this.terms = terms;
  }

  /**
   * Use this function to instantiate an Add object.
   */
  public static Expression add(List<Expression> terms) {
    if (terms.isEmpty()) {
      throw new RuntimeException("Don't instantiate an add with an empty list!");
    } else {
      // we want to add together all the divs

      List<Expression> simplified = simplify(terms).stream()
                                        // reverse order
                                        .sorted((x, y) -> -1 * x.compareTo(y))
                                        .collect(toList());
      return simplified.size() > 1 ? new Add(simplified) : simplified.get(0);
    }
  }

  /**
   * Or this one.
   */
  public static Expression add(Expression... terms) {
    return add(Arrays.asList(terms));
  }

  List<Expression> getTerms() {
    return terms;
  }

  @Override
  public Constant getConstantTerm() {
    List<Expression> constant = terms.stream().filter(Expression::isConstant).collect(toList());
    return constant.isEmpty() ? addID() : constant.get(0).asConstant();
  }

  @Override
  public Expression getSymbolicTerms() {
    return add(terms.stream().filter(x -> !x.isConstant()).collect(toList()));
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
    // want to be able to negate first term
    String firstTerm = terms.get(0).isNegative()
                           ? "-" + negate(terms.get(0)).toString()
                           : terms.get(0).toString();

    return "(" + firstTerm
               + terms.subList(1, terms.size()).stream()
                     // we want to print to subtraction
                     .map(x -> x.isNegative()
                                   ? " - " + negate(x).toString()
                                   : " + " + x.toString())
                     .reduce("", (a, b) -> a + b)
               + ")";
  }

  public Optional<Expression> evaluate(String var, Double input) {
    // adds terms together
    List<Optional<Expression>> eval = terms.stream()
                                           .map(x -> x.evaluate(var, input))
                                           .collect(toList());

    // make sure each term was evaluated
    return eval.stream().filter(Optional::isPresent).count() == eval.size()
               ? Optional.of(
                   add(
                       eval.stream().map(Optional::get).collect(toList())))
               : Optional.empty();
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

  /**
   * This function brings together all the simplify functions. It runs
   * recursively until there is nothing left to simplify.
   *
   * (Everything but the (possible) recursive call runs in expected linear
   * time.)
   */
  private static List<Expression> simplify(List<Expression> terms) {
    return terms.size() > 1 && !isSimplified(terms)
               ? simplify(
                   simplifyTerms(
                     simplifyConstantTerms(
                         withoutNesting(terms))))
               : terms;
  }

  /**
   * This method simplifies a list of terms by ensuring terms
   * are taken to the proper constants. (e.g. we want to write x + x
   * as 2.0 * x.)
   *
   * @return List<Expression> simplified
   */
  private static List<Expression> simplifyTerms(List<Expression> terms) {
    // maintain a hash map of factors we've already seen
    // this allows us to compute this function in linear time
    HashMap<Expression, List<Expression>> powerMap = new HashMap<>();

    for (Expression term : terms) {
      if (powerMap.containsKey(term.getSymbolicFactors())) {
        List<Expression> newList = powerMap.get(term.getSymbolicFactors());

        newList.add(term.getConstantFactor());
        powerMap.replace(term.getSymbolicFactors(), newList);

      } else {
        List<Expression> newList = new ArrayList<>();

        newList.add(term.getConstantFactor());
        powerMap.put(term.getSymbolicFactors(), newList);
      }
    }

    // add up the constants
    return powerMap.keySet().stream()
               .map(key -> mult(
                   key,
                   add(powerMap.get(key))))
               .collect(toList());
  }

  /**
   * This method simplifies a list of factors to get rid of extraneous
   * constant factors. (e.g. adding 0.0)
   *
   * @return List<Expression> simplified
   */
  private static List<Expression> simplifyConstantTerms(List<Expression> factors) {
    // keep track of constants' values
    List<Expression> noConstants = new ArrayList<>();
    Integer constants = 0;

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
      noConstants.add(addID());
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

  /**
   * This function checks whether or not the given list of Expressions
   * can form a simplified Add object.
   */
  private static Boolean isSimplified(List<Expression> terms) {
    // we want to make sure there is at most 1 constant in factors
    int conCount = 0;
    int denConCount = 0;
    Set<Expression> bases = new HashSet<>();

    for (Expression term : terms) {
      if (term.isConstant()) {
        conCount += 1;
      }

      if (term.getBase().isConstant() && term.getExponent().equals(constant(-1))) {
        denConCount += 1;
      }

      // all of these conditions imply factors is not simplified
      if (conCount > 1
              || denConCount > 1
              || (term.equals(addID()) && terms.size() > 1)
              || term.isAdd()
              || bases.contains(term.getSymbolicFactors())
                     && !term.getSymbolicFactors().equals(multID())) {
        return false;
      }

      bases.add(term.getSymbolicFactors());
    }

    return true;
  }
}
