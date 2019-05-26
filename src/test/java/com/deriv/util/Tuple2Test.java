package com.deriv.util;

import org.junit.jupiter.api.Test;
import com.deriv.expression.Expression;

import static com.deriv.expression.Add.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Variable.*;
import static org.junit.jupiter.api.Assertions.*;

class Tuple2Test {
  @Test
  void getTest() {
    Tuple2<Expression, Expression> tup = Tuple2.of(multID(), addID());
    assertEquals(multID(), tup.getItem1()); // check first item1
    assertEquals(addID(), tup.getItem2()); // check second item1

    Tuple2<Expression, Expression> tup2 = Tuple2.of(add(multID(), x()), multID());
    assertEquals(add(multID(), x()), tup2.getItem1()); // check first item1
    assertEquals(multID(), tup2.getItem2()); // check second item1

    Tuple2<Double, Double> tup3 = Tuple2.of(new Double("1.5"), new Double("3.6"));
    assertEquals(new Double("1.5"), tup3.getItem1()); // check first item1
    assertEquals(new Double("3.6"), tup3.getItem2()); // check second item1
  }

  @Test
  void equalsTest() {
    Tuple2<Expression, Expression> tup = Tuple2.of(multID(), addID());
    assertEquals(Tuple2.of(multID(), addID()), tup); // check equality
    assertEquals(tup, tup);
    assertNotEquals(tup, multID());

    Tuple2<Expression, Expression> tup2 = Tuple2.of(add(multID(), x()), multID());
    assertEquals(Tuple2.of(add(multID(), x()), multID()), tup2); // check equality again
    assertNotEquals(Tuple2.of(add(multID(), x()), addID()), tup2);

    Tuple2<Double, Double> tup3 = Tuple2.of(new Double("1.5"), new Double("3.6"));
    assertNotEquals(Tuple2.of(new Double("1.7"), new Double("3.6")), tup3); // check not equals
  }

  @Test
  void hashCodeTest() {
    Tuple2<Expression, Expression> tup = Tuple2.of(multID(), addID());
    assertEquals(multID().hashCode() * 31 + addID().hashCode(), tup.hashCode()); // check hashCode
  }

  @Test
  void toStringTest() {
    Tuple2<Expression, Expression> tup = Tuple2.of(multID(), x());
    assertEquals("(1, x)", tup.toString());
  }
}
