package com.deriv.expression;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.deriv.expression.Mult.mult;

public class Trig implements Expression {
  /**
   * String that identifies what trig function we're referring to.
   */
  private String _func;

  /**
   * The expression that the trig function operates on.
   */
  private Expression _inside;

  // valid trig functions
  private static Set<String> valid = Stream.of("sin", "cos", "tan", "csc", "sec", "cot")
                               .collect(Collectors.toSet());

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

  /**
   * Instantiates a Trig object. Avoid using as much as possible! Instead, use
   * one of the several constructors down below.
   *
   * Data definition: a trig is a function name (e.g. "sin", "cos", etc.) and
   * an input (Expression).
   */
  private Trig(String _func, Expression _inside) {
    this._func = _func;
    this._inside = _inside;
  }

  public static Expression trig(String func, Expression inside) {
    if (!valid.contains(func)) {
      throw new RuntimeException("Not a valid trig function!");
    }

    return new Trig(func, inside);
  }

  /**
   * Static constructor for the sine of a function.
   * @param inside expr
   * @return sin
   */
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
    if (this == o)
      return true;

    if (!(o instanceof Trig))
      return false;

    Trig tri = (Trig) o;
    return tri._func.equals(this._func) && tri._inside.equals(this._inside);
  }

  @Override
  public int hashCode() {
    return 31 * _func.hashCode() + _inside.hashCode();
  }

  @Override
  public String toString() {
    return this._inside.isMult() || this._inside.isAdd()
               ? this._func + this._inside.toString()
               : this._func + "(" + this._inside.toString() + ")";
  }

  @Override
  public String toLaTex() {
    return "\\" + this._func + "(" + this._inside.toLaTex() + ")";
  }

  public Optional<Expression> evaluate(Variable var, Expression val) {
    return _inside.evaluate(var, val)
               .map(x -> evalMap.get(this._func).apply(x));
  }

  public Optional<Expression> differentiate(Variable var) {
    return _inside.differentiate(var).map(x -> {
      switch (_func) {
        case "sin":
          return mult(x, cos(_inside));

        case "cos":
          return mult(
            Constant.constant(-1),
            x,
            sin(_inside));

        case "tan":
          return mult(
            x,
            Power.poly(
              sec(_inside),
              2));

        case "csc":
          return mult(
            Constant.constant(-1),
            x,
            csc(_inside),
            cot(_inside));

        case "sec":
          return mult(
            x,
            sec(_inside),
            tan(_inside));

        case "cot":
          return mult(
            Constant.constant(-1),
            x,
            Power.poly(
              csc(_inside),
              2));

        default:
          throw new RuntimeException("Why is _func not dealt with in switch statement?");

      }
    });
  }
}