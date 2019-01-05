package com.deriv.server;

import com.deriv.expression.Expression;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import spark.Response;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Variable.x;
import static org.junit.jupiter.api.Assertions.*;
import static com.deriv.server.Server.*;

class ServerTest {
  @Test
  void jsonTest() {
    assertEquals(JSONObject.class, jobject().getClass());
    assertTrue(jobject().isEmpty());
  }

  @Test
  void parseTest() {
    // test parseVar
    Optional<String> oVar = parseVar("x");
    assertTrue(oVar.isPresent());
    assertEquals("x", oVar.get());

    Optional<String> oVar2 = parseVar("xyz");
    assertFalse(oVar2.isPresent());

    // test parseDouble
    Optional<Double> oVal = parseDouble("5.4");
    assertTrue(oVal.isPresent());
    assertEquals((Double) 5.4, oVal.get());

    Optional<Double> oVal2 = parseDouble("5");
    assertTrue(oVal2.isPresent());
    assertEquals((Double) 5.0, oVal2.get());

    Optional<Double> oVal3 = parseDouble("5.4.9");
    assertFalse(oVal3.isPresent());
  }

  @Test
  void oDifferentiateTest() {
    String str = "x ^ 2";
    Expression ex = mult(constant(2), x());
    Optional<Expression> parsed = oDifferentiate(str, "x");
    assertTrue(parsed.isPresent());
    assertEquals(ex, parsed.get());
  }

  @Test
  void oEvaluateTest() {
    String str = "x ^ 2";
    Expression ex = constant(9);
    Optional<Expression> parsed = oEvaluate(str, "x", "3");
    assertTrue(parsed.isPresent());
    assertEquals(ex, parsed.get());
  }

  @Test
  void diffObjectTest() {
    String str = "x ^ 2";
    Expression ex = mult(constant(2), x());
    assertEquals(JSONObject.class, diffObject(ex, str, "x").getClass());
  }

  @Test
  void evalObjectTest() {
    String str = "x ^ 2";
    Expression ex = constant(9);
    assertEquals(JSONObject.class, evalObject(ex, str, "x", "3").getClass());
  }
}