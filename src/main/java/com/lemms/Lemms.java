package com.lemms;

import com.lemms.interpreter.Interpreter;
import com.lemms.parser.Parser;

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

import com.lemms.SyntaxNode.StatementNode;

import java.io.File;
import java.util.ArrayList;

public class Lemms {
    public static void main(String[] args) {
        try {
            String sourcePath = "foo/bar/sourcePath.example (später mit command-line-args ersetzen)"; sourcePath = "src/main/resources/print_test1.txt";  //String sourcePath = args[0];
            File sourceFile = new File(sourcePath);

            //Verknüpfung: Tokenizer + Parser + Interpreter
            Tokenizer t = new Tokenizer(sourceFile);
            Parser p = new Parser(t.getTokens());
            Interpreter i = new Interpreter(p.parse());
            i.interpret();

            //System.out.println(p.getAST());

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("no path given");
        }
    }
}