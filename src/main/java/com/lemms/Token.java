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
        return String.format("\n%s(%s)", type, value);
    }

}

enum TokenType {
    INT,
    STRING,
    BOOL,

    IDENTIFIER,

    PLUS,
    MINUS,
    MULTIPLICATION,
    DIVISION,
    MODULO,

    ASSIGNMENT,

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
    WHILE,
    IF,
    ELIF,
    ELSE,
}
