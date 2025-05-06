package com.lemms;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Interpreter {

    /* Prints the @error_message that happened on the line @line_num,
     * as well as the line itself @line_str with the
     * position of the error (@char_num)
     *
     * The function does NOT exit
     */
    private static void printError(
        int line_num, int char_num, String line_str, String error_message
    ) {
        String line_num_str = "  " + line_num.toString + " | "
        String padding = 
            StringUtils.repeat(" ", line_num_str.length());

        System.err.println("Error: " + error_message);
        System.err.println(line_num_str + line);
        System.err.println(padding + "^--here");
    }

  //   private static void runFile(String path) throws IOException {
  //       byte[] bytes = Files.readAllBytes(Paths.get(path));
  //       run(new String(bytes, Charset.defaultCharset()));
  //   }
  //
  //   private static void run(String program) {
  //       Tokenizer tokenizer = new Tokenizer(program);
  //       List<Token> tokens = tokenizer.scanTokens();
  //
  //       for (Token token : tokens) {
  //           System.out.println(token);
  //       }
  // }
  //
  //   private static void runPrompt() throws IOException {
  //       InputStreamReader input = new InputStreamReader(System.in);
  //       BufferedReader reader = new BufferedReader(input);
  //
  //       for (;;) { 
  //           System.out.print("lemms> ");
  //           String line = reader.readLine();
  //           if (line == null) break;
  //           run(line);
  //       }
  //   }

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Usage: lemms ./script");
            System.exit(1); 
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }
}
