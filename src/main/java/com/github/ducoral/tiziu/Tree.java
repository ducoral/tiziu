package com.github.ducoral.tiziu;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

abstract class Tree {

    abstract Object evaluate(Evaluator evaluator);

    static class Binary extends Tree {
        final Tree left;
        final String operator;
        final Tree right;

        Binary(Tree left, String operator, Tree right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        Object evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
    }

    static class Ternary extends Tree {
        final Tree conditional;
        final Tree expression1;
        final Tree expression2;

        Ternary(Tree conditional, Tree expression1, Tree expression2) {
            this.conditional = conditional;
            this.expression1 = expression1;
            this.expression2 = expression2;
        }

        @Override
        Object evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
    }

    static class Function extends Tree {
        final String name;
        final List<Tree> params;

        Function(String name, List<Tree> params) {
            this.name = name;
            this.params = params;
        }

        @Override
        Object evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
    }

    static class NullLiteral extends Tree {
        @Override
        Object evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
    }

    static class IdentifierLiteral extends Tree {
        final String name;

        IdentifierLiteral(String lexeme) {
            this.name = lexeme;
        }

        @Override
        Object evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
    }

    static class IntegerLiteral extends Tree {
        final BigInteger value;

        IntegerLiteral(String lexeme) {
            value = new BigInteger(lexeme);
        }

        @Override
        Object evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
    }

    static class DecimalLiteral extends Tree {
        final BigDecimal value;

        DecimalLiteral(String lexeme) {
            value = new BigDecimal(lexeme);
        }

        @Override
        Object evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
    }

    static class StringLiteral extends Tree {
        final String value;

        StringLiteral(String lexeme) {
            value = lexeme;
        }

        @Override
        Object evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
    }

    static class BooleanLiteral extends Tree {
        final Boolean value;

        BooleanLiteral(String lexeme) {
            value = "true".equals(lexeme);
        }

        @Override
        Object evaluate(Evaluator evaluator) {
            return evaluator.evaluate(this);
        }
    }
}