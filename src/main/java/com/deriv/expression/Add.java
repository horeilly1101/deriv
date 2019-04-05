package com.deriv.expression;

import com.deriv.expression.cmd.ICacheCmd;
import com.deriv.expression.cmd.IStepCmd;
import com.deriv.expression.simplifier.AddSimplifier;
import java.util.*;

import static java.util.stream.Collectors.*;

public class Add extends AExpression {
  private List<Expression> _terms;

  /**
   * Instantiates an Add. Avoid using as much as possible! Use the easy constructor
   * instead.
   *
   * Data definition: an add is a list of Expressions (terms). This is analogous to
   * the terms in an expression.
   */
  private Add(List<Expression> terms) {
    this._terms = terms;
  }

  public static Expression add(List<Expression> terms) {
    if (terms.isEmpty()) {
      throw new RuntimeException("Don't instantiate an add with an empty list!");
    } else {
      return new AddSimplifierComplete(terms).simplifyToExpression();
    }
  }

  /**
   * Or this one.
   */
  public static Expression add(Expression... terms) {
    return add(Arrays.asList(terms));
  }

  public List<Expression> getTerms() {
    return _terms;
  }

  @Override
  public Boolean isNegative() {
    return _terms.stream()
               .map(Expression::isNegative)
               .reduce(true, (a, b) -> a && b);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Add)) {
      return false;
    }

    Add ad = (Add) o;
    return ad._terms.equals(this._terms);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 9;
  }

  @Override
  public String toString() {
    return "(" + _terms.get(0)
               + _terms.subList(1, _terms.size()).stream()
                     .map(Expression::toString)
                     .reduce("", (a, b) -> a + " + " + b)
               + ")";
  }

  @Override
  public String toLaTex() {
    return _terms.get(0).toLaTex()
             + _terms.subList(1, _terms.size()).stream()
                 .map(Expression::toLaTex)
                 .reduce("", (a, b) -> a + " + " + b);
  }

  public Optional<Expression> evaluate(Variable var, Expression input) {
    // adds terms together
    List<Optional<Expression>> eval = _terms.stream()
                                        .map(x -> x.evaluate(var, input))
                                           .collect(toList());

    // make sure each term was evaluated
    return eval.stream().filter(Optional::isPresent).count() == eval.size()
               ? Optional.of(
                   add(
                       eval.stream().map(Optional::get).collect(toList())))
               : Optional.empty();
   }

  public Expression computeDerivative(Variable var, ICacheCmd cacheCmd, IStepCmd stepCmd) {
    // linearity of differentiation
    return add(_terms.stream()
                 .map(x -> x.differentiate(var, cacheCmd, stepCmd))
                 .collect(toList()));
  }

  public Step getDerivativeStep() {
    return Step.LINEARITY;
  }

  /**
   * Extension of AddSimplifier that allows us to create Add objects.
   */
  private static class AddSimplifierComplete extends AddSimplifier {
    AddSimplifierComplete(List<Expression> unTerms) {
      super(unTerms);
    }

    public Expression toExpression() {
      List<Expression> simplified = unTerms.stream()
                                .sorted((a, b) -> -1 * a.compareTo(b))
                                .collect(toList());

      return simplified.size() > 1
         ? new Add(simplified)
         : simplified.get(0);
    }
  }
}
