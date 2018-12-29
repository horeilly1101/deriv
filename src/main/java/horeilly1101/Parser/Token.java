package horeilly1101.Parser;

import horeilly1101.Parser.Scanner.*;

public class Token {
  SymbolType name;
  String data;

  Token(SymbolType name, String data) {
    this.name = name;
    this.data = data;
  }

  @Override
  public String toString() {
    return "(" + this.name + ", " + this.data + ")";
  }
}