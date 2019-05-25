package com.deriv.util;

/**
 * A tuple of two elements.
 * @param <T1> type of item 1
 * @param <T2> type of item 2
 */
public class Tuple2<T1, T2> extends Tuple1<T1> {
  /**
   * Second stored item.
   */
  final T2 item2;

  /**
   * Constructor for a Tuple2.
   * @param item1 first input
   * @param item2 second input
   */
  Tuple2(T1 item1, T2 item2) {
    super(item1);
    this.item2 = item2;
  }

  /**
   * Static constructor for a Tuple2.
   * @param item1 first input
   * @param item2 second input
   * @param <T1> first type
   * @param <T2> second type
   * @return typle1
   */
  public static <T1, T2> Tuple2<T1, T2> of(T1 item1, T2 item2) {
    return new Tuple2<>(item1, item2);
  }

  @Override
  public String toString() {
    return "(" + getItem1() + ", " + item2.toString() + ")";
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
    return 31 * super.hashCode() + item2.hashCode();
  }

  /**
   * Getter method for the second stored item.
   * @return item2
   */
  public T2 getItem2() {
    return item2;
  }
}
