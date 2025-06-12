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
    private int line = 1;

    private String input_file;
    private ArrayList<Token> tokens = new ArrayList<>();


    private void addToken(TokenType type, String text) {

        tokens.add(new Token(type, text, line));
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void scanToken() {
        char ch = input_file.charAt(index);

        // 1. Whitespace & Kommentare übverspringen
        switch (ch) {
            case ' ':
            case '\r':
            case '\t':
                index++;
                return; // einfach überspringen
            case '\n':
                this.line++;
                index++;
                return; // increment line counter
        }

//        switch (ch) {
//            case PLUS, MINUS, MULTIPLICATION, DIVISION, MODULO, GT, LT, NOT,
//                 BRACKET_OPEN, BRACKET_CLOSED, BRACES_OPEN, BRACES_CLOSED, SEMICOLON, ASSIGNMENT:
//                index++;
//                break;
//            case GEQ, LEQ, NEQ, EQ, AND, OR:
//                index += 2;
//                break;
//            case IDENTIFIER, STRING, INT, BOOL, IF, ELSE, WHILE: // Incrementation happens while reading
//                break;
//            default:
//                throw new Error("Forgot to implement token type: " + type);
//        }

        // 2. Single Character Tokens
        switch (ch) {
            case '(': index++; addToken(BRACKET_OPEN); return;
            case ')': index++; addToken(BRACKET_CLOSED); return;

            case '{': index++; addToken(BRACES_OPEN); return;
            case '}': index++; addToken(BRACES_CLOSED); return;

            case '-': index++; addToken(MINUS); return;
            case '+': index++; addToken(PLUS); return;
            case ';': index++; addToken(SEMICOLON); return;
            case '*': index++; addToken(MULTIPLICATION); return;
            case '/': index++; addToken(DIVISION); return;
            case '%': index++; addToken(MODULO); return;
            case ',': index++; addToken(COMMA); return;
        }




        // 3. Match multi-char tokens
        switch (ch) {
            case '=':
                if (input_file.charAt(index + 1) == '=') { // && index + 1 < input_file.length() ?
                    index += 2;
                    addToken(EQ);
                } else {
                    index++;
                    addToken(ASSIGNMENT);
                }
                return;

            case '!':
                if (input_file.charAt(index + 1) == '=') {
                    index += 2;
                    addToken(NEQ);
                } else {
                    index++;
                    addToken(NOT);
                }
                return;

            case '<':
                if (input_file.charAt(index + 1) == '=') {
                    index += 2;
                    addToken(LEQ);
                } else {
                    index++;
                    addToken(LT);
                }
                return;

            case '>': // >=
                if (input_file.charAt(index + 1) == '=') {
                    index += 2;
                    addToken(GEQ);
                } else {
                    index++;
                    addToken(GT);
                }
                return;

            case '&':
                if (input_file.charAt(index + 1) == '&') {
                    addToken(AND);
                    return;
                } else {
                    throw new Error("Illegal '&' in token");
                }
            case '|':
                if (input_file.charAt(index + 1) == '|') {
                    index += 2;
                    addToken(OR);
                    return;
                } //what if single "|"
        }

        // 4. Longer Tokens: Strings, Numbers, Identifier
        // 4.1 Identifier or Keyword
        if (isAlphabetic(ch)) {
            int begin_index = index;
            // Read until alphanumerics or underscores end
            while (index < input_file.length() &&
                    (isAlphabetic(input_file.charAt(index)) ||
                     isDigit(input_file.charAt(index)) || input_file.charAt(index) == '_')) {
                index++;
            }

            String new_token = input_file.substring(begin_index, index);

            // Keywords or Identifier?
            switch (new_token) {
                case "true", "false": addToken(BOOL, new_token); return;
                case "if": addToken(IF); return;
                case "else": addToken(ELSE); return;
                case "while": addToken(WHILE); return;
                default: addToken(IDENTIFIER, new_token); return;
            }
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

            String number = input_file
                    .substring(begin_index, index)
                    .replace("_", "");
            addToken(INT, number);
            return;
        }

        // Handle strings
        if (ch == '"') {
            int begin_index = index+1;
            index++;
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



        throw new Error("Illegal character '" + ch +"' on line " +line);
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
