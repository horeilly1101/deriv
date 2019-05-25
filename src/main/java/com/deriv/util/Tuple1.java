package com.deriv.util;

/**
 * A tuple of one element.
 * @param <T> input type
 */
public class Tuple1<T> {
  /**
   * First stored item.
   */
  final T item1;

  /**
   * Constructor for a Tuple1.
   * @param item1 input
   */
  Tuple1(T item1) {
    this.item1 = item1;
  }

  /**
   * Static constructor for a Tuple1.
   * @param item input item
   * @param <T> input item type
   * @return tuple1
   */
  public static <T> Tuple1<T> of(T item) {
    return new Tuple1<>(item);
  }

  @Override
  public String toString() {
    return "(" + item1 + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Tuple1)) {
      return false;
    }

    Tuple1 tup = (Tuple1) o;
    return item1.equals(tup.getItem1());
  }

  @Override
  public int hashCode() {
    // a hack, but it works
    return item1.hashCode();
  }

  /**
   * Getter method for item1.
   * @return
   */
  public T getItem1() {
    return item1;
  }
}