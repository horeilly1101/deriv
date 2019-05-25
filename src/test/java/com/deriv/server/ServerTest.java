package com.deriv.server;

import com.deriv.calculator.Calculator;
import com.deriv.expression.Expression;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Power.poly;
import static com.deriv.expression.Variable.x;
import static org.junit.jupiter.api.Assertions.*;
import static com.deriv.server.Server.*;

class ServerTest {
  private Response response = new Response() {
    public void status(int num) {

    }
  }; // dummy response

  private Request request = new Request() {
    public String params(String route) {
      switch (route) {
        case ":expr":
          return "2x";
        case ":var":
          return "x";
        case ":val":
          return "2";
      }

      throw new RuntimeException("unknown param asked for");
    }
  }; // dummy request

  private Calculator calc = new Calculator();

  @Test
  void jsonTest() {
    assertEquals(JSONObject.class, jobject().getClass());
    assertTrue(jobject().isEmpty());
  }

  @Test
  void diffObjectTest() {
    JSONObject json = getDerivative(request, response, calc);
    assertEquals("2x", json.getJSONObject("data").get("expression"));
    assertEquals("2", json.getJSONObject("data").get("result"));
  }

  @Test
  void evalObjectTest() {
    JSONObject json = getEvaluation(request, response, calc);
    assertEquals("2x", json.getJSONObject("data").get("expression"));
    assertEquals("4", json.getJSONObject("data").get("result"));
  }

  @Test
  void simplifyObjectTest() {
    JSONObject json = getSimplified(request, response, calc);
    assertEquals("2x", json.getJSONObject("data").get("input"));
    assertEquals("2x", json.getJSONObject("data").get("result"));
  }

  @Test
  void errorTest() {
    assertEquals(JSONObject.class, error(response).getClass());
  }
}