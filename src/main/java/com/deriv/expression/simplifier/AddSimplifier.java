package com.deriv.expression.simplifier;

import com.deriv.expression.Expression;
import java.util.*;
import static com.deriv.expression.Constant.addID;
import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Mult.mult;
import static java.util.stream.Collectors.toList;
import static com.deriv.expression.Add.*;

/**
 * AddSimplifier exists to help us simplify and make consistent
 * the construction of Add objects.
 */
public abstract class AddSimplifier implements Simplifier {
  protected List<Expression> unTerms;

  public AddSimplifier(List<Expression> unTerms) {
    this.unTerms = unTerms;
  }

  public abstract Expression toExpression();

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
  public void simplify() {
    if (unTerms.size() > 1 && !this.isSimplified()) {
      // run through our simplification procedures
      withoutNesting();
      simplifyConstantTerms();
      simplifyTerms();

      // repeat until fully simplified
      simplify();
    }
  }

  /**
   * This method adds terms with common factors. (e.g. It would take
   * x + 2x and output 3 * x.)
   */
  private void simplifyTerms() {
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
    this.unTerms = powerMap.keySet().stream()
                                .map(key -> mult(
                                  key,
                                  add(powerMap.get(key))))
                                .collect(toList());
  }

  /**
   * This method adds up the values of constant elements of unTerms.
   */
  private void simplifyConstantTerms() {
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

    this.unTerms = noConstants;
  }

  /**
   * This function ensures there is no nesting in unTerms. (i.e. none
   * of its elements are Add objects.)
   */
  private void withoutNesting() {
    List<Expression> newList = new ArrayList<>();

    for (Expression term : unTerms) {
      if (term.isAdd()) {
        // checked cast
        newList.addAll(term.asAdd().getTerms());
      } else {
        newList.add(term);
      }
    }

    this.unTerms = newList;
  }
}