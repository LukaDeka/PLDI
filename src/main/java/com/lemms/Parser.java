package com.lemms;
import com.lemms.SyntaxNode.Node;
import com.lemms.SyntaxNode.WhileNode;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Parser {

    private ArrayList<Token> getOneLine(ArrayList<Token> fileTokens, int currentLine){
        ArrayList<Token> line = new ArrayList<>();

        for (Token token : fileTokens){
            // break condition for tokens beyond the current line
            if (token.getLine() > currentLine){
                return line;
            }

            if (token.getLine() == currentLine){
                line.add(token);
            }
        }

        // "last line in file" case
        if (line.isEmpty()) return null;
        else return line;
    }

    private void parseLine(){
        //tryAssignmentParse()
        //tryStatementParse
        //tryWhileParse
        //tryIfParse
    }

    private ArrayList<Node> rootNodes;

    public Parser(ArrayList<Token> fileTokens) {
        // Split based on semicolons
        int currentLine = 0;
        ArrayList<Token> line_tokens ;

        while (true){
            line_tokens = getOneLine(fileTokens, currentLine);
            currentLine++;
            if (line_tokens == null) break;

            if (line_tokens.isEmpty()) continue; // here an empty tree should be added

            System.out.println(line_tokens);

            parseLine();
        }
    }

    public ArrayList<Node> getAST() {
        return rootNodes;
    }

    public static void main(String[] args) {
    }
}
