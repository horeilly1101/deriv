package com.deriv.calculator;

import com.deriv.expression.Expression;
import com.deriv.parser.Parser;

import java.util.Optional;

public class Calculator {
  private String inputString;

  Calculator(String inputString) {
    this.inputString = inputString;
  }

  public Optional<Expression> toOExpression() {
    return new Parser(inputString).parse();
  }

  public Optional<Expression> differentiate(String wrt) {
    return toOExpression().map(y -> y.differentiate(wrt));
  }

  public Optional<Expression> evaluate(String var, String val) {

  }
}
