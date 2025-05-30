package com.lemms;

import java.io.File;

import com.lemms.SyntaxNode.StatementNode;
import com.lemms.interpreter.Interpreter;

import java.util.ArrayList;

public class Test {


    public static void main(String[] args){

        File dir = new File("src/main/resources/successTests");
        File[] directoryListing = dir.listFiles();

        int exceptionCounter = 0;

        if (directoryListing != null) {
            for (File file : directoryListing) {
                // Test right File name
                if (!(file.getName().split("\\.")[1].equals("lemms")) || file.getName().split("\\.").length != 2){
                    System.out.println("Wrong file Name :(");
                    continue;
                }


                try {
                    System.out.println("Currently using "+ file.getName());
                    Tokenizer t = new Tokenizer(file);
                    Parser p = new Parser(t.getTokens());
                    p.parseStatements();
                    ArrayList<StatementNode> x = p.getAST();

                    System.out.println(p.getAST().toString());

                    Interpreter i = new Interpreter(p.getAST());

                } catch (Exception e) {
                    System.out.println("Something went wrong in LEMMS");
                    exceptionCounter++;
                }
            }
        } else {
            System.out.println("given path is not a directoy :(");
        }



    }
}