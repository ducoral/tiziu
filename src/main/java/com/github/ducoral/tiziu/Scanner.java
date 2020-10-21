package com.github.ducoral.tiziu;

import java.util.function.Predicate;

import static java.lang.Character.*;

class Scanner {

    static final int CHAR_EOF = 0;

    final String expression;

    int position = 0;

    Token token = Token.EOF;

    String lexeme = "";

    String error = "";

    Scanner(String expression) {
        this.expression = expression;
        scan();
    }

    void scan() {
        lexeme = "";
        error = "";

        while (isSpaceChar(current()))
            next();

        if (current() == CHAR_EOF)
            token = Token.EOF;
        else if (current() == '-' || isDigit(current()))
            parseMinusOperatorOrNumber();
        else if (current() == '_' || isLetter(current()))
            parseNullBooleanOrIdentifier();
        else if (current() == '\'' || current() == '"')
            parseString();
        else if (isCurrentOneOf("()[],?:")) {
            accept();
            switch (previous()) {
                case '(' : token = Token.OPEN_PARENTHESES; break;
                case ')' : token = Token.CLOSE_PARENTHESES; break;
                case '[' : token = Token.OPEN_BRACKETS; break;
                case ']' : token = Token.CLOSE_BRACKETS; break;
                case ',' : token = Token.COMMA; break;
                case '?' : token = Token.QUESTION_MARK; break;
                case ':' : token = Token.COLON; break;
            }
        } else if (isCurrentOneOf("+*/=!&|><")) {
            accept();
            if (previous() == '(')
                token = Token.OPEN_PARENTHESES;
            else if (previous() == ')')
                token = Token.CLOSE_PARENTHESES;
            else if (previous() == '=' && current() != '=')
                error("Operador '==' está incorreto!");
            else if (previous() == '!' && current() != '=')
                error("Operador '!=' está incorreto!");
            else if (previous() == '&' && current() != '&')
                error("Operador '&&' está incorreto!");
            else if (previous() == '|' && current() != '|')
                error("Operador '||' está incorreto!");
            else {
                if (isCurrentOneOf("=&|"))
                    accept();
                token = Token.OPERATOR;
            }
        } else
            error("O caractere '%s' inválido!", current());
    }

    private void parseMinusOperatorOrNumber() {
        accept();
        if (previous() == '-' && !isDigit(current()))
            token = Token.OPERATOR;
        else {
            acceptWhile(Character::isDigit);
            if (current() == '.') {
                accept();
                acceptWhile(Character::isDigit);
                if (isCurrentOneOf("eE")) {
                    accept();
                    if (isCurrentOneOf("-+"))
                        accept();
                    if (isDigit(current())) {
                        acceptWhile(Character::isDigit);
                        token = Token.DECIMAL;
                    } else
                        error("O valor '%s' é um número decimal inválido!", lexeme);
                } else
                    token = Token.DECIMAL;
            } else
                token = Token.INTEGER;
        }
    }

    private void parseNullBooleanOrIdentifier() {
        acceptWhile(ch -> ch == '_' || ch == '.' || isLetter(ch) || isDigit(ch));
        if (lexeme.equals("null"))
            token = Token.NULL;
        else if (lexeme.equals("true") || lexeme.equals("false"))
            token = Token.BOOLEAN;
        else
            token = Token.IDENTIFIER;
    }

    private void parseString() {
        char delimiter = current();
        next();
        while (current() != CHAR_EOF && current() != delimiter) {
            if (current() == '\\')
                next();
            accept();
        }
        if (current() == delimiter) {
            next();
            token = Token.STRING;
        } else
            error("String '%s' não fechada corretamente", lexeme);
    }

    private void next() {
        if (position < expression.length())
            position++;
    }

    private void accept() {
        lexeme += current();
        next();
    }

    private void acceptWhile(Predicate<Character> predicate) {
        while (predicate.test(current()))
            accept();
    }

    private boolean isCurrentOneOf(String chars) {
        return chars.indexOf(current()) > -1;
    }

    private char current() {
        return position < expression.length()
                ? expression.charAt(position)
                : CHAR_EOF;
    }

    private char previous() {
        return lexeme.isEmpty() ? 0 : lexeme.charAt(lexeme.length() - 1);
    }

    private void error(String message, Object... args) {
        token = Token.ERROR;
        error = String.format(message, args);
    }
}