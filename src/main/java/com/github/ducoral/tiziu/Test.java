package com.github.ducoral.tiziu;

import static com.github.ducoral.jutils.Core.map;

public class Test {

    public static void main(String[] args) {

        Function max = params -> {
            Integer x = (Integer) params.getOrDefault("x", 0);
            Integer y = (Integer) params.getOrDefault("y", 0);
            return Math.max(x, y);
        };

        Object value = new Tiziu()
                .configure(map().pair("max", max).done())
                .evaluate(
                        "(7 > max(x:a.b, y:b)) + (' a:' + a.b + ', b:' + b)",
                        map().pair("a.b", 10).pair("b", 20).done());

        System.out.println(value);
    }
}