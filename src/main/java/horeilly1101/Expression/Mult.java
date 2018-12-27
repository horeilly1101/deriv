package horeilly1101.Expression;

import java.util.*;

import static horeilly1101.Expression.Add.*;
import static horeilly1101.Expression.Power.*;
import static java.util.stream.Collectors.toList;
import static horeilly1101.Expression.Constant.*;

public class Mult implements Expression {
  private List<Expression> factors;

  /**
   * Instantiates a Mult. Avoid using as much as possible! Use the easy constructor
   * instead.
   *
   * Data definition: a term is a list of Expressions (factors). This is analogous
   * to the factors in an expression.
   */
  private Mult(List<Expression> factors) {
    // important check
    // mostly for debugging
    if (factors.size() < 2) {
      throw new RuntimeException("Mult was created with less than two factors!");
    }

    this.factors = factors;
  }

  /**
   * Use this to create a mult object.
   */
  static Expression mult(List<Expression> factors) {
    if (factors.isEmpty()) {
      // bad
      throw new RuntimeException("Don't instantiate a term with an empty list!");
    }

    List<Expression> simplified = simplify(factors).stream()
                                      .sorted().collect(toList());

    // if there is just one factor, there's no use in having a Mult object
    return simplified.size() == 1
               ? simplified.get(0)
               // if it can be a Div object, it should be a Div object
               : divCheck(simplified);
  }

  /**
   * Or use this if you really want to.
   */
  static Expression mult(Expression... factors) {
    return mult(Arrays.asList(factors));
  }

  /**
   * negates an Expression;
   */
  static Expression negate(Expression expr) {
    return mult(constant(-1), expr);
  }

  List<Expression> getFactors() {
    return factors;
  }

  @Override
  public Constant getConstantFactor() {
    List<Expression> constant = factors.stream().filter(Expression::isConstant).collect(toList());
    return constant.isEmpty() ? multID() : constant.get(0).asConstant();
  }

  @Override
  public Expression getSymbolicFactors() {
    return mult(factors.stream().filter(x -> !x.isConstant()).collect(toList()));
  }

  @Override
  public Boolean isNegative() {
    return this.getConstantFactor().isNegative();
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
    return "(" + factors.get(0).toString()
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
    return add(
              mult(
                  factors.get(0),
                  mult(factors.subList(1, factors.size())).differentiate(var)
              ),
              mult(
                  factors.get(0).differentiate(var),
                  mult(factors.subList(1, factors.size()))
              ));
  }

  /*
  Private, static methods to help simplify Mult objects when instantiated.

  The below functions are probably the ugliest ones you'll see in this project,
  but they've been thoroughly tested, and they each serve an important role
  in simplifying mults.
   */

  /**
   * This function brings together all the simplify functions. It runs
   * recursively until there is nothing left to simplify.
   *
   * (Everything but the (possible) recursive call runs in expected linear
   * time.)
   */
  private static List<Expression> simplify(List<Expression> factors) {
    return isSimplified(factors)
               ? factors
               : simplify(
                   simplifyFactors(
                       simplifyConstantFactors(
                           withoutNesting(factors))));
  }

  /**
   * This method simplifies a list of factors by ensuring factors
   * are taken to the proper exponents. (e.g. we want to write x * x
   * as x ^ 2.0.)
   *
   * @return List<Expression> simplified
   */
  private static List<Expression> simplifyFactors(List<Expression> factors) {
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
               .map(key -> power(
                   key,
                   add(powerMap.get(key))))
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
  private static List<Expression> simplifyConstantFactors(List<Expression> factors) {
    // keep track of constant values in the numerator and the denominator
    List<Expression> noConstants = new ArrayList<>();
    int numConstants = 1;
    int denConstants = 1;

    for (Expression factor : factors) {
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
    if (numConstants == 1 && noConstants.isEmpty()) {
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
  private static List<Expression> withoutNesting(List<Expression> factors) {
    List<Expression> newList = new ArrayList<>();

    for (Expression factor : factors) {
      // ensures no nested mults
      if (factor.isMult()) {
        // checked cast
        newList.addAll(factor.asMult().getFactors());

        // ensures no nest divs
      } else if (factor.isDiv()) {
        // checked cast
        newList.add(factor.asDiv().getNumerator());
        Expression den = factor.asDiv().getDenominator();

        if (den.isMult()) {
          // add the denominator factors individually
          newList.addAll(den.asMult().getFactors().stream().map(x -> poly(x, -1)).collect(toList()));
        } else {
          newList.add(poly(den, -1));
        }

      } else {
        newList.add(factor);
      }
    }

    return newList;
  }

  /**
   * This function figures out whether or not the list of factors is
   * fully simplified.
   */
  private static Boolean isSimplified(List<Expression> factors) {
    // we want to make sure there is at most 1 constant in numerator and denominator
    int numCount = 0;
    Constant num = multID();

    int dencount = 0;
    Constant den = multID();

    Set<Expression> bases = new HashSet<>();

    for (Expression fac : factors) {
      if (fac.isConstant()) {
        numCount += 1;
        num = fac.asConstant();
      }

      if (fac.getBase().isConstant() && fac.getExponent().equals(constant(-1))) {
        dencount += 1;
        den = fac.getBase().asConstant();
      }

      // all of these conditions imply factors is not simplified
      if (numCount > 1
              || dencount > 1
              || gcf(num.getVal(), den.getVal()) != 1
              || (fac.equals(addID()) && factors.size() > 1)
              || fac.isMult()
              || bases.contains(fac.getBase())
              || fac.isDiv()) {
        return false;
      }

      bases.add(fac.getBase());
    }

    // ensure there aren't extraneous 1 multiples
    return !(bases.contains(multID()) && dencount != factors.size() - 1);
  }

  /**
   * This method checks if any of the factors have a negative exponent.
   * If they do, it creates a Div with these factors in the denominator,
   * with positive exponents. If they don't, it creates a Mult.
   */
  private static Expression divCheck(List<Expression> factors) {
    List<Expression> num = new ArrayList<>();
    List<Expression> den = new ArrayList<>();

    for (Expression factor : factors) {
      if (factor.getExponent().isNegative()) {
        den.add(power(factor.getBase(), negate(factor.getExponent())));

      } else {
        num.add(factor);
      }
    }

    // no factors in numerator
    if (num.isEmpty()) {
      num.add(multID());
    }

    // no factors in denominator
    if (den.isEmpty()) {
      den.add(multID());
    }

    // factors aren't a div
    if (den.size() == 1 && den.get(0).equals(multID())) {
      return new Mult(num);
    }

    return den.size() == 1 && den.get(0).equals(multID())
               ? new Mult(num)
               : new Div(lengthCheck(num), lengthCheck(den));
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

  /**
   * This method checks whether or not a list of factors has more than
   * one element. If it does, then it creates a mult. If it has just one
   * object, it just returns the object.
   */
  private static Expression lengthCheck(List<Expression> factors) {
    return factors.size() == 1 ? factors.get(0) : new Mult(factors);
  }


}