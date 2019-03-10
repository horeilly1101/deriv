package com.deriv.expression.cmd;

import com.deriv.util.ICmd;
import java.util.concurrent.ConcurrentHashMap;

/**
 * General command that helps us differentiate functions. This particular command
 * specializes in storing key, values paies in a ConcurrentHashMap.
 *
 * @param <T1> type parameter of key.
 * @param <T2> type parameter of value.
 */
public interface DerivativeCmd<T1, T2> extends ICmd {
  /**
   * Store the given key, value pair in a ConcurrentHashMap.
   * @param key input
   * @param value input
   */
  void store(T1 key, T2 value);

  /**
   * Getter method for the concurrent hashmap that we're computing.
   *
   * @return map.
   */
  ConcurrentHashMap getStorage();
}
