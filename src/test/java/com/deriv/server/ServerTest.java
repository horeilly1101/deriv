package com.deriv.server;

import com.deriv.expression.Expression;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static com.deriv.expression.Constant.constant;
import static com.deriv.expression.Mult.mult;
import static com.deriv.expression.Power.poly;
import static com.deriv.expression.Variable.x;
import static org.junit.jupiter.api.Assertions.*;
import static com.deriv.server.Server.*;

class ServerTest {
  @Test
  void jsonTest() {
    assertEquals(JSONObject.class, jobject().getClass());
    assertTrue(jobject().isEmpty());
  }

//  @Test
//  void diffObjectTest() {
//    Expression func = poly(x(), 2);
//    Expression result = mult(constant(2), x());
//    assertEquals(JSONObject.class, diffObject(result, func, x().asVariable()).getClass());
//  }

  @Test
  void evalObjectTest() {
    Expression func = poly(x(), 2);
    Expression result = constant(9);
    assertEquals(JSONObject.class, evalObject(result, func, x().asVariable(), constant(3)).getClass());
  }
}