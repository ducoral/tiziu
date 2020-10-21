package com.github.ducoral.tiziu;

import java.util.HashMap;
import java.util.Map;

import static com.github.ducoral.tiziu.Evaluator.Provider;

public final class Tiziu {

    private final Provider provider;

    public interface Builder {
        Builder identifier(String name, Object value);
        Builder identifiers(Map<String, Object> map);
        Builder function(String name, Function function);
        Builder functions(Map<String, Function> map);
        Tiziu build();
    }

    public static Builder builder() {

        return new Builder() {

            final Map<String, Object> identifiers = new HashMap<>();

            final Map<String, Function> functions = new HashMap<>();

            public Builder identifier(String name, Object value) {
                identifiers.put(name, value);
                return this;
            }

            public Builder identifiers(Map<String, Object> map) {
                identifiers.putAll(map);
                return this;
            }

            public Builder function(String name, Function function) {
                functions.put(name, function);
                return this;
            }

            public Builder functions(Map<String, Function> map) {
                functions.putAll(map);
                return this;
            }

            public Tiziu build() {
                return new Tiziu(new Provider() {
                    public Function function(String name) {
                        return functions.getOrDefault(name, (list) -> null);
                    }
                    public Object value(String identifier) {
                        return identifiers.get(identifier);
                    }
                });
            }
        };
    }

    public Object evaluate(String expression) {
        Scanner scanner = new Scanner(expression);
        Parser parser = new Parser(scanner);
        Evaluator evaluator = new Evaluator(provider);
        return parser
                .parseExpression()
                .evaluate(evaluator);
    }

    private Tiziu(Provider provider) {
        this.provider = provider;
    }
}