package com.deriv.expression;

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

    return (new MultSimplifier(factors)).simplify().toExpression();
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

  List<Expression> getFactors() {
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

  public Optional<Expression> evaluate(String var, Expression input) {
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

  public Expression differentiate(String var) {
    addStep(Step.PRODUCT_RULE, this);

    // always compute product rule down the middle of the list of factors
    int mid = factors.size() / 2;
    Expression firstDerivative = mult(factors.subList(0, mid)).differentiate(var);
    Expression secondDerivative = mult(factors.subList(mid, factors.size())).differentiate(var);

    //

    return add(
              mult(
                  mult(factors.subList(0, mid)),
                  secondDerivative
              ),
              mult(
                  firstDerivative,
                  mult(factors.subList(mid, factors.size()))
              ));
  }

  /**
  Private, static class to help simplify Mult objects when instantiated.

  The below methods are probably the ugliest ones you'll see in this project,
  but they've been thoroughly tested, and they each serve an important role
  in simplifying mults.
   */
  private static class MultSimplifier implements Simplifier {
    private List<Expression> unFactors;

    MultSimplifier(List<Expression> unFactors) {
      this.unFactors = unFactors;
    }

    /**
     * This function figures out whether or not the list of factors is
     * fully simplified.
     */
    public Boolean isSimplified() {
      // we want to make sure there is at most 1 constant in numerator and denominator
      int numCount = 0;
      Constant num = multID().asConstant();

      int dencount = 0;
      Constant den = multID().asConstant();

      Set<Expression> bases = new HashSet<>();

      for (Expression fac : unFactors) {
        if (fac.isConstant()) {
          numCount += 1;
          num = fac.asConstant();
        }

        if (fac.getBase().isConstant() && fac.getExponent().equals(constant(-1))) {
          dencount += 1;
          den = fac.getBase().asConstant();
        }

        // all of these conditions imply this is not simplified
        if (numCount > 1
                || dencount > 1
                || gcf(num.getVal(), den.getVal()) != 1
                || (fac.equals(addID()) && unFactors.size() > 1)
                || fac.isMult()
                || bases.contains(fac.getBase())
        ) {
          return false;
        }

        bases.add(fac.getBase());
      }

      // ensure there aren't extraneous 1 multiples
      return !(bases.contains(multID()) && unFactors.size() != 1);
    }

    /**
     * This function brings together all the simplify functions. It runs
     * recursively until there is nothing left to simplify.
     *
     * (Everything but the (possible) recursive call runs in expected linear
     * time.)
     */
    public Simplifier simplify() {
      return this.isSimplified()
                 ? this
                 : this.withoutNesting().simplifyConstantFactors().simplifyFactors()
                       .simplify();
    }

    /**
     * This method checks 2 things. First, it checks if factors is just a list of a constant and an Add.
     * If this is the case, we want to distribute the constant among the terms and return an Add.
     * Second, it checks whether or not a list of factors has more than one element. If it does, then it
     * creates a mult. If it has just one object, it just returns the object.
     */
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

    /**
     * This method simplifies a list of factors by ensuring factors
     * are taken to the proper exponents. (e.g. we want to write x * x
     * as x ^ 2.0.)
     *
     * @return List<Expression> simplified
     */
    MultSimplifier simplifyFactors() {
      HashMap<Expression, List<Expression>> powerMap = new HashMap<>();

      for (Expression fac : unFactors) {
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
      List<Expression> result = powerMap.keySet().stream()
                                   .map(key -> power(
                                       key,
                                       add(powerMap.get(key))))
                                   .collect(toList());

      return new MultSimplifier(result);
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
     * This method also simplifies constants in the numerator and denominator
     * by dividing each by the greatest common factor. (Check out Euclid's
     * algorithm down below.)
     *
     * @return MultSimplifier simplified
     */
    MultSimplifier simplifyConstantFactors() {
      // keep track of constant values in the numerator and the denominator
      List<Expression> noConstants = new ArrayList<>();
      int numConstants = 1;
      int denConstants = 1;

      for (Expression factor : unFactors) {
        if (factor.isConstant()) {
          // checked cast
          numConstants *= factor.asConstant().getVal();
        } else if (factor.isPower()
                       && factor.asPower().getBase().isConstant()
                       && factor.asPower().getExponent().equals(constant(-1))) {

          denConstants *= factor.asPower().getBase().asConstant().getVal();
        } else {
          noConstants.add(factor);
        }
      }

      // divide numConstants and denConstants by gcf
      int gcf = gcf(numConstants, denConstants);
      numConstants /= gcf;
      denConstants /= gcf;

      // multiplicative identity?
      if (numConstants == 1 && denConstants == 1 && noConstants.isEmpty()) {
        noConstants.add(multID());
        // zero?
      } else if (numConstants == 0) {
        // all factors go to zero
        noConstants.clear();
        noConstants.add(addID());
      } else if (numConstants != 1) {
        noConstants.add(constant(numConstants));
      }

      // is there a constant in the denominator?
      if (denConstants != 1 && numConstants != 0) {
        noConstants.add(poly(constant(denConstants), -1));
      }

      return new MultSimplifier(noConstants);
    }

    /**
     * This method simplifies a list of factors by taking advantage of
     * the associativity of multiplication. (i.e. a Mult object multiplied
     * by a Mult object should not yield a Mult object of two Mult objects.
     * It should yield a Mult object of whatever was in the original objects,
     * flatmapped together.)
     *
     * @return MultSimplifier simplified
     */
    MultSimplifier withoutNesting() {
      List<Expression> newList = new ArrayList<>();

      for (Expression factor : unFactors) {
        // ensures no nested mults
        if (factor.isMult()) {
          // checked cast
          newList.addAll(factor.asMult().getFactors());
        } else {
          newList.add(factor);
        }
      }

      return new MultSimplifier(newList);
    }
  }

  /**
   * This method uses Euclid's recursive algorithm to find the greatest common
   * factor of two given integers.
   */
  private static int gcf(int a, int b) {
    if (a == 0) {
      return b;
    }

    if (b == 0) {
      return a;
    }

    return gcf(b, a % b);
  }
}