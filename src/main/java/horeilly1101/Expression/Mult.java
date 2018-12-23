package horeilly1101.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Mult implements Expression {
  private Constant constant;
  private List<Expression> factors;

  /**
   * Instantiates a Mult. Avoid using as much as possible! Use the easy constructor
   * instead.
   *
   * Data definition: a term is a list of Expressions (factors). This is analogous
   * to the factors in an expression.
   */
  Mult(List<Expression> factors) {
    this.constant = constant;
    this.factors = factors;
  }

  /**
   * Use this to create a mult object.
   */
  static Expression mult(List<Expression> factors) {
    if (factors.isEmpty()) {
      throw new RuntimeException("Don't instantiate a term with an empty list!");
    }

    Double conVal = 1.0;
    List<Expression> factorList = new ArrayList<>();

    for (Expression fac : factors) {
      if (fac.isConstant()) {
        conVal *= fac.asConstant().getVal();
      } else {
        factorList.add(fac);
      }
    }

    return simplified.size() > 1 ? new Mult(simplified) : simplified.get(0);
  }

  static Expression mult(Expression... factors) {
    return mult(Arrays.asList(factors));
  }

  public List<Expression> getFactors() {
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
}