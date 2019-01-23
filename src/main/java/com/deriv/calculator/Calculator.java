package com.deriv.calculator;

import com.deriv.expression.AExpression;
import com.deriv.expression.Expression;
import com.deriv.parser.Parser;

import java.util.List;
import java.util.Optional;

public class Calculator {
  private Optional<Expression> oExpression;

  Calculator(String inputString) {
    this.oExpression = new Parser(inputString).parse();
  }

  Calculator(Optional<Expression> oExpression) {
    this.oExpression = oExpression;
  }

  @Override
  public String toString() {
    if (oExpression.isPresent()) {
      return oExpression.get().toString();
    }
    return "";
  }

  public Optional<Expression> toOExpression() {
    return oExpression;
  }

  public Calculator differentiate(String wrt) {
    return new Calculator(new Parser(wrt).parseVariable().flatMap(var -> oExpression.map(ex -> ex.differentiate(var))));
  }

  public Optional<List<AExpression.Tuple>> derivativeSteps(String wrt) {
    return this.differentiate(wrt).oExpression.map(Expression::getSteps);
  }

  public Calculator evaluate(String var, String val) {
    return new Calculator(
      new Parser(var).parseVariable()
        .flatMap(vr -> new Parser(val).parse()
                         .flatMap(vl -> oExpression
                                          .flatMap(ex -> ex.evaluate(vr, vl)))));
  }
}