package com.deriv.util;

/**
 * A tuple of 3 elements.
 * @param <T1> first type
 * @param <T2> second type
 * @param <T3> third type
 */
public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
  /**
   * Third stored item.
   */
  final T3 item3;

  /**
   * Constructor for a Tuple3.
   * @param item1 first input
   * @param item2 second input
   * @param item3 third input
   */
  Tuple3(T1 item1, T2 item2, T3 item3) {
    super(item1, item2);
    this.item3 = item3;
  }

  /**
   * Static constructor for a Tuple3.
   * @param item1 first input
   * @param item2 second input
   * @param item3 third input
   * @param <T1> first type
   * @param <T2> second type
   * @param <T3> third type
   * @return tuple3
   */
  public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 item1, T2 item2, T3 item3) {
    return new Tuple3<>(item1, item2, item3);
  }

  @Override
  public String toString() {
    return "(" + getItem1() + ", " + getItem2() + ", " + item3 + ")";
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
    return 31 * super.hashCode() + item3.hashCode();
  }

  /**
   * Getter method for item3.
   * @return item3.
   */
  public T3 getItem3() {
    return item3;
  }
}