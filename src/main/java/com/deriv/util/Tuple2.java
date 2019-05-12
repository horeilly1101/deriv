package com.deriv.util;

public class Tuple2<T1, T2> extends Tuple1<T1> {
  final T2 item2;

  Tuple2(T1 item1, T2 item2) {
    super(item1);
    this.item2 = item2;
  }

  public static <T1, T2> Tuple2<T1, T2> of(T1 firstItem, T2 seconditem) {
    return new Tuple2<>(firstItem, seconditem);
  }

  @Override
  public String toString() {
    return "(" + super.toString() + ", " + item2.toString() + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Tuple2)) {
      return false;
    }

    Tuple2 tup = (Tuple2) o;
    return item1.equals(tup.getItem1()) && item2.equals(tup.getItem2());
  }

  @Override
  public int hashCode() {
    // a hack, but it works
    return 11 * item1.hashCode() + 9 * item2.hashCode() + 3;
  }

  public T2 getItem2() {
    return item2;
  }
}
