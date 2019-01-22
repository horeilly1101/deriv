package com.deriv.server;

import com.deriv.expression.Expression;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
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