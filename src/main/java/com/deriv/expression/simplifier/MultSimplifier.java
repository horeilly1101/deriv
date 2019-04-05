package com.deriv.expression.simplifier;

import com.deriv.expression.Constant;
import com.deriv.expression.Expression;
import java.util.*;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.addID;
import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Power.poly;
import static com.deriv.expression.Power.power;
import static java.util.stream.Collectors.toList;

/**
 Private, static class to help simplify Mult objects when instantiated.

 The below methods are probably the ugliest ones you'll see in this project,
 but they've been thoroughly tested, and they each serve an important role
 in simplifying mults.
 */
public abstract class MultSimplifier implements Simplifier {
  protected List<Expression> unFactors;

  public MultSimplifier(List<Expression> unFactors) {
    this.unFactors = unFactors;
  }

  /**
   * This method checks 2 things. First, it checks if factors is just a list of a constant and an Add.
   * If this is the case, we want to distribute the constant among the terms and return an Add.
   * Second, it checks whether or not a list of factors has more than one element. If it does, then it
   * creates a mult. If it has just one object, it just returns the object.
   */
  public abstract Expression toExpression();

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
  public void simplify() {
    if (!isSimplified()) {
      // run through our simplification procedures
      withoutNesting();
      simplifyConstantFactors();
      simplifyFactors();

      // repeat until fully simplified
      simplify();
    }
  }

  /**
   * This method simplifies a list of factors by ensuring factors
   * are taken to the proper exponents. (e.g. we want to write x * x
   * as x ^ 2.0.)
   */
  private void simplifyFactors() {
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
    this.unFactors = powerMap.keySet().stream()
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
   * This method also simplifies constants in the numerator and denominator
   * by dividing each by the greatest common factor. (Check out Euclid's
   * algorithm down below.)
   */
  private void simplifyConstantFactors() {
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

    this.unFactors = noConstants;
  }

  /**
   * This method simplifies a list of factors by taking advantage of
   * the associativity of multiplication. (i.e. a Mult object multiplied
   * by a Mult object should not yield a Mult object of two Mult objects.
   * It should yield a Mult object of whatever was in the original objects,
   * flatmapped together.)
   */
  private void withoutNesting() {
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

    this.unFactors = newList;
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