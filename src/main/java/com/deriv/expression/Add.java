package com.deriv.expression;

import com.deriv.expression.simplifier.AddSimplifier;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

/**
 * An Ddd represents several terms, added together.
 *
 * Data definition: an add is a list of Expressions (terms). This is analogous to
 * the terms in an expression.
 */
public class Add implements Expression {
  /**
   * A list of terms to be added together.
   */
  private List<Expression> _terms;

  /**
   * Instantiates an Add. Avoid using as much as possible! Use the easy constructor
   * instead.
   */
  private Add(List<Expression> terms) {
    this._terms = terms;
  }

  /**
   * Static constructor for an Add.
   * @param terms list of expressions
   * @return Add
   */
  public static Expression add(List<Expression> terms) {
    if (terms.isEmpty()) {
      throw new RuntimeException("Don't instantiate an add with an empty list!");
    } else {
      return new AddSimplifierComplete(terms).simplifyToExpression();
    }
  }

  /**
   * Static constructor for an Add.
   * @param terms array of expressions
   * @return Add
   */
  public static Expression add(Expression... terms) {
    return add(Arrays.asList(terms));
  }

  /**
   * Getter method for the terms of an Add.
   * @return terms
   */
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

  /**
   * Helper methods that takes advantage of the linearity of add operations.
   * @param func linear function
   * @return aggregated result
   */
  private Optional<Expression> linearityHelper(Function<Expression, Optional<Expression>> func) {
    // combines terms
    return Optional.of(_terms.parallelStream()
                         .map(func)
                         .filter(Optional::isPresent)
                         .map(Optional::get)
                         .collect(toList()))
             .flatMap(lst -> lst.size() == _terms.size()
                               ? Optional.of(add(lst))
                               : Optional.empty());
  }

 @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    // adds terms together
    return linearityHelper(x -> x.evaluate(var, input));
  }

  @Override
  public Optional<Expression> differentiate(Variable var) {
    // linearity of differentiation
    return linearityHelper(x -> x.differentiate(var));
  }

  /**
   * Extension of AddSimplifier that allows us to create Add objects.
   */
  private static class AddSimplifierComplete extends AddSimplifier {
    /**
     * Constructor for an AddSimplifierComplete.
     * @param unTerms input terms
     */
    AddSimplifierComplete(List<Expression> unTerms) {
      super(unTerms);
    }

    @Override
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
