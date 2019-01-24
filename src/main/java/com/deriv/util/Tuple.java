package com.deriv.util;

public class Tuple<T1, T2> {
  private final T1 firstItem;
  private final T2 secondItem;

  Tuple(T1 firstItem, T2 seconditem) {
    this.firstItem = firstItem;
    this.secondItem = seconditem;
  }

  public static <T1, T2> Tuple<T1, T2> of(T1 firstItem, T2 seconditem) {
    return new Tuple<>(firstItem, seconditem);
  }

  @Override
  public String toString() {
    return "(" + firstItem.toString() + ", " + secondItem.toString() + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Tuple)) {
      return false;
    }

    Tuple tup = (Tuple) o;
    return firstItem.equals(tup.getFirstItem()) && secondItem.equals(tup.getSecondItem());
  }

  @Override
  public int hashCode() {
    // a hack, but it works
    return 11 * firstItem.hashCode() + 9 * secondItem.hashCode() + 3;
  }

  public T1 getFirstItem() {
    return firstItem;
  }

  public T2 getSecondItem() {
    return secondItem;
  }
}
