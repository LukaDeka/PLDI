package com.lemms;
import com.lemms.Exceptions.LemmsParseError;
import com.lemms.Exceptions.LemmsRuntimeException;
import com.lemms.api.LemmsAPI;
import com.lemms.api.NativeFunction;
import com.lemms.interpreter.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    //für REPL
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        try {
            switch (args.length) {
                case 0:
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

                    // System.out.println(p.parse());
                    //Interpreter i = new Interpreter(p.parse());

                    //i.interpret();
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
        } catch (IOException e) { // <-- NEUER CATCH-BLOCK
            System.err.println("An I/O error occurred while reading from the console: " + e.getMessage());
            exit(1);
        }
    }

    private static void runRepl() throws IOException {
        LemmsAPI api = new LemmsAPI();
        Interpreter interpreter = new Interpreter(new ArrayList<>(), new HashMap<>());
        //Canvas Funktionen
        api.registerCanvasFunctions(interpreter);
        //ALLE nativen Funktionen (Standard + Canvas) von der API
        Map<String, NativeFunction> allNativeFunctions = api.getNativeFunctions();

        //Load-Funktion für die REPL (für files)
        NativeFunction loadFunction = (args) -> {
            if (args.isEmpty() || !(args.get(0) instanceof com.lemms.interpreter.object.LemmsString)) {
                throw new LemmsRuntimeException("load() requires a string argument for the file path.");
            }
            String path = ((com.lemms.interpreter.object.LemmsString) args.get(0)).value;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                interpreter.interpretLine(content); // Führe den Datei-Inhalt im aktuellen Interpreter aus
            } catch (IOException e) {
                throw new LemmsRuntimeException("Could not read file: " + path);
            }
            return null; // load() gibt nichts zurück
        };
        allNativeFunctions.put("load", loadFunction);

        interpreter.addNativeFunctions(allNativeFunctions);
        interpreter.initializeForRepl();

        System.out.println("Welcome to Lemms REPL!");
        System.out.println("Type code to be evaluated. Type 'exit()' to quit.");

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        StringBuilder multilineBuffer = new StringBuilder();
        String prompt = "> ";


        for (;;) {
            System.out.print(prompt);
            String line = reader.readLine();

            if (line == null) { // Ctrl+D
                break;
            }
            if (line.trim().equals("exit()")) {
                break;
            }

            multilineBuffer.append(line).append("\n");

            // Prüfe, ob die Eingabe jetzt vollständig ?
            if (isInputComplete(multilineBuffer.toString())) {
                // Wenn ja, führe Code aus
                String codeToRun = multilineBuffer.toString();

                // Leere Eingaben ignorieren (only ENTER pressed)
                if (!codeToRun.trim().isEmpty()) {
                    try {
                        // ganzen Block zum Interpreter passen
                        interpreter.interpretLine(codeToRun);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(ANSI_RED + "Syntax Error: Missing semicolon ';' at the end of the statement?" + ANSI_RESET);
                    } catch (LemmsParseError e) {
                        System.out.println(ANSI_RED + "Parse Error: " + e.getMessage() + ANSI_RESET);
                        if (e.getToken() != null) {
                            System.out.println(ANSI_RED + "[Near token: " + e.getToken() + " on line " + e.getToken().getLine() + "]" + ANSI_RESET);
                        }
                    } catch (LemmsRuntimeException e) {
                        System.out.println(ANSI_RED + "Runtime Error: " + e.getMessage() + ANSI_RESET);
                        if (e.getToken() != null) {
                            System.out.println(ANSI_RED + "[Line " + e.getToken().getLine() + "]" + ANSI_RESET);
                        }
                    } catch (Exception e) {
                        System.out.println(ANSI_RED + "An unexpected error occurred: " + e.getMessage() + ANSI_RESET);
                    }
                }

                // reset. f+rr nächste eingabe
                multilineBuffer.setLength(0);
                prompt = "> ";
            } else {
                // Wenn nicht vollständig, auf nächste Eingabe warten
                prompt = "... ";
            }
        }
        System.out.println("Bye!");
    }

    private static boolean isInputComplete(String source) {
        int braceDepth = 0;   // Für {}
        int parenDepth = 0;   // Für ()

        boolean inString = false;
        boolean inComment = false;
        char prevChar = '\0';

        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);

            // Zeilenkommentare zurücksetzen
            if (ch == '\n') {
                inComment = false;
                prevChar = ch;
                continue;
            }

            if (inComment) {
                prevChar = ch;
                continue;
            }

            if (inString) {
                if (ch == '"' && prevChar != '\\') {
                    inString = false;
                }
            } else {
                switch (ch) {
                    case '"':
                        inString = true;
                        break;
                    case '#': // Start eines Kommentars
                        inComment = true;
                        break;
                    case '{':
                        braceDepth++;
                        break;
                    case '}':
                        braceDepth--;
                        break;
                    case '(':
                        parenDepth++;
                        break;
                    case ')':
                        parenDepth--;
                        break;
                }
            }
            prevChar = ch;
        }

        // input ist unvollständig, wenn mind ein Scope (Klammer Ebene) noch offen
        // wenn user zu viele closing Klammer eingibt (z.B. braceDepth < 0),
        // dann Fehler an den Parser und trotzdem als "vollständig" in der REPL
        return braceDepth <= 0 && parenDepth <= 0;
    }
}