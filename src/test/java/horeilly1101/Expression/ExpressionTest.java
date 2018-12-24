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

    Expression pow = poly(x(), constant(2.0));
    assertTrue(pow.isPower());

    Expression trig = sin(x());
    assertTrue(trig.isTrig());

    Expression con = constant(17.0);
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

    Expression pow = poly(x(), constant(2.0));
    assertTrue(pow.asPower().isPower());

    Expression trig = sin(x());
    assertTrue(trig.asTrig().isTrig());

    Expression con = constant(17.0);
    assertTrue(con.asConstant().isConstant());

    Expression va = var("y");
    assertTrue(va.asVariable().isVariable());
  }
}