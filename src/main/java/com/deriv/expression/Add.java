package com.deriv.expression;

import java.util.*;

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
    this.terms = terms;
  }

  /**
   * Use this function to instantiate an Add object.
   */
  public static Expression add(List<Expression> terms) {
    if (terms.isEmpty()) {
      throw new RuntimeException("Don't instantiate an add with an empty list!");
    } else {
      return (new AddSimplifier(terms)).simplify().toExpression();
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
  public Boolean isNegative() {
    return terms.stream()
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
    return ad.terms.equals(this.terms);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 9;
  }

  @Override
  public String toString() {
    return "(" + terms.get(0)
               + terms.subList(1, terms.size()).stream()
                     .map(Expression::toString)
                     .reduce("", (a, b) -> a + " + " + b)
               + ")";
  }

  public Optional<Expression> evaluate(String var, Expression input) {
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

  /**
   * AddSimplifier exists to help us simplify and make consistent
   * the construction of Add objects.
   *
   * You shouldn't mess with this code!
   */
  private static class AddSimplifier implements Simplifier {
    List<Expression> unTerms;

    private AddSimplifier(List<Expression> unTerms) {
      this.unTerms = unTerms;
    }

    /**
     * This function checks whether or not the given list of Expressions
     * can form a simplified Add object.
     */
    public Boolean isSimplified() {
      // we want to make sure there is at most 1 constant in factors
      int conCount = 0;
//    int denConCount = 0;
      Set<Expression> bases = new HashSet<>();

      for (Expression term : unTerms) {
        if (term.isConstant()) {
          conCount += 1;
        }

//      if (term.getBase().isConstant() && term.getExponent().equals(constant(-1))) {
//        denConCount += 1;
//      }

        // all of these conditions imply factors is not simplified
        if (conCount > 1
//              || denConCount > 1
                || (term.equals(addID()) && unTerms.size() > 1)
                || term.isAdd()
                || bases.contains(term.getSymbolicFactors())
                       && !term.getSymbolicFactors().equals(multID())) {
          return false;
        }

        bases.add(term.getSymbolicFactors());
      }

      return true;
    }

    /**
     * This function brings together all the simplify functions. It runs
     * recursively until there is nothing left to simplify.
     *
     * (Everything but the (possible) recursive call runs in expected linear
     * time.)
     */
    public Simplifier simplify() {
      return unTerms.size() > 1 && !this.isSimplified()
                 ? this.withoutNesting().simplifyConstantTerms().simplifyTerms().simplify()
                 : this;
    }

    public Expression toExpression() {
      List<Expression> simplified = unTerms.stream()
                                        .sorted((a, b) -> -1 * a.compareTo(b))
                                        .collect(toList());

      return simplified.size() > 1
                 ? new Add(simplified)
                 : simplified.get(0);

    }

    /**
     * This method adds terms with common factors. (e.g. It would take
     * x + 2x and output 3 * x.)
     */
    AddSimplifier simplifyTerms() {
      // maintain a hash map of factors we've already seen
      // this allows us to compute this function in linear time
      HashMap<Expression, List<Expression>> powerMap = new HashMap<>();

      for (Expression term : unTerms) {
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
      List<Expression> result = powerMap.keySet().stream()
                                   .map(key -> mult(
                                       key,
                                       add(powerMap.get(key))))
                                   .collect(toList());

      return new AddSimplifier(result);
    }

    /**
     * This method adds up the values of constant elements of unTerms.
     */
    AddSimplifier simplifyConstantTerms() {
      // keep track of constants' values
      List<Expression> noConstants = new ArrayList<>();
      Integer constants = 0;

      for (Expression term : unTerms) {
        if (term.isConstant()) {
          // checked cast
          constants += term.asConstant().getVal();
        } else {
          noConstants.add(term);
        }
      }

      // multiplicative identity?
      if (constants == 0.0 && noConstants.isEmpty()) {
        noConstants.add(addID());
        // zero?
      } else if (constants != 0.0) {
        noConstants.add(constant(constants));
      }

      return new AddSimplifier(noConstants);
    }

    /**
     * This function ensures there is no nesting in unTerms. (i.e. none
     * of its elements are Add objects.)
     */
    AddSimplifier withoutNesting() {
      List<Expression> newList = new ArrayList<>();

      for (Expression term : unTerms) {
        if (term.isAdd()) {
          // checked cast
          newList.addAll(term.asAdd().getTerms());
        } else {
          newList.add(term);
        }
      }

      return new AddSimplifier(newList);
    }
  }
}
