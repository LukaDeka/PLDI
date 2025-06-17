package com.lemms.interpreter;

import com.lemms.SyntaxNode.*;

public interface StatementVisitor {
    public FlowSignal visitIfStatement(IfNode ifNode);
    public FlowSignal visitWhileStatement(WhileNode whileNode);
    public FlowSignal visitBlockStatement(BlockNode blockNode);
    public FlowSignal visitAssignmentNode(AssignmentNode assignmentNode);
    public FlowSignal visitFunctionCallStatement(FunctionCallStatementNode functionCallStatementNode);
    public void visitFunctionDeclarationStatement(FunctionDeclarationNode functionDeclarationNode);
    public void visitClassDeclarationStatement(ClassDeclarationNode classDeclarationNode);
    public FlowSignal visitReturnNode(ReturnNode returnNode);
}
