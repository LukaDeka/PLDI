package com.lemms;

public class Token {
    private final TokenType type;
    private final String value;
    private final int line;

//    public Token(TokenType type) {
//        this.type = type;
//    }

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
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

    public Token(String value, int line, TokenType type) {
        this.value = value;
        this.line = line;
        this.type = type;
    }


    public String toString2() {
        if (value == null)
            return String.format("\n%s", type);
        return String.format("\n%s(%s)", type, value);
    }
    @Override
    public String toString() {
        if (value == null)
            return String.format("%s", type);
        return String.format("%s(%s)", type, value);
    }
}
