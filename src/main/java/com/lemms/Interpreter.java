package com.lemms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Interpreter {

    public static void runFile(String path) throws IOException {

    }

    // private static void runFile(String path) throws IOException {
    // byte[] bytes = Files.readAllBytes(Paths.get(path));
    // run(new String(bytes, Charset.defaultCharset()));
    // }
    //
    // private static void run(String program) {
    // Tokenizer tokenizer = new Tokenizer(program);
    // List<Token> tokens = tokenizer.scanTokens();
    //
    // for (Token token : tokens) {
    // System.out.println(token);
    // }
    // }
    //
    // private static void runPrompt() throws IOException {
    // InputStreamReader input = new InputStreamReader(System.in);
    // BufferedReader reader = new BufferedReader(input);
    //
    // for (;;) {
    // System.out.print("lemms> ");
    // String line = reader.readLine();
    // if (line == null) break;
    // run(line);
    // }
    // }

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Usage: lemms ./script");
            System.exit(1);
        } else if (args.length == 1) {
            try {
                runFile(args[0]);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // runPrompt();
        }
    }
}
