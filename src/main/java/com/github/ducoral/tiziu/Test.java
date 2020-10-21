package com.github.ducoral.tiziu;

public class Test {

    public static void main(String[] args) {

        String expr = "(7 > max(x:a.b, y:b)) + (' a:' + a.b + ', b:' + b)";

        Function max = params -> {
            Integer x = (Integer) params.getOrDefault("x", 0);
            Integer y = (Integer) params.getOrDefault("y", 0);
            return Math.max(x, y);
        };

        Object value = Tiziu.builder()
                .identifier("a.b", 6)
                .identifier("b", 4)
                .function("max", max)
                .build()
                .evaluate(expr);

        System.out.println(value);
    }
}