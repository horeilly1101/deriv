package com.deriv.server;

import com.deriv.calculator.Calculator;
import com.deriv.expression.Expression;
import com.deriv.expression.Variable;
import org.json.JSONObject;
import spark.Filter;
import spark.Request;
import spark.Response;

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
  static  JSONObject error(Response res) {
    res.status(400); // client error
    return jobject().put("error", "invalid input(s)");
  }

  static JSONObject getDerivative(Request req, Response res, Calculator calc) {
    // url variables
    String expr = req.params(":expr");
    String var = req.params(":var");

    return calc.differentiate(expr, var) // differentiate
             .map(result -> diffObject(
               result,
               calc.toOExpression(expr).get(),
               calc.toOVariable(var).get()))
             .orElseGet(() -> error(res)); // error message, if unsuccessful
  }

  /**
   * Returns a JSON object corresponding to the differentiate route.
   */
  private static JSONObject diffObject(Expression result, Expression expr, Variable var) {
    return jobject()
             .put("data",
               jobject()
                 // the gets are checked, because oDeriv is checked
                 .put("expression", expr.toLaTex())
                 .put("result", result.toLaTex())
                 .put("var", var.toLaTex())
             );
  }

  static JSONObject getEvaluation(Request req, Response res, Calculator calc) {
    // url variables
    String expr = req.params(":expr");
    String var = req.params(":var");
    String val = req.params(":val");

    return calc.evaluate(expr, var, val) // evaluate
             .map(result -> evalObject( // create json object
               result,
               calc.toOExpression(expr).get(),
               calc.toOVariable(var).get(),
               calc.toOExpression(val).get()
             )).orElseGet(() -> error(res)); // return an error if unsuccessful
  }

  /**
   * Returns a JSON object corresponding to the evaluate route.
   */
  private static JSONObject evalObject(Expression result, Expression expr, Variable var, Expression val) {
    return jobject()
             .put("data",
               jobject()
                 // the gets are checked, because oEval is checked
                 .put("expression", expr.toLaTex())
                 .put("result", result.toLaTex())
                 .put("var", var.toLaTex())
                 .put("val", val.toLaTex())
             );
  }

  static JSONObject getSimplified(Request req, Response res, Calculator calc) {
    // expression from url
    String expr = req.params(":expr");

    // simplify and return JSON object
    return calc.simplify(expr).map(result -> simplifyObject(result, expr))
             .orElseGet(() -> error(res));
  }

  /**
   * Returns a JSON object corresponding to the simplify route.
   *
   * @param result simplified result
   * @param input string from url
   * @return JSON object
   */
  private static JSONObject simplifyObject(Expression result, String input) {
    return jobject()
             .put("data",
               jobject()
                 // the gets are checked, because oEval is checked
                 .put("input", input)
                 .put("result", result.toLaTex())
             );
  }

  /**
   * Main function that runs the server on localhost:4567.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    // initialize calculator
    Calculator calc = new Calculator();

    // enable CORS headers
    // https://stackoverflow.com/questions/45295530/spark-cors-access-control-allow-origin-error
    after((Filter) (request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "GET");
    });

    // the GET call that differentiates an expression
    get("/differentiate/:expr/:var", (req, res) -> getDerivative(req, res, calc));

    // the GET call that evaluates an expression
    get("/evaluate/:expr/:var/:val", (req, res) -> getEvaluation(req, res, calc));

    // the GET call that simplifies an expression
    get("/simplify/:expr", (req, res) -> getSimplified(req, res, calc));
  }
}