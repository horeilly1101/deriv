package com.deriv.server;

import com.deriv.expression.Expression;
import org.json.JSONObject;
import java.util.Optional;

import static com.deriv.parser.Parser.parse;
import static spark.Spark.*;

public class Server {
  /**
   * An easy constructor for an empty JSON Object. Helps clean up
   * the code a little bit.
   */
  private static JSONObject jobject() {
    return new JSONObject();
  }

  // runs the server on localhost:4567
  public static void main(String[] args) {
    // the GET call that differentiates an expression
    get("/differentiate/:expr/:var", (req, res) -> {
      // url variables
      String expr = req.params(":expr");
      String var = req.params(":var");

      // attempt to parse var
      Optional<String> oVar = var.length() == 1
                                  ? parse(var).map(Expression::toString)
                                  : Optional.empty();

      // attempt to parse expr
      Optional<Expression> oExpr = parse(expr);

      // attempt to differentiate expression
      Optional<Expression> oDeriv = oVar.flatMap(vr ->
                                                    oExpr.map(ex -> ex.differentiate(vr)));

      // return the derivative if possible, otherwise an error
      return oDeriv.isPresent()
                 ? jobject()
                       .put("data",
                           jobject()
                               .put("expression", oExpr.get().toString())
                               .put("result", oDeriv.get().toString())
                               .put("var", oVar.get())
                       )
                 : jobject()
                       .put("error",
                           jobject()
                               .put("code", 400)
                               .put("problem", "input(s) not valid"));
    });

    // the GET call that evaluates an expression
    get("/evaluate/:expr/:var/:val", (req, res) -> {
      // url variables
      String expr = req.params(":expr");
      String var = req.params(":var");
      String val = req.params(":val");

      // attempt to parse val
      Optional<Double> oVal;
      try {
        oVal = Optional.of(new Double(val));
      } catch (NumberFormatException e) {
        oVal = Optional.empty();
      }

      // attempt to parse var
      Optional<String> oVar = var.length() == 1
                                      ? parse(var).map(Expression::toString)
                                      : Optional.empty();

      // attempt to parse expr
      Optional<Expression> oExpr = parse(expr);

      // attempt to evaluate expression
      Optional<Expression> oEval = oVal.flatMap(vl ->
                                                    oVar.flatMap(vr ->
                                                                     oExpr.map(ex -> ex.evaluate(vr, vl))));

      // return the evaluation if possible, otherwise an error
      return oEval.isPresent()
                 ? jobject()
                     .put("data",
                         jobject()
                           .put("expression", oExpr.get().toString())
                           .put("result", oEval.get().toString())
                           .put("var", oVar.get())
                           .put("val", oVal.get().toString())
                     )
                 : jobject()
                       .put("error",
                           jobject()
                               .put("code", 400)
                               .put("problem", "input(s) not valid"));
    });
  }
}