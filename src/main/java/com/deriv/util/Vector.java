package com.deriv.util;

/**
 * Symbolic representation of a vector.
 *
 * @param <T> input parameter.
 */
public class Vector<T extends Arithmetic<T>> extends Matrix<T> {
  Vector(T[][] _data, int _width, int _height, Class<?> _clazz) {
    super(_data, _width, _height, _clazz);
  }
}
