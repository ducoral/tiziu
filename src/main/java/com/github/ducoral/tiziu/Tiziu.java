package com.github.ducoral.tiziu;

import java.util.HashMap;
import java.util.Map;

import static com.github.ducoral.tiziu.Evaluator.Provider;

public final class Tiziu {

    private final Map<Object, Object> functions = new HashMap<>();

    public Object evaluate(String expression, Map<Object, Object> scope) {
        return new Parser(new Scanner(expression))
                .parseExpression()
                .evaluate(new Evaluator(getProvider(scope)));
    }

    private Provider getProvider(Map<Object, Object> scope) {
        return new Provider() {
            public Function function(String name) {
                Object function = functions.get(name);
                return function instanceof Function ? (Function) function : params -> null;
            }
            public Object value(String identifier) {
                return scope.get(identifier);
            }
        };
    }

    public Tiziu configure(Map<Object, Object> functions) {
        this.functions.putAll(functions);
        return this;
    }

    public Map<Object, Object> functions() {
        return functions;
    }

}