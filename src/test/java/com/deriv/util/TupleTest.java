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
    Tuple<Expression, Expression> tup = Tuple.of(multID(), addID());
    assertEquals(multID(), tup.getFirstItem()); // check first item
    assertEquals(addID(), tup.getSecondItem()); // check second item

    Tuple<Expression, Expression> tup2 = Tuple.of(add(multID(), x()), multID());
    assertEquals(add(multID(), x()), tup2.getFirstItem()); // check first item
    assertEquals(multID(), tup2.getSecondItem()); // check second item

    Tuple<Double, Double> tup3 = Tuple.of(new Double("1.5"), new Double("3.6"));
    assertEquals(new Double("1.5"), tup3.getFirstItem()); // check first item
    assertEquals(new Double("3.6"), tup3.getSecondItem()); // check second item
  }

  @Test
  void equalsTest() {
    Tuple<Expression, Expression> tup = Tuple.of(multID(), addID());
    assertEquals(Tuple.of(multID(), addID()), tup); // check equality

    Tuple<Expression, Expression> tup2 = Tuple.of(add(multID(), x()), multID());
    assertEquals(Tuple.of(add(multID(), x()), multID()), tup2); // check equality again

    Tuple<Double, Double> tup3 = Tuple.of(new Double("1.5"), new Double("3.6"));
    assertNotEquals(Tuple.of(new Double("1.7"), new Double("3.6")), tup3); // check not equals
  }
}
