package com.lemms.interpreter;

import com.lemms.SyntaxNode.*;

public interface StatementVisitor {
    public void visitIfStatement(IfNode ifNode);
    public void visitWhileStatement(WhileNode whileNode);
    public void visitBlockStatement(BlockNode blockNode);
    public void visitPrintStatement(PrintNode printNode);
    public void visitAssignmentNode(AssignmentNode assignmentNode);

}
