package com.lemms;

import static com.lemms.TokenType.STRING;

public class Token {
    private final TokenType type;
    private final String value;
    private final int line;

    // public Token(TokenType type) {
    // this.type = type;
    // }

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
        this.line = 0;
    }

    public Token(TokenType type) {
        this.type = type;
        this.value = null;
        this.line = 0;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        if (value == null)
            return String.format("%s", type);

        String format = "";
        if (type == STRING) {
            format = String.format("%s(\"%s\")", type, value);
        } else {
            format = String.format("%s(%s)", type, value);
        }

        return format;
    }
}
