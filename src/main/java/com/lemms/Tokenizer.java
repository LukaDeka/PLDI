package com.lemms;
import java.io.*;
import java.util.Scanner;

public class Tokenizer {

    private void readFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(" ");
                for (String token : tokens) {
                    System.out.println(token);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Tokenizer(File file) {

    }

    public static void main(String[] args) {
    }
}
