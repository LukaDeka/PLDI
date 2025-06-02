package com.lemms;

import java.io.File;

import com.lemms.SyntaxNode.StatementNode;
import com.lemms.interpreter.Interpreter;
import com.lemms.parser.Parser;

import java.util.ArrayList;
import java.util.List;

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
                    
                    List<StatementNode> programStatements = p.parse();

                    // System.out.println(programStatements.toString());

                    Interpreter i = new Interpreter(programStatements);
                    i.interpret();

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