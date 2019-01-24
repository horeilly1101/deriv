package com.deriv.calculator;

import com.deriv.expression.AExpression;
import com.deriv.expression.Expression;
import com.deriv.expression.Variable;
import com.deriv.parser.Parser;
import com.deriv.util.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows the use to differentiate and evaluate arbitrary functions just
 * by inputting in a string representation of an expression.
 */
public class Calculator {
  private final Map<String, Optional<Expression>> expressionCache = new ConcurrentHashMap<>();
  private final Map<String, Optional<Variable>> variableCache = new ConcurrentHashMap<>();
  private final Map<Tuple<Expression, Variable>, Optional<Expression>>
    differentiateCache = new ConcurrentHashMap<>();
  private final Map<ThreeTuple<Expression, Variable, Expression>, Optional<Expression>>
    evaluateCache = new ConcurrentHashMap<>();

  /**
   * Constructor for our Calculator.
   */
  Calculator() { }

  /**
   * Simplifies the given String representation of an expression.
   *
   * @param inputString String representation of expression
   * @return an optional of the resulting string
   */
  public Optional<String> simplify(String inputString) {
    return toOExpression(inputString).map(Expression::toString);
  }

  /**
   * Parses the input String and returns an Optional of Expression.
   *
   * @return an Optional of the Expression
   */
  public Optional<Expression> toOExpression(String inputString) {
    return expressionCache.computeIfAbsent(inputString, str -> new Parser(str).parse());
  }

  /**
   * Differentiates the input String with respect to the input variable
   * and returns an Optional of Expression.
   *
   * @param wrt input variable
   * @return an Optional of the resulting Expression
   */
  public Optional<Expression> differentiate(String expressionString, String wrt) {
    return new Parser(wrt).parseVariable() // parse the variable
             .flatMap(var -> toOExpression(expressionString) // parse the expression
                               .map(ex -> ex.differentiate(var))); // differentiate
  }

  /**
   * Returns an optional list of the steps taken to differentiate the given string representation
   * of an expression.
   *
   * @param wrt the variable
   * @return
   */
  public Optional<List<AExpression.Tuple>> derivativeSteps(String expressionString, String wrt) {
    return differentiate(expressionString, wrt).map(Expression::getSteps);
  }

  public Optional<Expression> evaluate(String expressionString, String var, String val) {
    return new Parser(var).parseVariable() // parse variable
             .flatMap(vr -> new Parser(val).parse() // parse value
                              .flatMap(vl -> toOExpression(expressionString) // parse expression
                                               .flatMap(ex -> ex.evaluate(vr, vl)))); // evaluate
  }
}