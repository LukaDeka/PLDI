package com.lemms;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenizerTest {

    @Test
    public void test1() {
        Tokenizer tokenizer = new Tokenizer(new File("src/main/resources/example1.lemms"));
        ArrayList<Token> tokens = tokenizer.getTokens();

        String expected = """
[IDENTIFIER(funny_var2), ASSIGNMENT, INT(100100134), SEMICOLON, IDENTIFIER(funny_var), ASSIGNMENT, STRING("epic string"string"), SEMICOLON, IF, BRACKET_OPEN, IDENTIFIER(funny_var2), NEQ, INT(100100134), BRACKET_CLOSED, BRACES_OPEN, IDENTIFIER(print), BRACKET_OPEN, STRING("awman"), BRACKET_CLOSED, SEMICOLON, BRACES_CLOSED, IF, BRACKET_OPEN, INT(3), LEQ, INT(4), BRACKET_CLOSED, BRACES_OPEN, BRACES_CLOSED, IDENTIFIER(whiletrue), ASSIGNMENT, BOOL(true), SEMICOLON, IDENTIFIER(whilefalse), ASSIGNMENT, BOOL(false), SEMICOLON, IDENTIFIER(string____test_____), ASSIGNMENT, STRING("hi"), SEMICOLON, IDENTIFIER(print), BRACKET_OPEN, IDENTIFIER(string____test_____), BRACKET_CLOSED, SEMICOLON]
""";
        String normalizedExpected = normalize(expected);
        String normalizedActual = normalize(tokens.toString());
        assertEquals(normalizedExpected, normalizedActual);
    }

    @Test
    public void test2() {
        Tokenizer tokenizer = new Tokenizer(new String("funny_var1=100_100_134"));
        ArrayList<Token> tokens = tokenizer.getTokens();

        String expected = """
[IDENTIFIER(funny_var1), ASSIGNMENT, INT(100100134)]
""";
        String normalizedExpected = normalize(expected);
        String normalizedActual = normalize(tokens.toString());
        assertEquals(normalizedExpected, normalizedActual);
    }

    @Test
    public void test3() {
        Tokenizer tokenizer = new Tokenizer(new File("src/main/resources/example3.lemms"));
        ArrayList<Token> tokens = tokenizer.getTokens();

        String expected = """
[IDENTIFIER(ifvar1_), ASSIGNMENT, MINUS, INT(4), PLUS, INT(23), SEMICOLON, WHILE, BRACKET_OPEN, IDENTIFIER(ifvar1_), EQ, BOOL(false), BRACKET_CLOSED, BRACES_OPEN, IDENTIFIER(print), BRACKET_OPEN, STRING("
w
"
o
"
w"), BRACKET_CLOSED, SEMICOLON, BRACES_CLOSED, IDENTIFIER(print), BRACKET_OPEN, IDENTIFIER(ifvar1_), BRACKET_CLOSED, SEMICOLON]
""";
        String normalizedExpected = normalize(expected);
        String normalizedActual = normalize(tokens.toString());
        assertEquals(normalizedExpected, normalizedActual);
    }

    @Test
    public void test4() {
        Tokenizer tokenizer = new Tokenizer(new String(""));
        ArrayList<Token> tokens = tokenizer.getTokens();

        String expected = """
[]
""";
        String normalizedExpected = normalize(expected);
        String normalizedActual = normalize(tokens.toString());
        assertEquals(normalizedExpected, normalizedActual);
    }

    @Test
    public void commentTest1() {
        Tokenizer tokenizer = new Tokenizer(new String("#cool comment"));
        ArrayList<Token> tokens = tokenizer.getTokens();

        String expected = """
[]
""";
        String normalizedExpected = normalize(expected);
        String normalizedActual = normalize(tokens.toString());
        assertEquals(normalizedExpected, normalizedActual);
    }

    @Test
    public void commentTest2() {
        Tokenizer tokenizer = new Tokenizer(new String("#cool comment#"));
        ArrayList<Token> tokens = tokenizer.getTokens();

        String expected = """
[]
""";
        String normalizedExpected = normalize(expected);
        String normalizedActual = normalize(tokens.toString());
        assertEquals(normalizedExpected, normalizedActual);
    }

    @Test
    public void commentTest3() {
        Tokenizer tokenizer = new Tokenizer(new String("#"));
        ArrayList<Token> tokens = tokenizer.getTokens();

        String expected = """
[]
""";
        String normalizedExpected = normalize(expected);
        String normalizedActual = normalize(tokens.toString());
        assertEquals(normalizedExpected, normalizedActual);
    }

    @Test
    public void commentTest4() {
        Tokenizer tokenizer = new Tokenizer(new String("""
# this part is ignored
var1 = 4+3;#
"""));
        ArrayList<Token> tokens = tokenizer.getTokens();

        String expected = """
[IDENTIFIER(var1), ASSIGNMENT, INT(4), PLUS, INT(3), SEMICOLON]
""";
        String normalizedExpected = normalize(expected);
        String normalizedActual = normalize(tokens.toString());
        assertEquals(normalizedExpected, normalizedActual);
    }

    private String normalize(String input) {
        return input
                .replace("\r\n", "\n") // normalize Windows line endings
                .lines()
                .map(String::stripTrailing)
                .collect(Collectors.joining("\n"))
                .strip(); // remove leading/trailing newlines or spaces
    }
}
