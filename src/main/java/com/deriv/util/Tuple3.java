package com.deriv.util;

public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
  final T3 item3;

  Tuple3(T1 item1, T2 item2, T3 item3) {
    super(item1, item2);
    this.item3 = item3;
  }

  public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 item1, T2 item2, T3 item3) {
    return new Tuple3<>(item1, item2, item3);
  }

  @Override
  public String toString() {
    return "(" + super.toString() + ", " + item3.toString() + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Tuple3)) {
      return false;
    }

    Tuple3 tup = (Tuple3) o;
    return item1.equals(tup.getItem1())
             && item2.equals(tup.getItem2())
             && item3.equals(tup.getItem3());
  }

  @Override
  public int hashCode() {
    // a hack, but it works
    return super.hashCode() + 7 * item3.hashCode();
  }

  public T3 getItem3() {
    return item3;
  }
}