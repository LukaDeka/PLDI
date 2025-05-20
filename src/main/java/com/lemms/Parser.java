package com.lemms;
import com.lemms.SyntaxNode.BlockNode;
import com.lemms.SyntaxNode.Node;
import com.lemms.SyntaxNode.StatementNode;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Parser {

    private ArrayList<StatementNode> rootNodes;

    public Parser(ArrayList<Token> tokens) {
        // Split based on semicolons
        for (Token token : tokens) {
            String x = token.getValue();
        }
    }

    public StatementNode getAST() {
        BlockNode blockNode = new BlockNode();
        blockNode.statements = rootNodes;
        return blockNode;
    }

    public static void main(String[] args) {
    }
}
