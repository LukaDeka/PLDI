package com.lemms.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.lemms.SyntaxNode.*;

public class Interpreter implements StatementVisitor {
    public Environment environment;
    
    public void interpret(StatementNode statementNode) {        
        statementNode.accept(this);
    }

    @Override
    public void visitIfStatement(IfNode ifNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitIfStatement'");
    }

    @Override
    public void visitWhileStatement(WhileNode whileNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitWhileStatement'");
    }

    @Override
    public void visitBlockStatement(BlockNode blockNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitBlockStatement'");
    }

    @Override
    public void visitPrintStatement(PrintNode printNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitPrintStatement'");
    }

    @Override
    public void visitAssignmentNode(AssignmentNode assignmentNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitAssignmentNode'");
    }

}
