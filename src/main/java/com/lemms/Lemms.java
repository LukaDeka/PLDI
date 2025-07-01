package com.lemms;
import com.lemms.Exceptions.LemmsParseError;
import com.lemms.Exceptions.LemmsRuntimeException;
import com.lemms.SyntaxNode.StatementNode;
import com.lemms.api.LemmsAPI;
import com.lemms.interpreter.Interpreter;
import com.lemms.parser.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

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

public class Lemms {
    private static boolean hadError = false; // to avoid exiting REPL on mistake
    public static void main(String[] args) {
        try {
            switch (args.length) {
                case 0:
                    // Launch REPL
                    System.out.println("running REPL!");
                    runRepl();
                    break;
                case 1:
                    String sourcePath = args[0];
                    File sourceFile = new File(sourcePath);
                    if (!sourceFile.isFile()) {
                        System.out.println("Error, source file does not exist: " + sourcePath);
                        exit(1);
                    }


                    //Verknüpfung: Tokenizer + Parser + Interpreter
                    //Tokenizer t = new Tokenizer(sourceFile);
                    //Parser p = new Parser(t.getTokens());
                    //StaticCanvas.addPrimitives(api, );
                    LemmsAPI api = new LemmsAPI();
                    api.setScript(sourcePath);
                    api.interpret();

                    if (hadError) exit(1); //for REPL

                    break;
                default:
                    System.out.println(
                        "Error, too many arguments!\n" +
                        "Hint: Call lemms without arguments for the REPL or\n" +
                        "      Pass a path to the file you want to interpret.\n"
                    );
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No path given");
        } catch (LemmsRuntimeException error) {
            System.err.println("Runtime Error: " + error.getMessage());
            System.err.println("[Line " + error.getToken().getLine() + "]");
        } catch (LemmsParseError error) {
            System.err.println("Parse Error: " + error.getMessage());
            System.err.println("[Line " + error.getToken().getLine() + "]");
        }
    }

    private static void runRepl() {
        System.out.println("Welcome to Lemms REPL v0.1");
        System.out.println("Type 'exit' to quit.");

        // Create a single, long-lived interpreter for the session
        Interpreter interpreter = new Interpreter(new ArrayList<>());
        interpreter.initializeGlobalScope(); // Set up the environment

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();

            // Check for exit condition
            if (line == null || line.equalsIgnoreCase("exit")) {
                break;
            }

            // If the user just hits enter, continue
            if (line.isBlank()) {
                continue;
            }

            // each line is a mini-program.
            // wwe handle errors inside the loop
            // to keep the REPL alive.
            try {
                Tokenizer tokenizer = new Tokenizer(line);
                List<Token> tokens = tokenizer.getTokens();
                Parser parser = new Parser(tokens);
                List<StatementNode> statements = parser.parse();

                interpreter.executeStatements(statements);

            } catch (IndexOutOfBoundsException e) {

            } catch (LemmsParseError e) {
                // Report error but don't exit
                reportParseError(e);
            } catch (LemmsRuntimeException e) {
                // Report error but don't exit
                reportRuntimeError(e);
            } catch (Exception e) {
                // Catch any other unexpected errors to keep the REPL from crashing
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); // Good for debugging
            }
        }
        System.out.println("Bye!");
    }

    // helpermethods - consistent error reporting
    private static void reportRuntimeError(LemmsRuntimeException error) {
        System.err.println("Runtime Error: " + error.getMessage());
        if (error.getToken() != null) {
            System.err.println("[Line " + error.getToken().getLine() + "]");
        }
        hadError = true;
    }

    private static void reportParseError(LemmsParseError error) {
        System.err.println("Parse Error: " + error.getMessage());
        if (error.getToken() != null) {
            System.err.println("[Line " + error.getToken().getLine() + "]");
        }
        hadError = true;
    }

}