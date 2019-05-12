package com.deriv.expression.cmd;

import com.deriv.expression.Expression;
import com.deriv.expression.Variable;
import com.deriv.util.ICmd;
import com.deriv.util.Tuple2;
import java.util.Map;
import java.util.function.Function;

public interface ICacheCmd extends ICmd {
  /**
   * Store the given key, value pair in a Map. Returns the computed result.
   *
   * @param key input
   * @param operation function to compute value
   * @return computed result.
   */
  Expression computeIfAbsent(Tuple2<Expression, Variable> key,
                             Function<Tuple2<Expression, Variable>, Expression> operation);

  /**
   * Getter method for the map that we're computing.
   *
   * @return map.
   */
  public Map<Tuple2<Expression, Variable>, Expression> getStorage();

}
