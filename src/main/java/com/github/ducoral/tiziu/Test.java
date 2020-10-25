package com.github.ducoral.tiziu;

public class Test {

    public static void main(String[] args) {

        Function max = params -> {
            Integer x = (Integer) params.getOrDefault("x", 0);
            Integer y = (Integer) params.getOrDefault("y", 0);
            return Math.max(x, y);
        };

    }
}