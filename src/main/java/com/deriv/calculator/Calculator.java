package com.deriv.calculator;

import com.deriv.expression.Expression;
import com.deriv.parser.Parser;

import java.util.Optional;

public class Calculator {
  private Optional<Expression> oExpression;

  Calculator(String inputString) {
    this.oExpression = new Parser(inputString).parse();
  }

  @Override
  public String toString() {
    return oExpression.toString();
  }

  public Optional<Expression> toOExpression() {
    return oExpression;
  }

  public Optional<Expression> differentiate(String wrt) {
    return new Parser(wrt).parseVariable().flatMap(var -> oExpression.map(ex -> ex.differentiate(var)));
  }

  public Optional<Expression> evaluate(String var, String val) {
    return new Parser(var).parseVariable()
             .flatMap(vr -> new Parser(val).parse()
                              .flatMap(vl -> oExpression
                                               .flatMap(ex -> ex.evaluate(vr, vl))));
  }
}
