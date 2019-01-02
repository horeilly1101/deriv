package com.deriv.server;

import com.deriv.expression.Expression;
import org.json.JSONObject;
import java.util.Optional;

import static com.deriv.parser.Parser.parse;
import static spark.Spark.*;

public class Server {
  // runs the server on localhost:4567
  public static void main(String[] args) {
    get("/differentiate/:expr/:wrt", (req, res) -> {
      // url variables
      String exprString = req.params(":expr");
      String wrt = req.params(":wrt");

      // evaluate derivative, if possible
      Optional<Expression> oParsed = parse(exprString);
      String expression = oParsed.isPresent()
                              ? oParsed.get().toString()
                              : "ERROR";
      String result = oParsed.isPresent()
                          ? oParsed.get().differentiate(wrt).toString()
                          : "ERROR";

      return (new JSONObject())
                 .put("expression", expression)
                 .put("wrt", wrt)
                 .put("result", result);
    });

    get("/evaluate/:expr/:var/:val", (req, res) -> {
      // url variables
      String expr = req.params(":expr");
      String var = req.params(":var");
      // need to check val
      String val = req.params(":val");

      // evaluate expression, if possible
      Optional<Expression> oParsed = parse(expr);
      String result = oParsed.isPresent()
                          ? oParsed.get().evaluate(var, new Double(val)).toString()
                          : "ERROR";

      return (new JSONObject())
                 .put("expression", expr)
                 .put("result", result)
                 .put("var", var)
                 .put("val", val);
    });
  }
}