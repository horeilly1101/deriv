package horeilly1101.Expression;

public class Power implements Expression {
  private Expression base;
  private Expression exponent;

  /**
   * Instantiates a Power. Avoid using as much as possible! Use the easy constructor
   * instead. (A power is the more general form of a polynomial and an exponential.)
   *
   * Data definition: a power is a base and an exponent.
   */
  private Power(Expression base, Expression exponent) {
    this.base = base;
    this.exponent = exponent;
  }

  static Expression power(Expression base, Expression exponent) {
    if (exponent.equals(Constant.multID())) {
      return base;
    }

    return new Power(base, exponent);
  }

  static Expression poly(Expression base, Constant exponent) {
    return power(base, exponent);
  }

  static Expression exponential(Constant base, Expression exponent) {
    return new Power(base, exponent);
  }

  @Override
  public Expression getExponent() {
    return exponent;
  }

  @Override
  public Expression getBase() {
    return base;
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
    return base.toString() + " ^ " + exponent.toString();
  }

  public Expression evaluate(String var, Double input) {
    return power(
        base.evaluate(var, input),
        exponent.evaluate(var, input));
  }

  public Expression differentiate(String var) {
    return Mult.mult(
        power(base, exponent),
        Add.add(
            Mult.mult(
                exponent.differentiate(var), Log.ln(base)),
            Mult.mult(exponent,
                base.differentiate(var),
                power(base, Constant.constant(-1.0)))));
  }
}