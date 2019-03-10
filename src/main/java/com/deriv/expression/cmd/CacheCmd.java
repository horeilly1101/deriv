package com.deriv.expression.cmd;

import com.deriv.expression.Variable;
import com.deriv.util.Tuple;
import com.deriv.expression.Expression;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Command that stores calculated derivatives in a cache.
 */
public class CacheCmd implements DerivativeCmd<Tuple<Expression, Variable>, Expression> {
  /**
   * Our instance of the cache.
   */
  private ConcurrentHashMap<Tuple<Expression, Variable>, Expression> cache;

  /**
   * Public constructor for a CacheCmd.
   */
  public CacheCmd() { }

  @Override
  public void store(Tuple<Expression, Variable> key, Expression value) {

  }

  @Override
  public ConcurrentHashMap getStorage() {
    return null;
  }
}
