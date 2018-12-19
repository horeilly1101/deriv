package horeilly1101;

import com.sun.istack.internal.NotNull;

import java.util.*;
import java.lang.Math;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

interface Expression extends Comparable {
  /**
   * Plugs an input into the value of whatever variable is
   * in the function, and then evaluates the rest of the
   * expression.
   *
   * @return Double solution
   */
  Double evaluate(Double input);

  /**
   * Takes the derivative of the given expression.
   *
   * @return Expression derivative
   */
  Expression differentiate();

  /**
   * This method compares an expression with a given object. This
   * is important, as it allows us to define an ordering on our
   * data structures.
   */
  default int compareTo(@NotNull Object o) {
    // constants come first
    if (this.getClass().equals(Constant.class)) {
      return -1;
    } else if (o.getClass().equals(Constant.class)) {
      return 1;
    }

    return this.toString().compareTo(o.toString());
  }

  default Mult asMult() {
    return (Mult) this;
  }

  default Add asAdd() {
    return (Add) this;
  }

//  default Power asPower() {
//    return (Power) this;
//  }
//
//  default Double getPower() {
//    throw new RuntimeException();
//  }

  default Constant getConstant() {
    if (this.getClass().equals(Mult.class)) {
      List<Expression> cons = this.asMult().factors.stream()
                                  .filter(x -> x.getClass().equals(Constant.class))
                                  .collect(toList());
      return cons.isEmpty() ? new Constant(1.0) : (Constant) cons.get(0);
    } else {
      return new Constant(1.0);
    }
  }

  default Expression getFactora() {
    return this.getClass().equals(Mult.class)
               ? Mult.mult(this.asMult().factors.stream()
                               .filter(x -> !x.getClass().equals(Constant.class))
                               .collect(toList()))
               : this;
  }

  class Mult implements Expression {
    private List<Expression> factors;

    /**
     * Instantiates a Term. Avoid using as much as possible! Use the easy constructor
     * instead.
     *
     * Data definition: a term is a list of Expressions (factors). This is analogous
     * to the factors in an expression.
     */
    private Mult(List<Expression> factors) {
      this.factors = factors;
    }

    static Expression mult(List<Expression> factors) {
      if (factors.isEmpty()) {
        throw new RuntimeException("Don't instantiate a term with an empty list!");
      } else {
        // we don't want terms of one object
        List<Expression> simplified = simplify(factors);
        simplified.sort(Expression::compareTo);
        return simplified.size() > 1 ? new Mult(simplified) : simplified.get(0);
      }
    }

    static Expression mult(Expression... factors) {
      return mult(Arrays.asList(factors));
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
                 : factors.get(0).toString()
                       + factors.subList(1, factors.size()).stream() //  sublist is O(1)
                             .map(Expression::toString)
                             .reduce("", (a, b) -> a + " * " + b);
    }

    public Double evaluate(Double input) {
      // multiplies factors together
      return factors.stream()
                 .map(x -> x.evaluate(input))
                 .reduce(1.0, (a, b) -> a * b);
    }

    static List<Expression> simplify(List<Expression> factors) {
      return simplifyFactors(simplifyConstants(withoutNesting(factors)));
    }

    /**
     * This method simplifies a list of factors by ensuring factors
     * are taken to the proper exponents. (e.g. we want to write x * x
     * as x ^ 2.0.)
     *
     * @return List<Expression> simplified
     */
    static List<Expression> simplifyFactors(List<Expression> factors) {
      List<Power> powers = factors.stream()
                               .map(x -> x.getClass().equals(Power.class) ? (Power) x : Power.polyUnsimplified(x))
                               .collect(toList());

      HashMap<Expression, List<Double>> powerMap = new HashMap<>();

      for (Power pow : powers) {
        if (powerMap.containsKey(pow.base)) {
          List<Double> newList = powerMap.get(pow.base);
          newList.add(pow.exponent);
          powerMap.replace(pow.base, newList);
        } else {
          List<Double> newList = new ArrayList<>();
          newList.add(pow.exponent);
          powerMap.put(pow.base, newList);
        }
      }

      // add up the exponents
      return powerMap.keySet().stream()
                 .map(key -> Power.poly(
                     key,
                     powerMap.get(key).stream()
                         .reduce(0.0, (a, b) -> a + b)))
                 .collect(toList());
    }

    /**
     * This method simplifies a list of factors to get rid of extraneous
     * constant factors. (e.g. multipling an expression by 1 should yield
     * the expression, multiplying an expression by 0 should yield zero,
     * and so on.)
     *
     * @return List<Expression> simplified
     */
    static List<Expression> simplifyConstants(List<Expression> factors) {
      // keep track of constants' values
      List<Expression> noConstants = new ArrayList<>();
      Double constants = 1.0;

      for (Expression factor : factors) {
        if (factor.getClass().equals(Constant.class)) {
          // checked cast
          constants *= ((Constant) factor).val;
        } else {
          noConstants.add(factor);
        }
      }

      // multiplicative identity?
      if (constants == 1.0 && noConstants.isEmpty()) {
        noConstants.add(Constant.constant(1.0));
        // zero?
      } else if (constants == 0.0) {
        // all factors go to zero
        noConstants.clear();
        noConstants.add(Constant.constant(0.0));
      } else if (constants != 1.0) {
        noConstants.add(Constant.constant(constants));
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
    static List<Expression> withoutNesting(List<Expression> factors) {
      List<Expression> newList = new ArrayList<>();

      for (Expression factor : factors) {
        if (factor.getClass().equals(Mult.class)) {
          // checked cast
          newList.addAll(factor.asMult().factors);
        } else {
          newList.add(factor);
        }
      }

      return newList;
    }

    public Expression differentiate() {
      return factors.size() == 1
                 ? factors.get(0).differentiate()
                 // product rule
                 : Add.add(
                     mult(
                         factors.get(0),
                         mult(factors.subList(1, factors.size())).differentiate()
                     ),
                     mult(
                         factors.get(0).differentiate(),
                         mult(factors.subList(1, factors.size()))
                     ));
    }
  }

  class Add implements Expression {
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
        List<Expression> simplified = simplify(terms);
        // reverse sort
        simplified.sort(Comparator.reverseOrder());
        return simplified.size() > 1 ? new Add(simplified) : terms.get(0);
      }
    }

    static Expression add(Expression... terms) {
      return add(Arrays.asList(terms));
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

    public Double evaluate(Double input) {
      // adds terms together
      return terms.stream()
                 .map(x -> x.evaluate(input))
                 .reduce(0.0, (a, b) -> a + b);
    }

    static List<Expression> simplify(List<Expression> terms) {
      return simplifyTerms(simplifyConstants(withoutNesting(terms)));
//      return simplifyConstants(withoutNesting(terms));
    }

    /**
     * This method simplifies a list of terms by ensuring terms
     * are taken to the proper constants. (e.g. we want to write x + x
     * as 2.0 * x.)
     *
     * @return List<Expression> simplified
     */
    static List<Expression> simplifyTerms(List<Expression> terms) {

      HashMap<Expression, List<Double>> powerMap = new HashMap<>();

      for (Expression term : terms) {
        if (powerMap.containsKey(term.getFactora())) {
          List<Double> newList = powerMap.get(term.getFactora());
          newList.add(term.getConstant().val);
          powerMap.replace(term.getFactora(), newList);
        } else {
          List<Double> newList = new ArrayList<>();
          newList.add(term.getConstant().val);
          powerMap.put(term.getFactora(), newList);
        }
      }

      // add up the constants
      return powerMap.keySet().stream()
                 .map(key -> Mult.mult(
                     key,
                     Constant.constant(powerMap.get(key).stream()
                                           .reduce(0.0, (a, b) -> a + b))))
                 .collect(toList());
    }

    /**
     * This method simplifies a list of factors to get rid of extraneous
     * constant factors. (e.g. multipling an expression by 1 should yield
     * the expression, multiplying an expression by 0 should yield zero,
     * and so on.)
     *
     * @return List<Expression> simplified
     */
    static List<Expression> simplifyConstants(List<Expression> factors) {
      // keep track of constants' values
      List<Expression> noConstants = new ArrayList<>();
      Double constants = 0.0;

      for (Expression factor : factors) {
        if (factor.getClass().equals(Constant.class)) {
          // checked cast
          constants += ((Constant) factor).val;
        } else {
          noConstants.add(factor);
        }
      }

      // multiplicative identity?
      if (constants == 0.0 && noConstants.isEmpty()) {
        noConstants.add(Constant.constant(0.0));
        // zero?
      } else if (constants != 0.0) {
        noConstants.add(Constant.constant(constants));
      }

      return noConstants;
    }

    /**
     * This method simplifies a list of terms by taking advantage of
     * the associativity of addition. (i.e. a Mult object multiplied
     * by a Mult object should not yield a Mult object of two Mult objects.
     * It should yield a Mult object of whatever was in the original objects,
     * flatmapped together.)
     *
     * @return List<Expression> simplified
     */
    static List<Expression> withoutNesting(List<Expression> terms) {
      List<Expression> newList = new ArrayList<>();

      for (Expression term : terms) {
        if (term.getClass().equals(Add.class)) {
          // checked cast
          newList.addAll(term.asAdd().terms);
        } else {
          newList.add(term);
        }
      }

      return newList;
    }

    public Expression differentiate() {
      // linearity of differentiation
      return add(terms.stream()
                     .map(Expression::differentiate)
                     .collect(toList()));
    }
  }

  class Power implements Expression {
    private Expression base;
    // polynomial for now
    private Double exponent;

    /**
     * Instantiates a Power. Avoid using as much as possible! Use the easy constructor
     * instead. (A power is the more general form of a polynomial and an exponential.)
     *
     * Data definition: a power is a base and an exponent.
     */
    private Power(Expression base, Double exponent) {
      this.base = base;
      this.exponent = exponent;
    }

    static Power polyUnsimplified(Expression base) {
      return new Power(base, 1.0);
    }

    static Expression poly(Expression base, Double exponent) {
      return simplify(base, exponent);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      } else if (!(o instanceof Power)) {
        return false;
      }

      Power pow = (Power) o;
      return pow.base.equals(this.base) && pow.exponent.equals(this.exponent);
    }

    @Override
    public int hashCode() {
      return this.toString().hashCode() + 12;
    }

    @Override
    public String toString() {
      return "(" + base.toString() + ") ^ (" + exponent.toString() + ")";
    }

    public Double evaluate(Double input) {
      return Math.pow(base.evaluate(input), exponent);
    }

    static Expression simplify(Expression base, Double exponent) {
      if (exponent == 1.0) {
        return base;
      } else if (exponent == 0.0) {
        return Constant.constant(0.0);
      } else {
        return new Power(base, exponent);
      }
    }

    public Expression differentiate() {
      // assume polynomial for now
      // power rule
      return exponent == 1.0
                 ? new Constant(1.0)
                 : Mult.mult(Constant.constant(exponent),
                     base.differentiate(),
                     poly(base, exponent - 1));
    }
  }

  class Trig implements Expression {

    // maps to ensure cleaner code (i.e. no long if statements)
    private Map<String, Function<Double, Double>> evalMap = new TreeMap<>();
    private Map<String, Function<Trig, Expression>> derivMap = new TreeMap<>();

    private String func;
    private Expression inside;

    private Trig(String func, Expression inside) {
      this.func = func;
      this.inside = inside;

      // define functions for evaluating expressions
      evalMap.put("sin", Math::sin);
      evalMap.put("cos", Math::cos);
      evalMap.put("tan", Math::tan);
      evalMap.put("csc", x -> 1.0 / Math.sin(x));
      evalMap.put("sec", x -> 1.0 / Math.cos(x));
      evalMap.put("cot", x -> 1.0 / Math.tan(x));

      // define functions for evaluating derivatives
      derivMap.put("sin",
          x -> Mult.mult(
              x.inside.differentiate(),
              cos(inside)));

      derivMap.put("cos",
          x -> Mult.mult(
              Constant.constant(-1.0),
              x.inside.differentiate(),
              sin(inside)));

      derivMap.put("tan",
          x -> Mult.mult(
              x.inside.differentiate(),
              Power.poly(
                  sec(inside),
                  2.0)));

      derivMap.put("csc",
          x -> Mult.mult(
              Constant.constant(-1.0),
              x.inside.differentiate(),
              csc(inside),
              cot(inside)));

      derivMap.put("sec",
          x -> Mult.mult(
              x.inside.differentiate(),
              sec(inside),
              tan(inside)));

      derivMap.put("cot",
          x -> Mult.mult(
              Constant.constant(-1.0),
              x.inside.differentiate(),
              Power.poly(
                  csc(inside),
                  2.0)));
    }

    static Expression sin(Expression inside) {
      return new Trig("sin", inside);
    }

    static Expression cos(Expression inside) {
      return new Trig("cos", inside);
    }

    static Expression tan(Expression inside) {
      return new Trig("tan", inside);
    }

    static Expression csc(Expression inside) {
      return new Trig("csc", inside);
    }

    static Expression sec(Expression inside) {
      return new Trig("sec", inside);
    }

    static Expression cot(Expression inside) {
      return new Trig("cot", inside);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      } else if (!(o instanceof Trig)) {
        return false;
      }

      Trig tri = (Trig) o;
      return tri.func.equals(this.func) && tri.inside.equals(this.inside);
    }

    @Override
    public int hashCode() {
      return this.toString().hashCode() + 10;
    }

    @Override
    public String toString() {
      return this.func + "(" + this.inside.toString() + ")";
    }

    public Double evaluate(Double val) {
      return evalMap.get(this.func).apply(val);
    }

    public Expression differentiate() {
      return derivMap.get(this.func).apply(this);
    }

  }

  class Constant implements Expression {
    private Double val;

    private Constant(Double val) {
      this.val = val;
    }

    static Expression constant(Double val) {
      return new Constant(val);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      } else if (!(o instanceof Constant)) {
        return false;
      }

      Constant con = (Constant) o;
      return con.val.equals(this.val);
    }

    @Override
    public int hashCode() {
      return this.toString().hashCode() + 11;
    }

    @Override
    public String toString() {
      return val.toString();
    }

    public Double evaluate(Double input) {
      return val;
    }

    public Expression differentiate() {
      return constant(0.0);
    }
  }

  class Variable implements Expression {
    // univariate for now
    private Variable() { }

    static Expression var() {
      return new Variable();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      } else if (!(o instanceof Variable)) {
        return false;
      }

      Variable var = (Variable) o;
      return var.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
      return this.toString().hashCode() + 12;
    }

    @Override
    public String toString() {
      return "x";
    }

    public Double evaluate(Double input) {
      return input;
    }

    public Expression differentiate() {
      return Constant.constant(1.0);
    }
  }
}