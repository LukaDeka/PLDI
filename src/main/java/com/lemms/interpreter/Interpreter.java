package com.lemms.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.lemms.SyntaxNode.*;

public class Interpreter {
    public Environment environment;
    
    public void interpret(StatementNode statementNode) {        
        
    }

    public void visitStatement(StatementNode statementNode) 
    {
        if(statementNode instanceof WhileNode) {
            visitWhileNode((WhileNode)statementNode);
        }
    }

    public Object evaluateValueNode(ValueNode valueNode) {
        return Boolean.TRUE;
    }

    public void visitWhileNode(WhileNode whileNode) {        
        while (evaluateValueNode(whileNode.condition) == Boolean.TRUE) {
            visitStatement(whileNode.statement);
        }
    }

}
