package com.deriv.util;

public class Tuple1<T> {
  final T item1;

  Tuple1(T item1) {
    this.item1 = item1;
  }

  public static <T> Tuple1<T> of(T item) {
    return new Tuple1<>(item);
  }

  @Override
  public String toString() {
    return "(" + item1.toString() + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Tuple2)) {
      return false;
    }

    Tuple2 tup = (Tuple2) o;
    return item1.equals(tup.getItem1());
  }

  @Override
  public int hashCode() {
    // a hack, but it works
    return 11 * item1.hashCode() + 9;
  }

  public T getItem1() {
    return item1;
  }
}