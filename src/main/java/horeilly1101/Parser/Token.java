package horeilly1101.Parser;

import horeilly1101.Parser.Scanner.*;

/**
 * This is just a simple class to hold token names and data.
 * It makes our lives easier while parsing.
 */
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