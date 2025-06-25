package com.lemms;

public enum TokenType {
    INT,
    STRING,
    BOOL,

    IDENTIFIER,

    FUNCTION,
    RETURN,
    CLASS,

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
    BRACES_OPEN,
    BRACES_CLOSED,

    SEMICOLON,
    WHILE,
    IF,
    ELSE,

    COMMA,
    DOT
}