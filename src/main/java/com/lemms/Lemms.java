package com.lemms;

import com.lemms.interpreter.Interpreter;

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

public class Lemms {
    public static void main(String[] args) {
        try {
            //String sourcePath = args[0];
            String sourcePath = "foo/bar/sourcePath.example (später mit command-line-args ersetzen)";

            Tokenizer t = new Tokenizer(sourcePath);
            Parser p = new Parser(t.getTokens());
            Interpreter i = new Interpreter(p.getAST());

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("no path given");
        }
    }
}