package com.lemms;

public class Token {
    private TokenType type;
    private String value;
    private int line;

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public Token(String value, int line, TokenType type) {
        this.value = value;
        this.line = line;
        this.type = type;
    }

    public static TokenType determineType(String value){
        //TODO: logic for determine Token-Type based on String value (strong recommendation Pattern Matching)
        //TODO: reevaluate Type - determination needed? eventually redundant due to parser and specified nodes
        return TokenType.UNDEFINED;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", value, line);
    }

    //TODO: reevaluate Type - determination needed? eventually redundant due to parser and specified nodes
    public enum TokenType {
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

        UNDEFINED
    }
}
