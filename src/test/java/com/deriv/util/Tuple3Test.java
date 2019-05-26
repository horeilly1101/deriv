package com.deriv.util;

import com.deriv.expression.Expression;
import org.junit.jupiter.api.Test;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.addID;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Variable.x;
import static org.junit.jupiter.api.Assertions.*;

class Tuple3Test {
  @Test
  void getTest() {
    Tuple3<Expression, Expression, Expression> tup = Tuple3.of(multID(), addID(), x());
    assertEquals(multID(), tup.getItem1()); // check first item1
    assertEquals(addID(), tup.getItem2()); // check second item1
    assertEquals(x(), tup.getItem3()); // check third item1

    Tuple3<Expression, Expression, Expression> tup2 = Tuple3.of(add(multID(), x()), multID(), addID());
    assertEquals(add(multID(), x()), tup2.getItem1()); // check first item1
    assertEquals(multID(), tup2.getItem2()); // check second item1
    assertEquals(addID(), tup2.getItem3());

    Tuple3<Double, Double, Double> tup3
      = Tuple3.of(new Double("1.5"), new Double("3.6"), new Double("1.0"));
    assertEquals(new Double("1.5"), tup3.getItem1()); // check first item1
    assertEquals(new Double("3.6"), tup3.getItem2()); // check second item1
    assertEquals(new Double("1.0"), tup3.getItem3()); // check third item1
  }

  @Test
  void equalsTest() {
    Tuple3<Expression, Expression, Expression> tup = Tuple3.of(multID(), addID(), x());
    assertEquals(Tuple3.of(multID(), addID(), x()), tup); // check equality
    assertEquals(tup, tup);
    assertNotEquals(tup, multID());

    Tuple3<Expression, Expression, Expression> tup2 = Tuple3.of(add(multID(), x()), multID(), addID());
    assertEquals(Tuple3.of(add(multID(), x()), multID(), addID()), tup2); // check equality again
    assertNotEquals(Tuple3.of(add(multID(), x()), multID(), multID()), tup2);

    Tuple3<Double, Double, Double> tup3
      = Tuple3.of(new Double("1.5"), new Double("3.6"), new Double("1.0"));
    assertNotEquals(
      Tuple3.of(new Double("1.4"), new Double("3.6"), new Double("1.0")),
      tup3); // check not equals
    assertNotEquals(
      Tuple3.of(new Double("1.5"), new Double("3.7"), new Double("1.0")),
      tup3); // check not equals
  }

  @Test
  void hashCodeTest() {
    Tuple3<Expression, Expression, Expression> tup = Tuple3.of(multID(), addID(), x());
    assertEquals(
      31 * 31 * multID().hashCode() + 31 * addID().hashCode() + x().hashCode(),
      tup.hashCode()
    );
  }

  @Test
  void toStringTest() {
    Tuple3<Expression, Expression, Expression> tup = Tuple3.of(multID(), x(), addID());
    assertEquals("(1, x, 0)", tup.toString());
  }
}
