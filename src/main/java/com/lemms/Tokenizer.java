package com.lemms;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tokenizer {

    private ArrayList<Token> tokens = new ArrayList<>();

//    private static File file;
//
//    public static ArrayList<Token> getTokens() {
//        return tokens;
//    }
//
//    private void addToken(Type type) {
//        // tokens.add(new Token(type, ));
//    }
//
//    private void getNextToken(String file) {
//    }
//
//    public Tokenizer(File path) {
//        this.file = file;
//    }


    public Tokenizer(Path path) {
        path = Paths.get("src/main/resources/exampleCode2.txt"); //Alternativ mit ClassLoader //delete line later
        System.out.println("Path: " + path);
        tokenize(path);
    }

    public Tokenizer(String path) {
        this(Paths.get(path)); //Alternativ funktioniert auch mit ClassLoader
    }

    public ArrayList<Token> getTokens(){
        return tokens;
    }

    public void tokenize(Path path) {
        //Quellcode-File auslesen
        try (Stream<String> lines = Files.lines(path)) { //.readString(path); //.readAllBytes(path); //.readAllLines(path);
            AtomicInteger lineCounter = new AtomicInteger(0);

              //String Zerlegung
//            tokens = lines
//                    .map(line -> line.replaceAll("([{}=;])"," $1 ").trim().split("\\s+"))
//                    .flatMap(Arrays::stream)
//                    .map(value-> new Token(value, lineCounter.getAndIncrement(), Token.determineType(value)))
//                    //.filter(token -> !token.isEmpty()) //empty lines
//                    .collect(Collectors.toCollection(ArrayList::new));

            List<String> lines2 = Files.readAllLines(path);
            for (int i = 0; i < lines2.size(); i++) {
                String line = lines2.get(i);
                String[] splitted = line.replaceAll("([{}=;])", " $1 ").trim().split("\\s+");
                for (String tokenValue : splitted) {
                    if (!tokenValue.isEmpty()) {
                        tokens.add(new Token(tokenValue, i, Token.determineType(tokenValue)));
                    }
                }
            }



            System.out.println("T-Size: " + tokens.size());
            System.out.println("Tokens: " + tokens);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
