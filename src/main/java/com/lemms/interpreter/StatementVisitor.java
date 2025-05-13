package com.lemms.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.lemms.SyntaxNode.*;

public interface StatementVisitor {
    public void visitIfStatement(IfNode ifNode);
    public void visitWhileStatement(WhileNode whileNode);
    public void visitBlockStatement(BlockNode blockNode);
    public void visitPrintStatement(PrintNode printNode);
    public void visitAssignmentNode(AssignmentNode assignmentNode);
    
}
