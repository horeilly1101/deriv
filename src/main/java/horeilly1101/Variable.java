package horeilly1101;

public class Variable implements Expression {
  String var;

  Variable(String var) {
    this.var = var;
  }

  static Expression var(String var) {
    if (var.equals("e")) {
      throw new RuntimeException("Variable can't be named e.");
    }

    return new Variable(var);
  }

  static Expression x() {
    return new Variable("x");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Variable)) {
      return false;
    }

    Variable var = (Variable) o;
    return var.var.equals(this.var);
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode() + 12;
  }

  @Override
  public String toString() {
    return var;
  }

  public Expression evaluate(String var, Double input) {
    return var.equals(this.var) ? Constant.constant(input) : this;
  }

  public Expression differentiate(String wrt) {
    return wrt.equals(var) ? Constant.constant(1.0) : Constant.constant(0.0);
  }
}