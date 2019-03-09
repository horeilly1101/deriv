package com.deriv.util;

/**
 * Interface that specifies a way to multiply and add two objects. This helps us generalize
 * across the code base, decoupling the system.
 */
public interface Arithmetic<T extends Arithmetic<T>> {
  /**
   * Multiplies an Arithmetic object with another Arithmetic object.
   *
   * @param input to be composes with.
   * @return the resulting object.
   */
  T times(T input);

  /**
   * Adds two Arithmetic objects together.
   *
   * @param input to be added.
   * @return the resulting object.
   */
  T plus(T input);

  /**
   * Getter method for the additive identity of an Arithmetic.
   *
   * @return additive identity.
   */
  T getAddID();
}
