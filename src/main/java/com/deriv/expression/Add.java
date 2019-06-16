package com.deriv.expression;

import com.deriv.expression.simplifier.AddSimplifier;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.Arrays;
import java.util.Objects;

import static com.deriv.expression.ExpressionUtils.mapAndJoin;
import static com.deriv.expression.ExpressionUtils.shallowCopy;
import static java.util.stream.Collectors.*;

/**
 * An Add represents several terms, added together.
 *
 * Data definition: an add is a list of Expressions (terms). This is analogous to
 * the terms in an expression.
 */
public class Add implements Expression {
  /**
   * A list of terms to be added together.
   */
  private final List<Expression> _terms;

  /**
   * Private constructor for an Add. Use one of the static constructors instead.
   */
  private Add(List<Expression> terms) {
    this._terms = terms;
  }

  /**
   * Static constructor for an Add.
   * @param terms list of expressions
   * @return Add
   */
  public static Expression add(List<? extends Expression> terms) {
    if (terms.isEmpty())
      throw new RuntimeException("Don't instantiate an add with an empty list!");

    return new AddSimplifierComplete(shallowCopy(terms)).simplifyToExpression();
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
    return shallowCopy(_terms);
  }

  @Override
  public boolean isNegative() {
    return _terms.stream()
               .map(Expression::isNegative)
               .reduce(true, (a, b) -> a && b);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (!(o instanceof Add))
      return false;

    Add ad = (Add) o;
    return _terms.equals(ad._terms);
  }

  @Override
  public int hashCode() {
    return 31 * _terms.hashCode() + 7;
  }

  @Override
  public String toString() {
    return "(" + mapAndJoin(_terms, Objects::toString, " + ") + ")";
  }

  @Override
  public String toLaTex() {
    return mapAndJoin(_terms, Expression::toLaTex, " + ");
  }

 @Override
  public Optional<Expression> evaluate(Variable var, Expression input) {
    // adds terms together
    return ExpressionUtils.linearityHelper(_terms, x -> x.evaluate(var, input)).map(Add::add);
  }

  @Override
  public Optional<Expression> differentiate(Variable var) {
    // linearity of differentiation
    return ExpressionUtils.linearityHelper(_terms, x -> x.differentiate(var)).map(Add::add);
  }

  @Override
  public Set<Variable> getVariables() {
    return _terms.stream().flatMap(x -> x.getVariables().stream()).collect(toSet());
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

      if (simplified.size() > 1)
        return new Add(simplified);

      return simplified.get(0);
    }
  }
}
