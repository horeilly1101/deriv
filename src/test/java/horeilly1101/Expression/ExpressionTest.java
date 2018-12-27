package horeilly1101.Expression;

import org.junit.Test;

import static horeilly1101.Expression.Add.*;
import static horeilly1101.Expression.Constant.*;
import static horeilly1101.Expression.Mult.*;
import static horeilly1101.Expression.Power.*;
import static horeilly1101.Expression.Variable.*;
import static horeilly1101.Expression.Trig.*;
import static horeilly1101.Expression.Log.*;
import static org.junit.Assert.*;

public class ExpressionTest {

  @Test
  public void typeCheckTest() {
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
  public void typeCastTest() {
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
  public void getterTest() {
    // e * x
    Expression mult = mult(x(), e());

    assertEquals(multID(), mult.getConstantFactor());
    assertEquals(mult, mult.getSymbolicFactors());
    assertEquals(addID(), mult.getConstantTerm());

    // x + 1
    Expression ad = add(x(), multID());

    assertEquals(multID(), ad.getConstantTerm());
    assertEquals(x(), ad.getSymbolicTerms());
    assertEquals(multID(), ad.getExponent());

    // x ^ 2
    Expression pow = poly(x(), 2);

    assertEquals(constant(2), pow.getExponent());
    assertEquals(x(), pow.getBase());
    assertEquals(pow, pow.getSymbolicFactors());
  }
}