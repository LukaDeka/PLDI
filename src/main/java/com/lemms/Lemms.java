package com.lemms;
import com.lemms.Exceptions.LemmsParseError;
import com.lemms.Exceptions.LemmsRuntimeException;
import com.lemms.interpreter.Interpreter;
import com.lemms.parser.Parser;
import java.io.File;
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
    public static void main(String[] args) {
        try {
            switch (args.length) {
                case 0:
                    // Launch REPL
                    System.out.println("REPL not yet implemented. Bye!");
                    break;
                case 1:
                    String sourcePath = args[0];
                    File sourceFile = new File(sourcePath);
                    if (!sourceFile.isFile()) {
                        System.out.println("Error, source file does not exist: " + sourcePath);
                        exit(1);
                    }


                    //Verknüpfung: Tokenizer + Parser + Interpreter
                    Tokenizer t = new Tokenizer(sourceFile);
                    Parser p = new Parser(t.getTokens());
                    // System.out.println(p.parse());
                    Interpreter i = new Interpreter(p.parse());

                    i.interpret();
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
}