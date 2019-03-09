package com.deriv.util;

/**
 * Class that represents a matrix (taken from the context of linear algebra).
 * Note that indexing starts at 0.
 *
 * @param <T> generic type parameter.
 */
public class Matrix<T extends Arithmetic<T>> implements Arithmetic<Matrix<T>> {
  private T[][] _data;
  private int _width;
  private int _height;

  /**
   * Private constructor for a matrix.
   *
   * @param _data array of arrays of elements.
   * @param _width width of the matrix.
   * @param _height height of the matrix.
   */
  private Matrix(T[][] _data, int _width, int _height) {
    if (_width == 0 || _height == 0) {
      throw new RuntimeException("You can't create an empty matrix!");
    }

    this._data = _data;
    this._width = _width;
    this._height = _height;
  }

  /**
   * Getter method for the widht of a matrix.
   *
   * @return the width.
   */
  public int getWidth() {
    return _width;
  }

  /**
   * Getter method for the height of a matrix.
   *
   * @return the height.
   */
  public int getHeight() {
    return _height;
  }

  /**
   * Gets the desired element from the matrix. (Indexing starts at 0!)
   *
   * @param i row index
   * @param j column index
   * @return element (i, j)
   */
  public T get(int i, int j) {
    if (i >= this._height) {
      throw new RuntimeException("This row of the matrix does not exist!");
    }

    if (j >= this._width) {
      throw new RuntimeException("This column of the matrix does not exist!");
    }

    return _data[i][j];
  }

  @Override
  @SuppressWarnings("unchecked")
  public Matrix<T> times(Matrix<T> input) {
    if (this._width != input.getHeight()) {
      throw new RuntimeException("The dimensions of these matrices don't match up!");
    }

    // to trick the compiler, we have to create an Object array and then
    // cast it to the generic we want
    T[][] newData = (T[][]) new Object[this._height][input.getWidth()];

    // initialize the array
    for (int i = 0; i < this._height; i++) {
      for (int j = 0; j < input.getWidth(); j++) {
        newData[i][j] = this.get(0, 0).getAddID();
      }
    }

    for (int i = 0; i < this._height; i++) {
      for (int j = 0; j < input.getWidth(); j++) {
        for (int k = 0; k < this._width; k++) {
          // something
        }
      }
    }

    return new Matrix<>(newData, input.getWidth(), this._height);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Matrix<T> plus(Matrix<T> input) {
    if (this._width != input.getWidth() || this._height != input.getHeight()) {
      throw new RuntimeException("The dimensions of these matrices don't match up!");
    }

    // to trick the compiler, we have to create an Object array and then
    // cast it to the generic we want
    T[][] newData = (T[][]) new Object[this._height][this._width];

    for (int i = 0; i < this._height; i++) {
      for (int j = 0; j < this._width; j++) {
        newData[i][j] = this.get(i, j).plus(input.get(i, j));
      }
    }

    return new Matrix<>(newData, this._width, this._height);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Matrix<T> getAddID() {
    // to trick the compiler, we have to create an Object array and then
    // cast it to the generic we want
    T[][] newData = (T[][]) new Object[this._height][this._width];

    for (int i = 0; i < this._height; i++) {
      for (int j = 0; j < this._width; j++) {
        newData[i][j] = this.get(0, 0).getAddID();
      }
    }

    return new Matrix<>(newData, this._width, this._height);
  }
}
