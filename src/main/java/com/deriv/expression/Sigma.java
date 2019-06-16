package com.deriv.expression;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.deriv.expression.Constant.constant;

// Sigma.of(3, 4, i -> Sigma.of(1, 5, j -> addID()))

public class Sigma implements Expression {
    private final int _beginRange;
    private final int _endRange;
    private final Function<Variable, ? extends Expression> _varMap;

    private Sigma(int beginRange, int endRange, Function<Variable, ? extends Expression> varMap) {
        this._beginRange = beginRange;
        this._endRange = endRange;
        this._varMap = varMap;
    }

    public static Expression of(int beginRange, int endRange, Function<Variable, ? extends Expression> varMap) {
        return new Sigma(beginRange, endRange, varMap);
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public Optional<Expression> evaluate(Variable var, Expression input) {
//        return of(_beginRange, _endRange, _varMap.andThen(x -> x.evaluate(var, input)));
        return Optional.empty();
    }

    @Override
    public Optional<Expression> differentiate(Variable var) {
        return Optional.empty();
    }

    @Override
    public Set<Variable> getVariables() {
        return null;
    }

    @Override
    public String toLaTex() {
        return null;
    }
}