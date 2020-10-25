package com.github.ducoral.tiziu;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

class Evaluator {

    interface Provider {
        Function function(String name);
        Object value(String identifier);
    }

    private final Provider provider;

    Evaluator(Provider provider) {
        this.provider = provider;
    }

    Object evaluate(Tree.Binary binary) {
        switch (binary.operator) {
            case "&&" : return and(binary.left, binary.right);
            case "||" : return or(binary.left, binary.right);
            default:
                Object left = binary.left.evaluate(this);
                Object right = binary.right.evaluate(this);
                switch (binary.operator) {
                    case "+": return sum(left, right);
                    case "-": return subtract(left, right);
                    case "*": return multiply(left, right);
                    case "/": return divide(left, right);
                    case "==": return compare(left, right) == 0;
                    case "!=": return compare(left, right) != 0;
                    case ">": return compare(left, right) > 0;
                    case ">=": return compare(left, right) >= 0;
                    case "<": return compare(left, right) < 0;
                    case "<=": return compare(left, right) <= 0;
                    default: return null;
                }
        }
    }

    Object evaluate(Tree.Ternary ternary) {
        return isTrue(ternary.conditional.evaluate(this))
                ? ternary.expression1.evaluate(this)
                : ternary.expression2.evaluate(this);
    }

    Object evaluate(Tree.Function function) {
        Map<String, Object> parameters = new HashMap<>();
        for (Tree.Function.Param param : function.params)
            parameters.put(param.name, param.value.evaluate(this));
        return provider.function(function.name).call(parameters);
    }

    Object evaluate(Tree.NullLiteral literal) {
        return null;
    }

    Object evaluate(Tree.IdentifierLiteral identifier) {
        return provider.value(identifier.name);
    }

    Object evaluate(Tree.IntegerLiteral literal) {
        return literal.value;
    }

    Object evaluate(Tree.DecimalLiteral literal) {
        return literal.value;
    }

    Object evaluate(Tree.StringLiteral literal) {
        return literal.value;
    }

    Object evaluate(Tree.BooleanLiteral literal) {
        return literal.value;
    }

    private Object sum(Object left, Object right) {
        if (isNumber(left) && isNumber(right))
            return isDecimal(left) || isDecimal(right)
                    ? bigDecimal(left).add(bigDecimal(right))
                    : bigInteger(left).add(bigInteger(right));
        else
            return str(left) + str(right);
    }

    private Object subtract(Object left, Object right) {
        if (isNumber(left) && isNumber(right))
            return isDecimal(left) || isDecimal(right)
                ? bigDecimal(left).subtract(bigDecimal(right))
                : bigInteger(left).subtract(bigInteger(right));
        else
            return str(left).replace(str(right), "");
    }

    private Object divide(Object left, Object right) {
        if (isNumber(left) && isNumber(right))
            if (isZero(right))
                return BigInteger.ZERO;
            else
                return isDecimal(left) || isDecimal(right) || !bigInteger(left).mod(bigInteger(right)).equals(BigInteger.ZERO)
                        ? bigDecimal(left).divide(bigDecimal(right), 10, RoundingMode.HALF_UP)
                        : bigInteger(left).divide(bigInteger(right));
        else
            return str(left) + "/" + right;
    }

    private Object multiply(Object left, Object right) {
        if (isNumber(left) && isNumber(right))
            return isDecimal(left) || isDecimal(right)
                    ? bigDecimal(left).multiply(bigDecimal(right))
                    : bigInteger(left).multiply(bigInteger(right));
        else
            return str(left) + "*" + str(right);
    }

    private Object and(Tree left, Tree right) {
        return isTrue(left.evaluate(this)) && isTrue(right.evaluate(this));
    }

    private Object or(Tree left, Tree right) {
        return isTrue(left.evaluate(this)) || isTrue(right.evaluate(this));
    }

    private int compare(Object left, Object right) {
        if (isNumber(left) && isNumber(right))
            return bigDecimal(left).compareTo(bigDecimal(right));
        else
            return str(left).compareTo(str(right));
    }

    private boolean isNumber(Object left) {
        return left instanceof Number;
    }

    private boolean isDecimal(Object value) {
        return value instanceof Float
                || value instanceof Double
                || value instanceof BigDecimal;
    }

    private boolean isZero(Object value) {
        return value instanceof Number && ((Number) value).doubleValue() == 0;
    }

    private boolean isTrue(Object value) {
        if (value instanceof Boolean)
            return (Boolean) value;
        else if (value instanceof String)
            return !str(value).isEmpty();
        else if (isNumber(value))
            return !isZero(value);
        else
            return value != null;
    }

    private String str(Object value) {
        return String.valueOf(value);
    }

    private BigInteger bigInteger(Object left) {
        return new BigInteger(str(left));
    }

    private BigDecimal bigDecimal(Object left) {
        return new BigDecimal(str(left));
    }
}
