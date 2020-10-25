package com.github.ducoral.tiziu;

import java.util.HashMap;
import java.util.Map;

import static com.github.ducoral.tiziu.Evaluator.Provider;

public final class Tiziu {

    private final Map<String, Function> functions = new HashMap<>();

    public Object evaluate(String expression, Map<String, Function> scope) {
        return new Parser(new Scanner(expression))
                .parseExpression()
                .evaluate(new Evaluator(getProvider(scope)));
    }

    private Provider getProvider(Map<String, Function> scope) {
        return new Provider() {
            public Function function(String name) {
                Function function = functions.get(name);
                return function == null ? params -> null : function;
            }
            public Object value(String identifier) {
                return scope.get(identifier);
            }
        };
    }

    public Tiziu configure(Map<String, Function> functions) {
        this.functions.putAll(functions);
        return this;
    }

    public Map<String, Function> functions() {
        return functions;
    }

}