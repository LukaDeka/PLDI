package com.lemms;
import com.lemms.SyntaxNode.Node;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Parser {

    private ArrayList<Node> rootNodes;

    public Parser(Token[] tokens) {
        // Split based on semicolons
        for (Token token : tokens) {
            String x = token.value;
        }
    }

    public static void main(String[] args) {
    }
}
