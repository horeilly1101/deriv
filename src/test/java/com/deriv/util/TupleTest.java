package com.deriv.util;

import org.junit.jupiter.api.Test;
import com.deriv.expression.Expression;

import static com.deriv.expression.Add.*;
import static com.deriv.expression.Constant.*;
import static com.deriv.expression.Variable.*;
import static org.junit.jupiter.api.Assertions.*;

class TupleTest {
  @Test
  void getTest() {
    Tuple2<Expression, Expression> tup = Tuple2.of(multID(), addID());
    assertEquals(multID(), tup.getFirstItem()); // check first item1
    assertEquals(addID(), tup.getSecondItem()); // check second item1

    Tuple2<Expression, Expression> tup2 = Tuple2.of(add(multID(), x()), multID());
    assertEquals(add(multID(), x()), tup2.getFirstItem()); // check first item1
    assertEquals(multID(), tup2.getSecondItem()); // check second item1

    Tuple2<Double, Double> tup3 = Tuple2.of(new Double("1.5"), new Double("3.6"));
    assertEquals(new Double("1.5"), tup3.getFirstItem()); // check first item1
    assertEquals(new Double("3.6"), tup3.getSecondItem()); // check second item1
  }

  @Test
  void equalsTest() {
    Tuple2<Expression, Expression> tup = Tuple2.of(multID(), addID());
    assertEquals(Tuple2.of(multID(), addID()), tup); // check equality

    Tuple2<Expression, Expression> tup2 = Tuple2.of(add(multID(), x()), multID());
    assertEquals(Tuple2.of(add(multID(), x()), multID()), tup2); // check equality again

    Tuple2<Double, Double> tup3 = Tuple2.of(new Double("1.5"), new Double("3.6"));
    assertNotEquals(Tuple2.of(new Double("1.7"), new Double("3.6")), tup3); // check not equals
  }
}
