//package com.deriv.util;
//
//import java.lang.reflect.Array;
//import java.util.Arrays;
//
///**
// * Class that represents a matrix (taken from the context of linear algebra).
// * Note that indexing starts at 0.
// *
// * @param <T> generic type parameter.
// */
//public class Matrix<T extends Composable<T>> implements Composable<Matrix<T>> {
//  /**
//   * An array of arrays that represents our matrix.
//   */
//  private T[][] _data;
//
//  /**
//   * The width of our matrix.
//   */
//  private int _width;
//
//  /**
//   * The height of our matrix.
//   */
//  private int _height;
//
//  /**
//   * The class of the elements in our matrix.
//   */
//  private Class<T> _clazz;
//
//  /**
//   * Private constructor for a matrix.
//   *
//   * @param _data array of arrays of elements.
//   * @param _width width of the matrix.
//   * @param _height height of the matrix.
//   */
//  Matrix(T[][] _data, int _width, int _height, Class<T> _clazz) {
//    if (_width == 0 || _height == 0) {
//      throw new RuntimeException("You can't create an empty matrix!");
//    }
//
//    this._data = _data;
//    this._width = _width;
//    this._height = _height;
//    this._clazz = _clazz;
//  }
//
//  @Override
//  public String toString() {
//    StringBuilder result = new StringBuilder("[\n");
//    for (int i = 0; i < _height; i++) {
//      result.append("\t");
//      result.append(Arrays.toString(_data[i]));
//      result.append("\n");
//    }
//    result.append("]");
//    return result.toString();
//  }
//
//  @SuppressWarnings("unchecked")
//  static <T extends Composable<T>> T[][] empty2DArray(Class<T> clazz, int height, int width) {
//    // to trick the compiler, we have to create an Object array and then
//    // cast it to the generic we want
//    return (T[][]) Array.newInstance(clazz, height, width);
//  }
//
//  /**
//   * Static constructor for a Matrix. This should be the only way you create a matrix!
//   * Ensure that the input array is not empty, and ensure that each array element has
//   * the same length.
//   *
//   * @param _data input array of arrays.
//   * @param <T> type parameter.
//   * @return constructed matrix object.
//   */
//  public static <T extends Composable<T>> Matrix<T> make(T[][] _data) {
//    int height = _data.length;
//
//    // empty arrays provide all kinds of problems, so we don't allow them
//    if (height == 0) {
//      throw new RuntimeException("Input arrays cannot be empty!");
//    }
//
//    int width = _data[0].length;
//
//    if (width == 0) {
//      throw new RuntimeException("Input arrays cannot be empty!");
//    }
//
//    // we need each row to have the same length
//    for (T[] row : _data) {
//      if (row.length != width) {
//        throw new RuntimeException("Each row must have the same number of elements!");
//      }
//
//      // we can't let elements be null
//      for (T elem : row) {
//        if (elem == null) {
//          throw new RuntimeException("Matrix elements cannot be null!");
//        }
//      }
//    }
//
//    // get the class of the input objects
//    @SuppressWarnings("unchecked")
//    Class<T> clazz = (Class<T>) _data[0][0].getClass();
//
//    // construct matrix
//    return new Matrix<>(_data, width, height, clazz);
//  }
//
//  /**
//   * Getter method for the widht of a matrix.
//   *
//   * @return the width.
//   */
//  public int getWidth() {
//    return _width;
//  }
//
//  /**
//   * Getter method for the height of a matrix.
//   *
//   * @return the height.
//   */
//  public int getHeight() {
//    return _height;
//  }
//
//  /**
//   * Getter method for the matrix data.
//   *
//   * @return _data.
//   */
//  public T[][] getData() {
//    return _data;
//  }
//
//  /**
//   * Getter method for the class of the data in the matrix.
//   *
//   * @return desired class.
//   */
//  public Class<T> getClazz() {
//    return _clazz;
//  }
//
//  /**
//   * Gets the desired element from the matrix. (Indexing starts at 0!)
//   *
//   * @param i row index
//   * @param j column index
//   * @return element (i, j)
//   */
//  public T get(int i, int j) {
//    if (i >= this._height) {
//      throw new RuntimeException("This row of the matrix does not exist!");
//    }
//
//    if (j >= this._width) {
//      throw new RuntimeException("This column of the matrix does not exist!");
//    }
//
//    return _data[i][j];
//  }
//
//  @Override
//  public Matrix<T> times(Matrix<T> input) {
//    if (this._width != input.getHeight()) {
//      throw new RuntimeException("The dimensions of these matrices don't match up!");
//    }
//
//    // initialize empty 2D array
//    T[][] newData = empty2DArray(_clazz, _height, input.getWidth());
//
//    // fill in the array
//    for (int i = 0; i < this._height; i++) {
//      for (int j = 0; j < input.getWidth(); j++) {
//        newData[i][j] = this.get(0, 0).getAddID();
//      }
//    }
//
//    // compute the matrix product
//    for (int i = 0; i < this._height; i++) {
//      for (int j = 0; j < input.getWidth(); j++) {
//        for (int k = 0; k < this._width; k++) {
//          newData[i][j] = newData[i][j].plus(this.get(i, k).times(input.get(k, j)));
//        }
//      }
//    }
//
//    return new Matrix<>(newData, input.getWidth(), this._height, this._clazz);
//  }
//
//  @Override
//  public Matrix<T> plus(Matrix<T> input) {
//    if (this._width != input.getWidth() || this._height != input.getHeight()) {
//      throw new RuntimeException("The dimensions of these matrices don't match up!");
//    }
//
//    // initialize empty 2D array
//    T[][] newData = empty2DArray(_clazz, _height, _width);
//
//    for (int i = 0; i < this._height; i++) {
//      for (int j = 0; j < this._width; j++) {
//        newData[i][j] = this.get(i, j).plus(input.get(i, j));
//      }
//    }
//
//    return new Matrix<>(newData, this._width, this._height, this._clazz);
//  }
//
//  @Override
//  public Matrix<T> getAddID() {
//    // initialize empty 2D array
//    T[][] newData = empty2DArray(_clazz, _height, _width);
//
//    for (int i = 0; i < this._height; i++) {
//      for (int j = 0; j < this._width; j++) {
//        newData[i][j] = this.get(0, 0).getAddID();
//      }
//    }
//
//    return new Matrix<>(newData, this._width, this._height, this._clazz);
//  }
//}
