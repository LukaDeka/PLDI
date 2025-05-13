package com.lemms;
import java.io.*;
import java.util.Scanner;

public class Tokenizer {

    private static Token[] tokens;

    public static Token[] getTokens() {
        return tokens;
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

    public class Token {
        public Type type;
        public String value;
    }

    private void getNextToken(String file) {
    }

    public Tokenizer(File file) {
    }

    public static void main(String[] args) {
    }
}
