//package com.deriv.util;
//
///**
// * Interface that specifies a way to multiply and add two objects. This helps us generalize
// * across the code base, decoupling the system.
// */
//public interface Composable<T extends Composable<T>> {
//  /**
//   * Multiplies an Composable object with another Composable object.
//   *
//   * @param input to be composes with.
//   * @return the resulting object.
//   */
//  T times(T input);
//
//  /**
//   * Adds two Composable objects together.
//   *
//   * @param input to be added.
//   * @return the resulting object.
//   */
//  T plus(T input);
//
//  /**
//   * Getter method for the additive identity of an Composable.
//   *
//   * @return additive identity.
//   */
//  T getAddID();
//
////  T timesScalar(T input)
//}
