package com.deriv.calculator;

import com.deriv.expression.AExpression.*;
import com.deriv.expression.Expression;
import com.deriv.expression.Variable;
import com.deriv.expression.cmd.CacheCmd;
import com.deriv.expression.cmd.ICacheCmd;
import com.deriv.expression.cmd.IStepCmd;
import com.deriv.expression.cmd.StepCmd;
import com.deriv.parser.Parser;
import com.deriv.util.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows us to differentiate and evaluate arbitrary functions just by inputting in a string
 * representation of an expression. Implemented with memoization, so recomputing values should
 * take a constant amount of time.
 */
public class Calculator {
  /**
   * Map to memoize expression parsing.
   */
  private final Map<String, Optional<Expression>> expressionCache = new ConcurrentHashMap<>();

  /**
   * Map to memoize variable parsing.
   */
  private final Map<String, Optional<Variable>> variableCache = new ConcurrentHashMap<>();

  /**
   * Map to memoize derivative computing.
   */
  private final Map<Tuple<Expression, Variable>, Expression>
    differentiateCache = new ConcurrentHashMap<>();

  /**
   * Map to memoize evaluation computing.
   */
  private final Map<ThreeTuple<Expression, Variable, Expression>, Optional<Expression>>
    evaluateCache = new ConcurrentHashMap<>();

  /**
   * Map to memoize the steps taken to differentiate functions.
   */
  private final Map<Expression, IStepCmd> stepCache = new ConcurrentHashMap<>();

  /**
   * Constructor for our Calculator.
   */
  public Calculator() { }

  /**
   * Simplifies the given String representation of an expression.
   *
   * @param inputString String representation of expression
   * @return an optional of the resulting string
   */
  public Optional<Expression> simplify(String inputString) {
    return toOExpression(inputString);
  }

  /**
   * Parses the input String and returns an Optional of Expression.
   *
   * @return an Optional of the Expression
   */
  public Optional<Expression> toOExpression(String inputString) {
    // only parse if the given expression has not already been parsed to an expression
    return expressionCache.computeIfAbsent(inputString, str -> new Parser(str).parse());
  }

  /**
   * Parses the input String and returns an Optional of Variable.
   *
   * @return an Optional of the Variable
   */
  public Optional<Variable> toOVariable(String inputString) {
    // only parse if the given string has not already been parsed to a variable
    return variableCache.computeIfAbsent(inputString, str -> new Parser(str).parseVariable());
  }



  /**
   * Differentiates the input String with respect to the input variable
   * and returns an Optional of Expression.
   *
   * @param wrt input variable
   * @return an Optional of the resulting Expression
   */
  public Optional<Expression> differentiate(String expressionString, String wrt) {
    ICacheCmd cacheCmd = new CacheCmd();
    IStepCmd stepCmd = new StepCmd();
    return toOVariable(wrt) // parse the variable
             .flatMap(var -> toOExpression(expressionString) // parse the expression
                               .map(ex -> {
                                 Expression result = differentiateCache.computeIfAbsent(
                                   Tuple.of(ex, var), // create tuple to store computation
                                   tup -> tup.getFirstItem().differentiate(tup.getSecondItem(), cacheCmd, stepCmd)); // take derivative
                                 differentiateCache.putAll(cacheCmd.getStorage()); // store all derivatives in cache
                                 stepCache.put(ex, stepCmd); // store the steps in the cache
                                 return result;
                               }));
  }

  /**
   * Returns an optional list of the steps taken to differentiate the given string representation
   * of an expression.
   *
   * @param wrt the variable
   * @return an optional list of the steps
   */
  public Optional<List<Tuple<Step, Expression>>> derivativeSteps(String expressionString, String wrt) {
    return null;
//    return toOVariable(wrt)
//             .flatMap(var -> toOExpression(expressionString)
//               .map(ex -> stepCache.computeIfAbsent(ex, given -> {
//                 ICacheCmd cacheCmd = new CacheCmd();
//                 IStepCmd stepCmd = new StepCmd();
//                 differentiateCache.computeIfAbsent(
//                   Tuple.of(ex, var), // create tuple to store computation
//                   tup -> tup.getFirstItem().differentiate(tup.getSecondItem(), cacheCmd, stepCmd)); // take derivative
//                 differentiateCache.putAll(cacheCmd.getStorage()); // store all derivatives in cache
//                 stepCache.put(ex, stepCmd); // store the steps in the cache
//                 return stepCmd;
//             }).getSteps())); // get the computed steps
  }

  /**
   * Evaluates the given expression at the given variable with the given value and returns an Optional
   * of Expression.
   *
   * @param expressionString String representation of Expression
   * @param var input variable
   * @param val input value
   * @return an Optional of the resulting expression
   */
  public Optional<Expression> evaluate(String expressionString, String var, String val) {
    return toOVariable(var) // parse variable
             .flatMap(vr -> toOExpression(val) // parse value
               .flatMap(vl -> toOExpression(expressionString) // parse expression
                 .flatMap(ex -> evaluateCache.computeIfAbsent( // check cache for computation
                   ThreeTuple.of(ex, vr, vl), // create tuple to store computation
                   tup -> tup.getFirstItem().evaluate( // evaluate
                     tup.getSecondItem(), // plug in var
                     tup.getThirdItem()))))); // plug in val
  }
}