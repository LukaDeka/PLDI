package com.lemms;

public class Token {
    private TokenType type;
    private String value = null;
    private int line;

    public Token(TokenType type) {
        this.type = type;
    }
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

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

    @Override
    public String toString() {
        if (value == null)
            return String.format("\n%s", type);
        return String.format("\n%s(%s)", type, value);
    }

    public String toString2() {
        if (value == null)
            return String.format("%s", type);
        return String.format("%s(%s)", type, value);
    }
}
