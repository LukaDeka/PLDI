package com.lemms;

public class Token {
    private Type type;
    private String value;
    private int lineNum;

    public String getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public Type getLineNum() {
        return lineNum;
    }

    public enum Type {
        INT,
        STRING,
        BOOL,

        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        MODULO,

        EQ,
        NEQ,
        GEQ,
        LEQ,
        GT,
        LT,

        AND,
        OR,
        NOT,

        BRACKET_OPEN,
        BRACKET_CLOSED,
        CURLY_OPEN,
        CURLY_CLOSED,

        SEMICOLON,
    }

    public Token(Type type, String value, int line) {
        this.value = value;
        this.line = line;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s ", value);
    }
}
