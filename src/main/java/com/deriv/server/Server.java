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

  // runs the server on localhost:4567
  public static void main(String[] args) {
    // declare calculator
    Calculator calc = new Calculator();

    // the GET call that differentiates an expression
    get("/differentiate/:expr/:var", (req, res) -> {
      // url variables
      String expr = req.params(":expr");
      String var = req.params(":var");

      // attempt to differentiate expression
      Optional<Expression> oDeriv = calc.differentiate(expr, var);

      // return the derivative if possible, otherwise an error
      return oDeriv.isPresent()
                 ? diffObject(oDeriv.get(), calc.toOExpression(expr).get(), calc.toOVariable(var).get())
                 : error(res);
    });

    // the GET call that evaluates an expression
    get("/evaluate/:expr/:var/:val", (req, res) -> {
      // url variables
      String expr = req.params(":expr");
      String var = req.params(":var");
      String val = req.params(":val");

      // attempt to evaluate expression
      Optional<Expression> oEval = calc.evaluate(expr, var, val);

      // return the evaluation if possible, otherwise an error
      return oEval.isPresent()
                 ? evalObject(
                    oEval.get(),
                    calc.toOExpression(expr).get(),
                    calc.toOVariable(var).get(),
                    calc.toOExpression(val).get())
                 : error(res);
    });
  }
}