package com.deriv.expression;

import com.deriv.expression.cmd.DerivativeCmd;
import com.deriv.simplifier.MultSimplifier;
import com.deriv.util.Tuple;

import java.util.*;

import static com.deriv.expression.Add.*;
import static com.deriv.expression.Power.*;
import static java.util.stream.Collectors.toList;
import static com.deriv.expression.Constant.*;

public class Mult extends AExpression {
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
  public static Expression mult(List<Expression> factors) {
    if (factors.isEmpty()) {
      // bad
      throw new RuntimeException("Don't instantiate a term with an empty list!");
    }

    return new MultSimplifierComplete(factors).simplifyToExpression();
  }

  /**
   * Or use this if you really want to.
   */
  public static Expression mult(Expression... factors) {
    return mult(Arrays.asList(factors));
  }

  /**
   * Use this constructor to play with division.
   */
  public static Expression div(Expression numerator, Expression denominator) {
    return mult(numerator, poly(denominator, -1));
  }

  /**
   * negates an Expression;
   */
  public static Expression negate(Expression expr) {
    return mult(constant(-1), expr);
  }

  public List<Expression> getFactors() {
    return factors;
  }

  @Override
  public Expression getConstantFactor() {
    List<Expression> constants = factors.stream()
                                    .filter(x -> x.isConstant()
                                                     || (x.getBase().isConstant()
                                                             && x.getExponent().equals(constant(-1))))
                                    .collect(toList());

    return constants.isEmpty()
               ? multID()
               : mult(constants);
  }

  @Override
  public Expression getSymbolicFactors() {
    List<Expression> symbolic = factors.stream()
                                    .filter(x -> !x.isConstant()
                                                     && !(x.getBase().isConstant()
                                                             && x.getExponent().equals(constant(-1))))
                                    .collect(toList());
    return symbolic.isEmpty()
               ? multID()
               : mult(symbolic);
  }

  @Override
  public Boolean isNegative() {
    Expression constantFactor = this.getConstantFactor();

    return constantFactor.getNumerator().isNegative()
               || constantFactor.getDenominator().isNegative();
  }

  @Override
  public Expression getNumerator() {
    List<Expression> num = factors.stream()
                               .filter(x -> !x.getExponent().isNegative())
                               .collect(toList());
    return num.isEmpty() ? multID() : mult(num);
  }

  @Override
  public Expression getDenominator() {
    List<Expression> den = factors.stream()
                               .filter(x -> x.getExponent().isNegative())
                               .map(y -> poly(y, -1))
                               .collect(toList());

    return den.isEmpty() ? multID() : mult(den);
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
    Expression den = this.getDenominator();

    if (!den.equals(multID())) {
      Expression num = this.getNumerator();

      // probably the easiest way to write this
      return num.toString() + " / " + den.toString();
    }

    return "(" + factors.get(0).toString()
               + factors.subList(1, factors.size()).stream() //  sublist is O(1)
                     .map(Expression::toString)
                     .reduce("", (a, b) -> a + " * " + b)
               + ")";
  }

  public Optional<Expression> evaluate(Variable var, Expression input) {
    // multiplies terms together
    List<Optional<Expression>> eval = factors.stream()
                                          .map(x -> x.evaluate(var, input))
                                          .collect(toList());

    // make sure each term was evaluated
    return eval.stream().filter(Optional::isPresent).count() == eval.size()
               ? Optional.of(
                   mult(
                       eval.stream().map(Optional::get).collect(toList())))
               : Optional.empty();
  }

  public Expression derive(Variable var, DerivativeCmd<Tuple<Expression, Variable>, Expression> cache) {
    // always compute product rule down the middle of the list of factors
    int mid = factors.size() / 2;

    Expression firstDerivative = mult(factors.subList(0, mid))
                                    .deriveCache(var, cache);

    Expression secondDerivative = mult(factors.subList(mid, factors.size()))
                                     .deriveCache(var, cache);

    return add(
              mult(
                  mult(factors.subList(0, mid)),
                  secondDerivative
              ),
              mult(
                  firstDerivative,
                  mult(factors.subList(mid, factors.size()))
              ))
             // add product rule as a step
             .addStep(Step.PRODUCT_RULE, this)
             // add the steps of the resulting derivatives
             .extendSteps(secondDerivative.getSteps())
             .extendSteps(firstDerivative.getSteps());
  }

  private static class MultSimplifierComplete extends MultSimplifier {
    MultSimplifierComplete(List<Expression> unFactors) {
      super(unFactors);
    }

    public Expression toExpression() {
      if (unFactors.size() == 2) {
        List<Expression> con = unFactors.stream()
                                 .filter(Expression::isConstant)
                                 .collect(toList());

        List<Expression> remain = unFactors.stream()
                                    .filter(Expression::isAdd)
                                    .collect(toList());

        if (con.size() == 1 && remain.size() == 1) {
          // distribute the constant amongst the terms
          return add(
            remain.get(0).asAdd().getTerms().stream()
              .map(x -> mult(con.get(0), x))
              .collect(toList()));
        }
      }

      // sort the factors
      List<Expression> simplified = unFactors.stream().sorted().collect(toList());
      return simplified.size() > 1 ? new Mult(simplified) : simplified.get(0);
    }
  }
}