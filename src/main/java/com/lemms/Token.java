package com.lemms;

public class Token {
    int line;
    String value;
    String type;

    public Token(String value, int line, String type) {
        this.value = value;
        this.line = line;
        this.type = type;
    }

    public static String determineType(){
        return "?"; //typed not defined yet
    }

    @Override
    public String toString() {
        return String.format("(%s: %s (%s))",type, value, line);

    }
}
