package com.deriv.expression.cmd;

import com.deriv.expression.Constant;
import com.deriv.expression.Variable;
import com.deriv.util.Tuple2;
import com.deriv.expression.Expression;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Command that stores calculated derivatives in a cache. This then allows us to
 * "recompute" derivatives for free.
 */
public class CacheCmd implements ICacheCmd {
  /**
   * Our instance of the cache.
   */
  private Map<Tuple2<Expression, Variable>, Expression> cache;

  /**
   * Public constructor for a CacheCmd.
   */
  public CacheCmd() {
    // instantiate a HashMap
    cache = new HashMap<>();
  }

  @Override
  public Expression computeIfAbsent(Tuple2<Expression, Variable> key,
                                    Function<Tuple2<Expression, Variable>, Expression> operation) {
    // these operations are already constant time, so we won't waste space caching them
    if (key.getFirstItem() instanceof Variable || key.getFirstItem() instanceof Constant) {
      return operation.apply(key);
    }

    return cache.computeIfAbsent(key, operation); // keep passing along the cache
  }

  @Override
  public Map<Tuple2<Expression, Variable>, Expression> getStorage() {
    return cache;
  }
}
