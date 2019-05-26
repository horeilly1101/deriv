package com.deriv.server;

import com.deriv.calculator.Calculator;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import static org.junit.jupiter.api.Assertions.*;
import static com.deriv.server.Server.*;

class ServerTest {
  /**
   * dummy response
   */
  private Response response = new Response() {
    public void status(int num) {
      // no op
    }
  };

  /**
   * dummy request
   */
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
  };

  /**
   * private Calculator object.
   */
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