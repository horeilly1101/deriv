package horeilly1101.Expression;

public class Variable implements Expression {
  String var;

  /**
   * This method is only package-private (because I want to use it
   * to create the constant e in Constant), but you still should
   * avoid using it to instantiate Variable objects. Use the easy
   * constructor below instead.
   *
   * Data definition: a variable is a string name (e.g. "x", "y",
   * etc.).
   */
  Variable(String var) {
    this.var = var;
  }

  /**
   * Use this method to instantiate a Variable object. You can't create
   * a variable named "e" because that's a really important constant in
   * calculus, and we don't want to create any problems.
   */
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
    return var.equals(this.var) ? toConstant(input) : this;
  }

  public Expression differentiate(String wrt) {
    return wrt.equals(var) ? Constant.multID() : Constant.addID();
  }

  private static Expression toConstant(Double input) {
    return div();
  }
}