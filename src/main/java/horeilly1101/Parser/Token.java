package horeilly1101.Parser;

public class Token {
  String name;
  String data;

  Token(String name, String data) {
    this.name = name;
    this.data = data;
  }

  @Override
  public String toString() {
    return "(" + this.name + ", " + this.data + ")";
  }
}
