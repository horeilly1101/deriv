package horeilly1101.Expression;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
}
