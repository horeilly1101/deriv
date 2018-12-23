package horeilly1101;

public class Log implements Expression {
  private Expression base;
  private Expression result;

  private Log(Expression base, Expression result) {
    this.base = base;
    this.result = result;
  }

  static Expression log(Expression base, Expression result) {
    return new Log(base, result);
  }

  static Expression ln(Expression result) {
    return new Log(Constant.e(), result);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Log)) {
      return false;
    }

    Log log = (Log) o;
    return log.base.equals(this.base) && log.result.equals(this.result);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 8;
  }

  @Override
  public String toString() {
    return "log(" + base.toString() + ", " + result.toString() + ")";
  }

  public Expression evaluate(String var, Double val) {
    return log(base.evaluate(var, val), result.evaluate(var, val));
  }

  public Expression differentiate(String var) {
    return Mult.mult(
        Add.add(
            Mult.mult(
                base,
                result.differentiate(var),
                log(Constant.e(), base)),
            Mult.mult(
                Constant.constant(-1.0),
                result,
                base.differentiate(var),
                log(Constant.e(), result))),

        Power.poly(
            Mult.mult(
                result,
                base,
                Power.poly(
                    log(Constant.e(),
                        base),
                    Constant.constant(2.0))),
            Constant.constant(-1.0)));
  }
}
