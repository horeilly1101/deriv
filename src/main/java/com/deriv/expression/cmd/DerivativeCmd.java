package com.deriv.expression.cmd;

import com.deriv.util.ICmd;
import java.util.Map;
import java.util.function.Function;

/**
 * General command that helps us differentiate functions. This particular command
 * specializes in storing key, values pairs in a ConcurrentHashMap.
 *
 * @param <T1> type parameter of key.
 * @param <T2> type parameter of value.
 */
public interface DerivativeCmd<T1, T2> extends ICmd {
  /**
   * Store the given key, value pair in a ConcurrentHashMap. Returns the computed result.
   * @param key input
   * @param operation function to compute value
   */
  T2 computeIfAbsent(T1 key, Function<T1, T2> operation);

  /**
   * Getter method for the concurrent hashmap that we're computing.
   *
   * @return map.
   */
  Map<T1, T2> getStorage();
}
