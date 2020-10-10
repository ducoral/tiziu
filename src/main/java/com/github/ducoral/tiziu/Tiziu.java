package com.github.ducoral.tiziu;

import java.util.HashMap;
import java.util.Map;

import static com.github.ducoral.tiziu.Evaluator.Provider;

public final class Tiziu {

    private final Map<String, Object> identifiers = new HashMap<>();

    private final Map<String, Function> functions = new HashMap<>();

    private final Provider provider = new Provider() {
        @Override
        public Function function(String name) {
            return functions.getOrDefault(name, (list) -> null);
        }
        @Override
        public Object value(String identifier) {
            return identifiers.get(identifier);
        }
    };

    public Object evaluate(String expression) {
        Tree tree = new Parser(new Scanner(expression)).parseExpression();
        return tree.evaluate(new Evaluator(provider));
    }

    public Tiziu setIdentifier(String name, Object value) {
        identifiers.put(name, value);
        return this;
    }

    public Tiziu setIdentifiers(Map<String, Object> identifierMap) {
        this.identifiers.putAll(identifierMap);
        return this;
    }

    public Tiziu setFunction(String name, Function function) {
        functions.put(name, function);
        return this;
    }

    public Tiziu setFunctions(Map<String, Function> functions) {
        this.functions.putAll(functions);
        return this;
    }
}