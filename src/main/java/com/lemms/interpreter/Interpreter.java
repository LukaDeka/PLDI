package com.lemms.interpreter;

import com.lemms.SyntaxNode.*;

public class Interpreter implements StatementVisitor, ValueVisitor {
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

    @Override
    public Object visitVariableValue(VariableNode variableNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitVariableValue'");
    }

    @Override
    public Object visitLiteralValue(LiteralNode literalNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitLiteralValue'");
    }

    @Override
    public Object visitOperatorValue(OperatorNode literalNode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitOperatorValue'");
    }

}
