package com.deriv.util;

public class ThreeTuple<T1, T2, T3> extends Tuple<T1, T2> {
  private final T3 thirdItem;

  ThreeTuple(T1 firstItem, T2 seconditem, T3 thirdItem) {
    super(firstItem, seconditem);
    this.thirdItem = thirdItem;
  }

  public static <T1, T2, T3> ThreeTuple<T1, T2, T3> of(T1 firstItem, T2 secondItem, T3 thirditem) {
    return new ThreeTuple<>(firstItem, secondItem, thirditem);
  }

  @Override
  public String toString() {
    return "(" + getFirstItem().toString() + ", "
             + getSecondItem().toString() + ", "
             + thirdItem.toString() + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Tuple)) {
      return false;
    }

    ThreeTuple tup = (ThreeTuple) o;
    return getFirstItem().equals(tup.getFirstItem())
             && getSecondItem().equals(tup.getSecondItem())
             && thirdItem.equals(tup.getThirdItem());
  }

  @Override
  public int hashCode() {
    // a hack, but it works
    return super.hashCode() + 7 * thirdItem.hashCode();
  }

  public T3 getThirdItem() {
    return thirdItem;
  }
}