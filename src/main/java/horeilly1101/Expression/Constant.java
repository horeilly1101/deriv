package horeilly1101.Expression;

public class Constant implements Expression {
  private Double val;

  private Constant(Double val) {
    this.val = val;
  }

  static Constant constant(Double val) {
    return new Constant(val);
  }

  public Double getVal() {
    return val;
  }

  static Constant multID() {
    return constant(1.0);
  }

  static Constant addID() {
    return constant(0.0);
  }

  static Expression e() {
    return new Variable("e");
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

  public Expression evaluate(String var, Double input) {
    return this;
  }

  public Expression differentiate(String var) {
    return constant(0.0);
  }
}