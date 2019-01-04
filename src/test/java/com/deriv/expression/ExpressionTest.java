package com.deriv.expression;

import org.junit.jupiter.api.Test;

import static com.deriv.expression.Log.*;
import static com.deriv.expression.Mult.*;
import static com.deriv.expression.Add.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Trig.*;
import static com.deriv.expression.Variable.*;
import static com.deriv.expression.Power.*;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {

  @Test
  void typeCheckTest() {
    Expression mult = mult(x(), e());
    assertTrue(mult.isMult());

    Expression ad = add(x(), multID());
    assertTrue(ad.isAdd());

    Expression lo = log(e(), x());
    assertTrue(lo.isLog());

    Expression pow = poly(x(), 2);
    assertTrue(pow.isPower());

    Expression trig = sin(x());
    assertTrue(trig.isTrig());

    Expression con = constant(17);
    assertTrue(con.isConstant());

    Expression va = var("y");
    assertTrue(va.isVariable());
  }

  @Test
  void typeCastTest() {
    Expression mult = mult(x(), e());
    assertTrue(mult.asMult().isMult());

    Expression ad = add(x(), multID());
    assertTrue(ad.asAdd().isAdd());

    Expression lo = log(e(), x());
    assertTrue(lo.asLog().isLog());

    Expression pow = poly(x(), 2);
    assertTrue(pow.asPower().isPower());

    Expression trig = sin(x());
    assertTrue(trig.asTrig().isTrig());

    Expression con = constant(17);
    assertTrue(con.asConstant().isConstant());

    Expression va = var("y");
    assertTrue(va.asVariable().isVariable());
  }

  @Test
  void getterTest() {
    // e * x
    Expression mult = mult(x(), e());

    assertEquals(multID(), mult.getConstantFactor());
    assertEquals(mult, mult.getSymbolicFactors());
    assertEquals(addID(), mult.getConstantTerm());
    assertEquals(mult, mult.getSymbolicTerms());

    // x + 1
    Expression ad = add(x(), multID());

    assertEquals(multID(), ad.getConstantTerm());
    assertEquals(x(), ad.getSymbolicTerms());
    assertEquals(multID(), ad.getExponent());
    assertEquals(ad, ad.getNumerator());
    assertEquals(multID(), ad.getDenominator());

    // x ^ 2
    Expression pow = poly(x(), 2);

    assertEquals(constant(2), pow.getExponent());
    assertEquals(x(), pow.getBase());
    assertEquals(multID(), pow.getConstantFactor());
    assertFalse(pow.isNegative());
  }
}