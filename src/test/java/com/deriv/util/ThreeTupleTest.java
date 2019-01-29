package com.deriv.util;

import com.deriv.expression.Expression;
import org.junit.jupiter.api.Test;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.addID;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Variable.x;
import static org.junit.jupiter.api.Assertions.*;

class ThreeTupleTest {
  @Test
  void getTest() {
    ThreeTuple<Expression, Expression, Expression> tup = ThreeTuple.of(multID(), addID(), x());
    assertEquals(multID(), tup.getFirstItem()); // check first item
    assertEquals(addID(), tup.getSecondItem()); // check second item
    assertEquals(x(), tup.getThirdItem()); // check third item

    ThreeTuple<Expression, Expression, Expression> tup2 = ThreeTuple.of(add(multID(), x()), multID(), addID());
    assertEquals(add(multID(), x()), tup2.getFirstItem()); // check first item
    assertEquals(multID(), tup2.getSecondItem()); // check second item
    assertEquals(addID(), tup2.getThirdItem());

    ThreeTuple<Double, Double, Double> tup3
      = ThreeTuple.of(new Double("1.5"), new Double("3.6"), new Double("1.0"));
    assertEquals(new Double("1.5"), tup3.getFirstItem()); // check first item
    assertEquals(new Double("3.6"), tup3.getSecondItem()); // check second item
    assertEquals(new Double("1.0"), tup3.getThirdItem()); // check third item
  }

  @Test
  void equalsTest() {
    ThreeTuple<Expression, Expression, Expression> tup = ThreeTuple.of(multID(), addID(), x());
    assertEquals(ThreeTuple.of(multID(), addID(), x()), tup); // check equality

    ThreeTuple<Expression, Expression, Expression> tup2 = ThreeTuple.of(add(multID(), x()), multID(), addID());
    assertEquals(ThreeTuple.of(add(multID(), x()), multID(), addID()), tup2); // check equality again

    ThreeTuple<Double, Double, Double> tup3
      = ThreeTuple.of(new Double("1.5"), new Double("3.6"), new Double("1.0"));
    assertNotEquals(
      ThreeTuple.of(new Double("1.4"), new Double("3.6"), new Double("1.0")),
      tup3); // check not equals
  }
}
