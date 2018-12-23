package horeilly1101;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SimplifyExpression {

    static List<Expression> multSimplify(List<Expression> factors) {
      return simplifyFactors(simplifyConstantFactors(multWithoutNesting(factors)));
    }

    /**
     * This method simplifies a list of factors by ensuring factors
     * are taken to the proper exponents. (e.g. we want to write x * x
     * as x ^ 2.0.)
     *
     * @return List<Expression> simplified
     */
    static List<Expression> simplifyFactors(List<Expression> factors) {
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
                 .map(key -> Expression.Power.power(
                     key,
                     Expression.Add.add(powerMap.get(key))))
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
    static List<Expression> simplifyConstantFactors(List<Expression> factors) {
      // keep track of constants' values
      List<Expression> noConstants = new ArrayList<>();
      Double constants = 1.0;

      for (Expression factor : factors) {
        if (factor.getClass().equals(Expression.Constant.class)) {
          // checked cast
          constants *= ((Expression.Constant) factor).getVal();
        } else {
          noConstants.add(factor);
        }
      }

      // multiplicative identity?
      if (constants == 1.0 && noConstants.isEmpty()) {
        noConstants.add(Expression.Constant.constant(1.0));
        // zero?
      } else if (constants == 0.0) {
        // all factors go to zero
        noConstants.clear();
        noConstants.add(Expression.Constant.constant(0.0));
      } else if (constants != 1.0) {
        noConstants.add(Expression.Constant.constant(constants));
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
    static List<Expression> multWithoutNesting(List<Expression> factors) {
      List<Expression> newList = new ArrayList<>();

      for (Expression factor : factors) {
        if (factor.getClass().equals(Expression.Mult.class)) {
          // checked cast
          newList.addAll(factor.asMult().getFactors());
        } else {
          newList.add(factor);
        }
      }

      return newList;
    }

    static List<Expression> addSimplify(List<Expression> terms) {
      return terms.size() > 1 ? simplifyTerms(simplifyConstantTerms(addWithoutNesting(terms))) : terms;
    }

    /**
     * This method simplifies a list of terms by ensuring terms
     * are taken to the proper constants. (e.g. we want to write x + x
     * as 2.0 * x.)
     *
     * @return List<Expression> simplified
     */
    static List<Expression> simplifyTerms(List<Expression> terms) {
      HashMap<Expression, List<Double>> powerMap = new HashMap<>();

      for (Expression term : terms) {
        if (powerMap.containsKey(term.getRemainingFactors())) {
          List<Double> newList = powerMap.get(term.getRemainingFactors());
          newList.add(term.getConstantFactor().getVal());
          powerMap.replace(term.getRemainingFactors(), newList);
        } else {
          List<Double> newList = new ArrayList<>();
          newList.add(term.getConstantFactor().getVal());
          powerMap.put(term.getRemainingFactors(), newList);
        }
      }

      // add up the constants
      return powerMap.keySet().stream()
                 .map(key -> Expression.Mult.mult(
                     key,
                     Expression.Constant.constant(powerMap.get(key).stream()
                                                      .reduce(0.0, (a, b) -> a + b))))
                 .collect(toList());
    }

    /**
     * This method simplifies a list of factors to get rid of extraneous
     * constant factors. (e.g. adding 0.0)
     *
     * @return List<Expression> simplified
     */
    static List<Expression> simplifyConstantTerms(List<Expression> factors) {
      // keep track of constants' values
      List<Expression> noConstants = new ArrayList<>();
      Double constants = 0.0;

      for (Expression factor : factors) {
        if (factor.getClass().equals(Expression.Constant.class)) {
          // checked cast
          constants += ((Expression.Constant) factor).getVal();
        } else {
          noConstants.add(factor);
        }
      }

      // multiplicative identity?
      if (constants == 0.0 && noConstants.isEmpty()) {
        noConstants.add(Expression.Constant.constant(0.0));
        // zero?
      } else if (constants != 0.0) {
        noConstants.add(Expression.Constant.constant(constants));
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
    static List<Expression> addWithoutNesting(List<Expression> terms) {
      List<Expression> newList = new ArrayList<>();

      for (Expression term : terms) {
        if (term.getClass().equals(Expression.Add.class)) {
          // checked cast
          newList.addAll(term.asAdd().getTerms());
        } else {
          newList.add(term);
        }
      }

      return newList;
    }
}
