package com.github.ducoral.tiziu;

import com.github.ducoral.jutils.Scanner;
import com.github.ducoral.jutils.Scanner.Token;

import java.util.ArrayList;
import java.util.List;

import static com.github.ducoral.jutils.Scanner.Token.*;

class Parser {

    private final Scanner scanner;

    Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    Tree parseExpression() {
        Tree expr = parseLogical();
        accept(EOF);
        return expr;
    }

    Tree parseLogical() {
        Tree expr = parseRelational();
        while (isLogicalOperator(scanner.lexeme))
            expr = new Tree.Binary(expr, accept(OPERATOR), parseRelational());
        return expr;
    }

    Tree parseRelational() {
        Tree expr = parseArithmetic();
        while (isRelationalOperator(scanner.lexeme))
            expr = new Tree.Binary(expr, accept(OPERATOR), parseArithmetic());
        return expr;
    }

    Tree parseArithmetic() {
        Tree expr = parseMultiplyDivide();
        while (isSumSubtractOperator(scanner.lexeme))
            expr = new Tree.Binary(expr, accept(OPERATOR), parseMultiplyDivide());
        return expr;
    }

    Tree parseMultiplyDivide() {
        Tree expr = parseTerm();
        while (isMultiplyDivideOperator(scanner.lexeme))
            expr = new Tree.Binary(expr, accept(OPERATOR), parseTerm());
        return expr;
    }

    private Tree parseTerm() {
        switch (current()) {
            case NULL:
                accept(NULL);
                return new Tree.NullLiteral();
            case INTEGER: return new Tree.IntegerLiteral(accept(INTEGER));
            case DECIMAL: return new Tree.DecimalLiteral(accept(DECIMAL));
            case STRING: return new Tree.StringLiteral(accept(STRING));
            case BOOLEAN: return new Tree.BooleanLiteral(accept(BOOLEAN));
            case IDENTIFIER:
                StringBuilder ident = new StringBuilder(accept(IDENTIFIER));
                if (isCurrent(OPEN_PARENTHESES)) {
                    List<Tree.Function.Param> params = new ArrayList<>();
                    accept(OPEN_PARENTHESES);
                    if (!isCurrent(CLOSE_PARENTHESES)) {
                        String name = accept(IDENTIFIER);
                        accept(COLON);
                        params.add(new Tree.Function.Param(name, parseLogical()));
                        while (isCurrent(COMMA)) {
                            accept(COMMA);
                            name = accept(IDENTIFIER);
                            accept(COLON);
                            params.add(new Tree.Function.Param(name, parseLogical()));
                        }
                    }
                    accept(CLOSE_PARENTHESES);
                    return new Tree.Function(ident.toString(), params);
                } else {
                    while (isCurrent(DOT))
                        ident.append(accept(DOT)).append(accept(IDENTIFIER));
                    return new Tree.IdentifierLiteral(ident.toString());
                }
            case OPEN_PARENTHESES:
                accept(OPEN_PARENTHESES);
                Tree expr = parseLogical();
                accept(CLOSE_PARENTHESES);
                return expr;
            case OPEN_BRACKETS:
                accept(OPEN_BRACKETS);
                Tree conditional = parseLogical();
                accept(QUESTION_MARK);
                Tree expression1 = parseLogical();
                accept(COLON);
                Tree expression2 = parseLogical();
                accept(CLOSE_BRACKETS);
                return new Tree.Ternary(conditional, expression1, expression2);
            default:
                throw new RuntimeException("Erro de sintaxe. Token inválido: " + scanner.lexeme);
        }
    }

    private boolean isCurrent(Token token) {
        return current() == token;
    }

    private boolean isSumSubtractOperator(String operator) {
        return isOneOf(operator, "+", "-");
    }

    private boolean isMultiplyDivideOperator(String operator) {
        return isOneOf(operator, "*", "/");
    }

    private boolean isRelationalOperator(String operator) {
        return isOneOf(operator, "==", "!=", ">", ">=", "<", "<=");
    }

    private boolean isLogicalOperator(String operator) {
        return isOneOf(operator, "&&", "||");
    }

    private boolean isOneOf(String operator, String... operators) {
        for (String op : operators)
            if (operator.equals(op))
                return true;
        return false;
    }

    private Token current() {
        return scanner.token;
    }

    private String accept(Token token) {
        if (scanner.token == token) {
            String lexeme = scanner.lexeme;
            scanner.scan();
            return lexeme;
        } else
            throw new RuntimeException("Erro de sintaxe: era esperado o símbolo " + token);
    }
}
