package com.deriv.server;

import com.deriv.expression.Expression;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Optional;

import static com.deriv.parser.Parser.parse;
import static spark.Spark.*;

public class Server {
  // runs the server on localhost:4567
  public static void main(String[] args) {
    get("/differentiate/:expr/:wrt", (req, res) -> {
      // url variables
      String expr = req.params(":expr");
      String wrt = req.params(":wrt");

      // evaluate derivative, if possible
      Optional<Expression> oParsed = parse(expr);
      String result = oParsed.isPresent()
                          ? oParsed.get().differentiate(wrt).toString()
                          : "ERROR";

      return (new JSONObject())
                 .put("expression", expr)
                 .put("result", result);
    });

//    get("/evaluate/:expr/:at/:with", (req, res) -> {
//      // url variables
//      String expr = req.params(":expr");
//      String at = req.params(":at");
//      String with = req.params(":with");
//
//      // evaluate derivative, if possible
//      Optional<Expression> oParsed = parse(expr);
//      String result = oParsed.isPresent()
//                          ? oParsed.get().evaluate(at, ).toString()
//                          : "ERROR";
//
//      return (new JSONObject())
//                 .put("expression", expr)
//                 .put("result", result);
//    });
  }
}