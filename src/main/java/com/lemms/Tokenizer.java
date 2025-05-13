package com.lemms;
import java.io.*;
import java.util.Scanner;

public class Tokenizer {

    private static ArrayList<Token> tokens;
    private static File file;

    public static ArrayList<Token> getTokens() {
        return tokens;
    }

    private void addToken(Type type) {
        // tokens.add(new Token(type, ));
    }

    private void getNextToken(String file) {
    }

    public Tokenizer(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
    }
}
