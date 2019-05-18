package com.deriv.expression;

import com.deriv.expression.simplifier.MultSimplifier;
import java.util.*;

import static com.deriv.expression.Add.*;
import static com.deriv.expression.Power.*;
import static java.util.stream.Collectors.toList;
import static com.deriv.expression.Constant.*;

public class Mult implements Expression {
  /**
   * A list of _factors to be multiplied together.
   */
  private List<Expression> _factors;

  /**
   * Instantiates a Mult. Avoid using as much as possible! Use the easy constructor
   * instead.
   *
   * Data definition: a term is a list of Expressions (_factors). This is analogous
   * to the _factors in an expression.
   */
  private Mult(List<Expression> _factors) {
    this._factors = _factors;
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
    return _factors;
  }

  @Override
  public Expression getConstantFactor() {
    List<Expression> constants = _factors.stream()
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
    List<Expression> symbolic = _factors.stream()
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
    List<Expression> num = _factors.stream()
                               .filter(x -> !x.getExponent().isNegative())
                               .collect(toList());
    return num.isEmpty() ? multID() : mult(num);
  }

  @Override
  public Expression getDenominator() {
    List<Expression> den = _factors.stream()
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
    return mul._factors.equals(this._factors);
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

    return "(" + _factors.get(0).toString()
               + _factors.subList(1, _factors.size()).stream() //  sublist is O(1)
                     .map(Expression::toString)
                     .reduce("", (a, b) -> a + " * " + b)
               + ")";
  }

  @Override
  public String toLaTex() {
    Expression den = this.getDenominator();

    if (!den.equals(multID())) {
      Expression num = this.getNumerator();

      // probably the easiest way to write this
      return "\\frac{" + num.toLaTex() + "}{" + den.toLaTex() + "}";
    }

    return _factors.get(0).toLaTex()
             + _factors.subList(1, _factors.size()).stream() //  sublist is O(1)
                 .map(ex -> ex.isAdd() ? "(" + ex.toLaTex() + ")" : ex.toLaTex())
                 .reduce("", (a, b) -> a + b);
  }

  public Optional<Expression> evaluate(Variable var, Expression input) {
    // multiplies terms together
    return Optional.of(_factors.parallelStream()
                         .map(x -> x.evaluate(var, input))
                         .filter(Optional::isPresent)
                         .map(Optional::get)
                         .collect(toList()))
             .flatMap(lst -> lst.size() == _factors.size()
                               ? Optional.of(mult(lst))
                               : Optional.empty());
  }

  public Optional<Expression> differentiate(Variable var) {
    // always compute product rule down the middle of the list of _factors
    int mid = _factors.size() / 2;

    // compute derivatives
    Optional<Expression> firstDerivative = mult(_factors.subList(mid, _factors.size())).differentiate(var);
    Optional<Expression> secondDerivative = mult(_factors.subList(0, mid)).differentiate(var);

    // combine the derivatives together
    return firstDerivative
             .flatMap(x -> secondDerivative
                             .map(y ->
                                    add(
                                      mult(mult(_factors.subList(0, mid)), x),
                                      mult(y, mult(_factors.subList(mid, _factors.size())))
                                    )));
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

      // sort the _factors
      List<Expression> simplified = unFactors.stream().sorted().collect(toList());
      return simplified.size() > 1 ? new Mult(simplified) : simplified.get(0);
    }
  }
}