package horeilly1101.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Mult implements Expression {
  private List<Expression> factors;

  /**
   * Instantiates a Mult. Avoid using as much as possible! Use the easy constructor
   * instead.
   *
   * Data definition: a term is a list of Expressions (factors). This is analogous
   * to the factors in an expression.
   */
  private Mult(List<Expression> factors) {
    this.factors = factors;
  }

  /**
   * Use this to create a mult object.
   */
  private static Expression mult(List<Expression> factors) {
    if (factors.isEmpty()) {
      throw new RuntimeException("Don't instantiate a term with an empty list!");
    }

    List<Expression> simplified = simplify(factors);
    return simplified.size() > 1 ? new Mult(simplified) : simplified.get(0);
  }

  static Expression mult(Expression... factors) {
    return mult(Arrays.asList(factors));
  }

  private List<Expression> getFactors() {
    return factors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Mult)) {
      return false;
    }

    Mult mul = (Mult) o;
    return mul.factors.equals(this.factors);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 8;
  }

  @Override
  public String toString() {
    // factors should never be empty, but we're being suitably cautious
    return factors.isEmpty()
               ? ""
               // a little clunky, but it gets the job done
               : "(" + factors.get(0).toString()
                     + factors.subList(1, factors.size()).stream() //  sublist is O(1)
                           .map(Expression::toString)
                           .reduce("", (a, b) -> a + " * " + b) + ")";
  }

  public Expression evaluate(String var, Double input) {
    // multiplies factors together
    return mult(factors.stream()
                    .map(x -> x.evaluate(var, input))
                    .collect(toList()));
  }

  public Expression differentiate(String var) {
    return Add.add(
        mult(
            factors.get(0),
            mult(factors.subList(1, factors.size())).differentiate(var)
        ),
        mult(
            factors.get(0).differentiate(var),
            mult(factors.subList(1, factors.size()))
        ));
  }

  /*
  Private, static methods to help simplify Mult objects when instantiated
   */

  private static List<Expression> simplify(List<Expression> factors) {
    return simplifyFactors(simplifyConstantFactors(withoutNesting(factors)));
  }

  /**
   * This method simplifies a list of factors by ensuring factors
   * are taken to the proper exponents. (e.g. we want to write x * x
   * as x ^ 2.0.)
   *
   * @return List<Expression> simplified
   */
  private static List<Expression> simplifyFactors(List<Expression> factors) {
    HashMap<Expression, List<Expression>> powerMap = new HashMap<>();

    for (Expression fac : factors) {
      if (powerMap.containsKey(fac.getBase())) {
        List<Expression> newList = powerMap.get(fac.getBase());
        newList.add(fac.getExponent());
        powerMap.replace(fac.getBase(), newList);
      } else {
        List<Expression> newList = new ArrayList<>();
        newList.add(fac.getExponent());
        powerMap.put(fac.getBase(), newList);
      }
    }

    // add up the exponents
    return powerMap.keySet().stream()
               .map(key -> Power.power(
                   key,
                   Add.add(powerMap.get(key))))
               .collect(toList());
  }

  /**
   * This method simplifies a list of factors to get rid of extraneous
   * constant factors. (e.g. multipling an expression by 1 should yield
   * the expression, multiplying an expression by 0 should yield zero,
   * and so on.)
   *
   * It also multiplies the values of all constants together, so that each
   * mult has a single Constant.
   *
   * @return List<Expression> simplified
   */
  private static List<Expression> simplifyConstantFactors(List<Expression> factors) {
    // keep track of constants' values
    List<Expression> noConstants = new ArrayList<>();
    Double constants = 1.0;

    for (Expression factor : factors) {
      if (factor.getClass().equals(Constant.class)) {
        // checked cast
        constants *= ((Constant) factor).getVal();
      } else {
        noConstants.add(factor);
      }
    }

    // multiplicative identity?
    if (constants == 1.0 && noConstants.isEmpty()) {
      noConstants.add(Constant.constant(1.0));
      // zero?
    } else if (constants == 0.0) {
      // all factors go to zero
      noConstants.clear();
      noConstants.add(Constant.constant(0.0));
    } else if (constants != 1.0) {
      noConstants.add(Constant.constant(constants));
    }

    return noConstants;
  }

  /**
   * This method simplifies a list of factors by taking advantage of
   * the associativity of multiplication. (i.e. a Mult object multiplied
   * by a Mult object should not yield a Mult object of two Mult objects.
   * It should yield a Mult object of whatever was in the original objects,
   * flatmapped together.)
   *
   * @return List<Expression> simplified
   */
  private static List<Expression> withoutNesting(List<Expression> factors) {
    List<Expression> newList = new ArrayList<>();

    for (Expression factor : factors) {
      if (factor.isMult()) {
        // checked cast
        newList.addAll(factor.asMult().getFactors());
      } else {
        newList.add(factor);
      }
    }

    return newList;
  }

}