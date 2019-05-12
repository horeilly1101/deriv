package com.deriv.expression.cmd;

import com.deriv.expression.Expression;
import com.deriv.expression.Variable;
import com.deriv.util.Tuple2;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NullCacheCmd implements ICacheCmd {
  @Override
  public Expression computeIfAbsent(Tuple2<Expression, Variable> key,
                                    Function<Tuple2<Expression, Variable>, Expression> operation) {
    return operation.apply(key);
  }

  @Override
  public Map<Tuple2<Expression, Variable>, Expression> getStorage() {
    return new HashMap<>();
  };
}
