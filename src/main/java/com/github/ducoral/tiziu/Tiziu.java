package com.github.ducoral.tiziu;

import java.util.HashMap;
import java.util.Map;

import static com.github.ducoral.tiziu.Evaluator.Provider;

public final class Tiziu {

    private final Map<String, Object> functions = new HashMap<>();

    public Object evaluate(String expression, Map<String, Object> scope) {
        return new Parser(new Scanner(expression))
                .parseExpression()
                .evaluate(new Evaluator(getProvider(scope)));
    }

    private Provider getProvider(Map<String, Object> scope) {
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

    public Tiziu configure(Map<String, Object> functions) {
        this.functions.putAll(functions);
        return this;
    }

    public Map<String, Object> functions() {
        return functions;
    }

}