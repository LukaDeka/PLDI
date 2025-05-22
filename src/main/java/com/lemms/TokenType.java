package com.lemms;

public enum TokenType {
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