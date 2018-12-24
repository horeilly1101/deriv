package horeilly1101.Expression;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Trig implements Expression {
  // maps to ensure cleaner code (i.e. no long "if" statements)
  private Map<String, Function<Expression, Expression>> evalMap = new TreeMap<>();
  private Map<String, BiFunction<Trig, String, Expression>> derivMap = new TreeMap<>();

  private String func;
  private Expression inside;

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

    // define functions for evaluating expressions
    evalMap.put("sin", Trig::sin);
    evalMap.put("cos", Trig::cos);
    evalMap.put("tan", Trig::tan);
    evalMap.put("csc", Trig::csc);
    evalMap.put("sec", Trig::sec);
    evalMap.put("cot", Trig::cot);

    // define functions for evaluating derivatives
    derivMap.put("sin",
        (x, var) -> Mult.mult(
            x.inside.differentiate(var),
            cos(inside)));

    derivMap.put("cos",
        (x, var) -> Mult.mult(
            Constant.constant(-1.0),
            x.inside.differentiate(var),
            sin(inside)));

    derivMap.put("tan",
        (x, var) -> Mult.mult(
            x.inside.differentiate(var),
            Power.poly(
                sec(inside),
                Constant.constant(2.0))));

    derivMap.put("csc",
        (x, var) -> Mult.mult(
            Constant.constant(-1.0),
            x.inside.differentiate(var),
            csc(inside),
            cot(inside)));

    derivMap.put("sec",
        (x, var) -> Mult.mult(
            x.inside.differentiate(var),
            sec(inside),
            tan(inside)));

    derivMap.put("cot",
        (x, var) -> Mult.mult(
            Constant.constant(-1.0),
            x.inside.differentiate(var),
            Power.poly(
                csc(inside),
                Constant.constant(2.0))));
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

  public Expression evaluate(String var, Double val) {
    return evalMap.get(this.func)
               .apply(inside.evaluate(var, val));
  }

  public Expression differentiate(String var) {
    return derivMap.get(this.func).apply(this, var);
  }
}