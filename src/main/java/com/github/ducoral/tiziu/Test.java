package com.github.ducoral.tiziu;

public class Test {

    public static void main(String[] args) {

        String expr = "c == null && b != null";

        Object value = new Tiziu()
                .setIdentifier("a", 6)
                .setIdentifier("b", 4)
                .setFunction("max", (list) -> Math.max(Integer.parseInt(list.get(0).toString()), Integer.parseInt(list.get(1).toString())))
                .evaluate(expr);

        System.out.println(expr + ": " + value);

    }

}