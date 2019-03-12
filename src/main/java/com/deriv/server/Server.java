package com.deriv.server;

import com.deriv.calculator.Calculator;
import com.deriv.expression.Expression;
import com.deriv.expression.Variable;
import com.deriv.util.Tuple;
import org.json.JSONObject;
import org.json.JSONArray;
import spark.Response;
import java.util.Optional;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Server {
  /**
   * An easy constructor for an empty JSON Object. Helps clean up
   * the code a little bit.
   */
  static JSONObject jobject() {
    return new JSONObject();
  }

  /**
   * Returns a JSON object error message.
   */
  private static  JSONObject error(Response res) {
    res.status(400); // client error
    return jobject()
              .put("error", "invalid input(s)");
  }

  /**
   * Returns a JSON object corresponding to the differentiate route.
   */
  static JSONObject diffObject(Expression result, Expression expr, Variable var) {
    return jobject()
              .put("data",
                  jobject()
                      // the gets are checked, because oDeriv is checked
                      .put("expression", expr)
                      .put("result", result)
                      .put("var", var)
                      .put("steps", new JSONArray(result.getSteps().stream()
                                                    .map(Tuple::toString)
                                                    .collect(Collectors.toList())))
              );
  }

  /**
   * Returns a JSON object corresponding to the evaluate route.
   */
  static JSONObject evalObject(Expression result, Expression expr, Variable var, Expression val) {
    return jobject()
             .put("data",
               jobject()
                 // the gets are checked, because oEval is checked
                 .put("expression", expr.toString())
                 .put("result", result.toString())
                 .put("var", var.toString())
                 .put("val", val.toString())
             );
  }

  /**
   * Returns a JSON object corresponding to the simplify route.
   */
  static JSONObject simplifyObject(Expression result, String input) {
    return jobject()
             .put("data",
               jobject()
                 // the gets are checked, because oEval is checked
                 .put("input", input)
                 .put("result", result.toString())
             );
  }

  // runs the server on localhost:4567
  public static void main(String[] args) {
    // initialize calculator
    Calculator calc = new Calculator();

    // the GET call that differentiates an expression
    get("/differentiate/:expr/:var", (req, res) -> {
      // url variables
      String expr = req.params(":expr");
      String var = req.params(":var");

      return calc.differentiate(expr, var) // differentiate
               .map(result -> diffObject(result, calc.toOExpression(expr).get(), calc.toOVariable(var).get()))
               .orElse(error(res)); // error message, if unsuccessful
    });

    // the GET call that evaluates an expression
    get("/evaluate/:expr/:var/:val", (req, res) -> {
      // url variables
      String expr = req.params(":expr");
      String var = req.params(":var");
      String val = req.params(":val");

      return calc.evaluate(expr, var, val) // evaluate
               .map(result -> evalObject( // create json object
                 result,
                 calc.toOExpression(expr).get(),
                 calc.toOVariable(var).get(),
                 calc.toOExpression(val).get()))
               .orElse(error(res)); // return an error if unsuccessful
    });

    get("/simplify/:expr", (req, res) -> {
      // expression from url
      String expr = req.params(":expr");

      // simplify and return JSON object
      return calc.simplify(expr).map(result -> simplifyObject(result, expr)).orElse(error(res));
    });
  }
}