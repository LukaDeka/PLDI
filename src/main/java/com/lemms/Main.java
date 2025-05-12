package com.lemms;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
     * Phases of Interpreter
     * 1. Lexing (Tokenizing)
     *       raw source code (Strings) gets disassembled in smaller units (Tokens)
     *       different types of tokens, e.g. Numbers (123), Operators (+ - * /), identifier (variablenames), etc..
     *       result: sequence of many different tokens
     * 2. Parsing
     *       analysing the sequence of tokens -> correct syntax and grammar?
     *       with those tokens a datastructure will be created, which represent the structure of the program
     *       usually a tree is used within each node represent Operators, Values, Variables etc..
     *       result: abstract syntax tree (AST)
     *       (eventually it's just a list of tokens - for simple Interpreter)
     *
     * 3. Evaluation
     *       now the "Interpreter" travers through the AST
     *       and performs/execute different actions based on the node/token
     *
     * */


    /*
     * Schlüsselwörter: var, if, else, etc.
     * Operatoren: =, +, -, etc.
     * Variablen: x, y, z, etc.
     * Werte: 4, 9, true, false, etc.
     * Blöcke: {, }, etc.
     * Sonstige: z.B. Semikolons ;
     *
     */

public class Main {
    public static void main(String[] args) {


        //Quellcode-File auslesen
        Path path = Paths.get("src/main/resources/exampleCode2.txt"); //Alternativ mit ClassLoader

        try (Stream<String> lines = Files.lines(path)) { //.readString(path); //.readAllBytes(path); //.readAllLines(path);

            AtomicInteger lineCounter = new AtomicInteger(0);
            ArrayList<Token> rawTokens = lines
                .map(line -> line.replaceAll("([{}=;])"," $1 ").trim().split("\\s+"))
                .flatMap(Arrays::stream)
                .map(value-> new Token(value, lineCounter.getAndIncrement(), Token.determineType()))
                //.filter(token -> !token.isEmpty()) //empty lines
                .collect(Collectors.toCollection(ArrayList::new));


            System.out.println(rawTokens);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}