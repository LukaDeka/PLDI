package com.lemms;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static com.lemms.TokenType.*;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

import static com.lemms.TokenType.*;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

public class Tokenizer {

    private int index = 0;
  

    private String input_file;

    private ArrayList<Token> tokens = new ArrayList<>();


    private void addToken(TokenType type, String text) {
        switch (type) {
            case PLUS, MINUS, MULTIPLICATION, DIVISION, MODULO, GT, LT, NOT,
                 BRACKET_OPEN, BRACKET_CLOSED, BRACES_OPEN, BRACES_CLOSED, SEMICOLON, ASSIGNMENT:
                index++;
                break;
            case GEQ, LEQ, NEQ, EQ, AND, OR:
                index += 2;
                break;
            case FUNCTION, IDENTIFIER, STRING, INT, BOOL, IF, ELSE, WHILE, RETURN: // Incrementation happens while reading
                break;
            default:
                throw new Error("Forgot to implement token type: " + type);
        }
        tokens.add(new Token(type, text));

    }

    private void scanToken() {
        Character ch = input_file.charAt(index);

        // Match single character tokens
        switch (ch) {
            case '(': addToken(BRACKET_OPEN, null); return;
            case ')': addToken(BRACKET_CLOSED, null); return;

            case '{': addToken(BRACES_OPEN, null); return;
            case '}': addToken(BRACES_CLOSED, null); return;

            case '-': addToken(MINUS, null); return;
            case '+': addToken(PLUS, null); return;
            case ';': addToken(SEMICOLON, null); return;
            case '*': addToken(MULTIPLICATION, null); return;
            case '%': addToken(MODULO, null); return;
            case ',': addToken(COMMA, null); return;
            case '#':
                index++;
                while (index + 1 < input_file.length() &&
                        (ch = input_file.charAt(index++)) != '\n');
                return;
        }

        // Match multi-char tokens
        switch (ch) {
            case '=':
                if (input_file.charAt(index + 1) == '=') {
                    addToken(EQ, null);
                    return;
                }
                addToken(ASSIGNMENT, null);
                return;
            case '<':
                if (input_file.charAt(index + 1) == '=') {
                    addToken(LEQ, null);
                    return;
                }
                addToken(LT, null);
                return;
            case '>':
                // >=
                if (input_file.charAt(index + 1) == '=') {
                    addToken(GEQ, null);
                    return;
                }
                addToken(GT, null);
                return;
            case '!':
                if (input_file.charAt(index + 1) == '=') {
                    addToken(NEQ, null);
                    return;
                }
                addToken(NOT, null);
                return;
            case '&':
                if (input_file.charAt(index + 1) == '&') {
                    addToken(AND, null);
                    return;
                }
                throw new Error("Illegal '&' in token");
            case '|':
                if (input_file.charAt(index + 1) == '|') {
                    addToken(OR, null);
                    return;
                }
        }

        // Identifier or keyword
        if (isAlphabetic(ch)) {
            // Read until alphanumerics or underscores end
            int begin_index = index;
            char next_char = ch;
            while (index < input_file.length() &&
                    (isAlphabetic(next_char) ||
                     isDigit(next_char) ||
                     next_char == '_')
            ) {
                next_char = input_file.charAt(index + 1);
                index++;
            }

            // Make new String from begin_index to index
            String new_token = input_file.substring(begin_index, index);

            // Check against known keywords: true, false, if, elif, else, while
            switch (new_token) {
                case "true", "false": addToken(BOOL, new_token); return;
                case "if": addToken(IF, null); return;                
                case "else": addToken(ELSE, null); return;
                case "while": addToken(WHILE, null); return;
                case "function": addToken(FUNCTION, null); return;
                case "return": addToken(RETURN, null); return;
            }

            // Else, it's an identifier
            addToken(IDENTIFIER, new_token);
            return;
        }

        // Handle strings
        if (ch == '"') {
            int begin_index = index++;
            while (index < input_file.length()) {
                char current_ch = input_file.charAt(index);
                if (current_ch == '\\') {
                    index += 2;
                    continue;
                }
                if (current_ch == '"') {
                    index++;
                    break;
                }
                index++;
            }
            String new_token = input_file.substring(begin_index, index);
            new_token = new_token.replace("\\", "");
            addToken(STRING, new_token);
            return;
        }

        // Handle numbers
        if (isDigit(ch)) {
            int begin_index = index;
            while (index < input_file.length() &&
                    (isDigit(input_file.charAt(index)) ||
                     input_file.charAt(index) == '_')
            ) { // Allow formats like 1_000_000 for readability
                index++;
            }
            String new_token = input_file.substring(begin_index, index);
            new_token = new_token.replace("_", "");
            addToken(INT, new_token);
            return;
        }

        if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
            index++;
            return;
        }

        throw new Error("Illegal character in token: " + ch);
    }



    // For REPL
    public Tokenizer(String program) {
        input_file = program;
        while (index < input_file.length()) {
            scanToken();
        }
    }

    // For reading files
    public Tokenizer(File file) {
        try {
            input_file = new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (index < input_file.length()) {
            scanToken();
        }
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer(new File("src/main/resources/example1.txt"));
        ArrayList<Token> tokens = tokenizer.getTokens();
        System.out.println(tokens);
    }
}
