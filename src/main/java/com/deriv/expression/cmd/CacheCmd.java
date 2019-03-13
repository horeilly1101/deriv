package com.deriv.expression.cmd;

import com.deriv.expression.Variable;
import com.deriv.util.Tuple;
import com.deriv.expression.Expression;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Command that stores calculated derivatives in a cache. This then allows us to
 * "recompute" derivatives for free.
 */
public class CacheCmd implements DerivativeCmd<Tuple<Expression, Variable>, Expression> {
  /**
   * Our instance of the cache.
   */
  private Map<Tuple<Expression, Variable>, Expression> cache;

  /**
   * Public constructor for a CacheCmd.
   */
  public CacheCmd() {
    // instantiate a HashMap
    cache = new HashMap<>();
  }

  @Override
  public Expression computeIfAbsent(Tuple<Expression, Variable> key, Function<Tuple<Expression, Variable>, Expression> operation) {
    cache.computeIfAbsent(key, operation); // keep passing along the cache
    return cache.get(key);
  }

  @Override
  public Map<Tuple<Expression, Variable>, Expression> getStorage() {
    return cache;
  }
}
