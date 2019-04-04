package com.deriv.expression;

import com.deriv.expression.cmd.ICacheCmd;
import com.deriv.expression.cmd.IStepCmd;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Trig extends AExpression {
  private String func;
  private Expression inside;

  // valid trig functions
  private static Set<String> valid = Stream.of("sin", "cos", "tan", "csc", "sec", "cot")
                               .collect(Collectors.toSet());

  /*
   * Below are maps to ensure cleaner code (i.e. no long "if" statements)
   */

  // define functions for evaluating expressions
  private static Map<String, Function<Expression, Expression>> evalMap = new TreeMap<>();
  static {
    evalMap.put("sin", Trig::sin);
    evalMap.put("cos", Trig::cos);
    evalMap.put("tan", Trig::tan);
    evalMap.put("csc", Trig::csc);
    evalMap.put("sec", Trig::sec);
    evalMap.put("cot", Trig::cot);
  }

  // define functions for evaluating derivatives
  private static Map<String, BiFunction<Trig, Variable, Expression>> derivMap = new TreeMap<>();
  static {
    derivMap.put("sin",
      (ex, var) -> {
        // calculate inside derivative
        Expression deriv = ex.inside.differentiate(var);

        return Mult.mult(
          deriv,
          cos(ex.inside));
      });

    derivMap.put("cos",
      (ex, var) -> {
        // calculate inside derivative
        Expression deriv = ex.inside.differentiate(var);

        return Mult.mult(
          Constant.constant(-1),
          deriv,
          sin(ex.inside));
      });

    derivMap.put("tan",
      (ex, var) -> {
        // calculate inside derivative
        Expression deriv = ex.inside.differentiate(var);

        return Mult.mult(
          deriv,
          Power.poly(
            sec(ex.inside),
            2));
      });

    derivMap.put("csc",
      (ex, var) -> {
        // calculate inside derivative
        Expression deriv = ex.inside.differentiate(var);

        return Mult.mult(
          Constant.constant(-1),
          deriv,
          csc(ex.inside),
          cot(ex.inside));
      });

    derivMap.put("sec",
      (ex, var) -> {
        // calculate inside derivative
        Expression deriv = ex.inside.differentiate(var);

        return Mult.mult(
          deriv,
          sec(ex.inside),
          tan(ex.inside));
      });

    derivMap.put("cot",
      (ex, var) -> {
        // calculate inside derivative
        Expression deriv = ex.inside.differentiate(var);

        return Mult.mult(
          Constant.constant(-1),
          deriv,
          Power.poly(
            csc(ex.inside),
            2));
      });
  }

  /**
   * Instantiates a Trig object. Avoid using as much as possible! Instead, use
   * one of the several constructors down below.
   *
   * Data definition: a trig is a function name (e.g. "sin", "cos", etc.) and
   * an input (Expression).
   */
  private Trig(String func, Expression inside) {
    this.func = func;
    this.inside = inside;
  }

  public static Expression trig(String func, Expression inside) {
    if (!valid.contains(func)) {
      throw new RuntimeException("Not a valid trig function!");
    }

    return new Trig(func, inside);
  }

  public static Expression sin(Expression inside) {
    return new Trig("sin", inside);
  }

  public static Expression cos(Expression inside) {
    return new Trig("cos", inside);
  }

  public static Expression tan(Expression inside) {
    return new Trig("tan", inside);
  }

  public static Expression csc(Expression inside) {
    return new Trig("csc", inside);
  }

  public static Expression sec(Expression inside) {
    return new Trig("sec", inside);
  }

  public static Expression cot(Expression inside) {
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
    return this.inside.isMult() || this.inside.isAdd()
               ? this.func + this.inside.toString()
               : this.func + "(" + this.inside.toString() + ")";
  }

  @Override
  public String toLaTex() {
    return "\\" + this.func + "(" + this.inside.toLaTex() + ")";
  }

  public Optional<Expression> evaluate(Variable var, Expression val) {
    return inside.evaluate(var, val)
               .map(x -> evalMap.get(this.func).apply(x));
  }

  public Expression computeDerivative(Variable var, ICacheCmd cacheCmd, IStepCmd stepCmd) {
    return derivMap.get(this.func)
             .apply(this, var);
  }

  public Step getDerivativeStep() {
    return Step.SIN;
  }
}