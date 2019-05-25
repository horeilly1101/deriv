package com.deriv.util;

import com.deriv.expression.Expression;
import org.junit.jupiter.api.Test;

import static com.deriv.expression.Add.add;
import static com.deriv.expression.Constant.multID;
import static com.deriv.expression.Variable.x;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class Tuple1Test {
  @Test
  void getTest() {
    Tuple1<Expression> tup = Tuple1.of(multID());
    assertEquals(multID(), tup.getItem1()); // check first item

    Tuple1<Expression> tup2 = Tuple1.of(add(multID(), x()));
    assertEquals(add(multID(), x()), tup2.getItem1()); // check first item

    Tuple1<Double> tup3 = Tuple1.of(new Double("1.5"));
    assertEquals(new Double("1.5"), tup3.getItem1()); // check first item
  }

  @Test
  void equalsTest() {
    Tuple1<Expression> tup = Tuple1.of(multID());
    assertEquals(Tuple1.of(multID()), tup); // check equality
    assertEquals(tup, tup);
    assertNotEquals(tup, multID());

    Tuple1<Expression> tup2 = Tuple1.of(add(multID(), x()));
    assertEquals(Tuple1.of(add(multID(), x())), tup2); // check equality again

    Tuple1<Double> tup3 = Tuple1.of(new Double("1.5"));
    assertNotEquals(Tuple1.of(new Double("1.7")), tup3); // check not equals
  }

  @Test
  void hashCodeTest() {
    Tuple1<Expression> tup = Tuple1.of(multID());
    assertEquals(multID().hashCode(), tup.hashCode());
  }

  @Test
  void toStringTest() {
    Tuple1<Expression> tup = Tuple1.of(multID());
    assertEquals("(1)", tup.toString());
  }
}
