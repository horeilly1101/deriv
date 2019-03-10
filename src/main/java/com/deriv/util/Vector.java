package com.deriv.util;

/**
 * Symbolic representation of a vector.
 *
 * @param <T> input parameter.
 */
public class Vector<T extends Composable<T>> extends Matrix<T> {
  private Vector(T[][] _data, int _height, Class<T> _clazz) {
    super(_data, 1, _height, _clazz);
  }

  /**
   * Static constructor for a Vector object.
   *
   * @param elements
   * @param <T>
   * @return
   */
  public static <T extends Composable<T>> Vector<T> of (T... elements) {
    int height = elements.length;

    // empty arrays provide all kinds of problems, so we don't allow them
    if (height == 0) {
      throw new RuntimeException("Input arrays cannot be empty!");
    }

    // get the class of the input objects
    @SuppressWarnings("unchecked")
    Class<T> clazz = (Class<T>) elements[0].getClass();

    // initialize an empty 2D array
    T[][] newData = empty2DArray(clazz, height, 1);

    // we need each row to have the same length
    for (int i = 0; i < elements.length; i++) {
      if (elements[i] == null) {
        throw new RuntimeException("Vector elements cannot be null!");
      }

      // create the necessary double array
      newData[i][0] = elements[i];
    }

    // construct matrix
    return new Vector<>(newData, height, clazz);
  }

  /**
   * Takes the dot product of two vectors.
   *
   * @param input vector
   * @return the computed dot product
   */
  public T dot(Vector<T> input) {
    if (this.getHeight() != input.getHeight()) {
      throw new RuntimeException("Vectors must have the same height!");
    }

    // initialize an empty 2D array
    T product = this.get(0, 0).getAddID();

    for (int i = 0; i < this.getHeight(); i++) {
      product = product.plus(this.get(i, 0).times(input.get(i, 0)));
    }

    return product;
  }
}